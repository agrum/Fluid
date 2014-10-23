import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.*;
 

public class Eye {
	//Projection info
	protected int m_projectionType;
	protected static final int PROJECTION_PERSP = 0;
	protected static final int PROJECTION_ORTHO = 1;
	protected float m_aspectRatio;
	protected float m_FOVHorizontal;
	protected float m_width;
	protected float m_nearPlane;
	protected float m_farPlane;
	protected Matrix4f m_projMatrix;

	//View info
	protected Vector3f m_position;
	protected Vector3f m_direction;
	protected Vector3f m_horizontal;
	protected Vector3f m_vertical;
	protected Matrix4f m_viewMatrix;
	
	// Entry point for the application
	/*public static void main(String[] args) {
		Eye eye = new Eye();

		eye.move(10, 20, 30);
		eye.lookLeft(90.0f);
	}*/

	public Eye() {		
		m_projectionType = PROJECTION_PERSP;
		m_aspectRatio = 1.6f;
		m_FOVHorizontal = 120.0f;
		m_nearPlane = 0.1f;
		m_farPlane = 100.0f;
		m_projMatrix = new Matrix4f();
		
		m_position = new Vector3f(0,0,0);
		m_direction = new Vector3f(0,0,1);
		m_horizontal = new Vector3f(1,0,0);
		m_vertical = new Vector3f(0,1,0);
		m_viewMatrix = new Matrix4f();
		
		setProjection();
		setView();
	}
	
	//Projection methods
	//Set
	public void setProjectionType(int p_projectionType)
	{
		if(p_projectionType == m_projectionType)
			return;
		switch(p_projectionType)
		{
		case PROJECTION_PERSP : 
			m_FOVHorizontal = 180.0f / (float) Math.PI * (float) Math.asin(m_width / (2.0f * m_farPlane));
			break;
		case PROJECTION_ORTHO : 		
			m_width = m_farPlane * (float) Math.sin(Math.PI / 180.0 * Math.sin(m_FOVHorizontal));
			break;
		default :
			return;
		}
		
		m_projectionType = p_projectionType;
		setProjection();
	}
	public void setAspectRatio(float p_aspectratio)
	{
		assert(p_aspectratio > 0.0);
		assert(p_aspectratio < 10000.0);

		m_aspectRatio = p_aspectratio;
		setProjection();
	}
	public void setFOVHorizontal(float p_angleDegre)
	{
		assert(p_angleDegre > 0.0);
		assert(p_angleDegre < 180.0);

		if(m_projectionType != PROJECTION_PERSP)
			setProjectionType(PROJECTION_PERSP);

		m_FOVHorizontal = p_angleDegre;
		setProjection();
	}
	public void setFOVVertical(float p_angleDegre)
	{
		assert(p_angleDegre > 0.0);
		assert(p_angleDegre * m_aspectRatio < 180.0);

		if(m_projectionType != PROJECTION_PERSP)
			setProjectionType(PROJECTION_PERSP);

		m_FOVHorizontal = p_angleDegre * m_aspectRatio;
		setProjection();
	}
	public void setWidth(float p_width)
	{
		assert(p_width > 0.0);

		if(m_projectionType != PROJECTION_PERSP)
			setProjectionType(PROJECTION_PERSP);

		m_width = p_width;
		setProjection();
	}
	public void setVisibility(float p_near, float p_far)
	{
		m_nearPlane = p_near;
		m_farPlane = p_far;
		
		setProjection();
	}
	public void setNearVisibility(float p_near)
	{
		m_nearPlane = p_near;
		
		setProjection();
	}
	public void setFarVisibility(float p_far)
	{
		m_farPlane = p_far;

		setProjection();
	}
	//Get
	public int projectionType() { return m_projectionType; }
	public double aspectRatio() { return m_aspectRatio; }
	public double FOVHorizontal() { return m_FOVHorizontal; }
	public Vector2f area() { return new Vector2f(m_width, m_width/m_aspectRatio);}
	public double nearVisibility() {return m_nearPlane; }
	public double farVisibility() { return m_farPlane; }
	//Projection matrix
	public Matrix4f projMatrix() { return m_projMatrix; }

	//View methods
	//Set
	public void move(float p_right, float p_up, float p_forward)
	{
		Vector3f forward = new Vector3f(m_direction);
		Vector3f left = new Vector3f(m_horizontal);
		Vector3f up = new Vector3f(m_vertical);
		
		forward.scale(p_forward);
		left.scale(p_right);
		up.scale(p_up);
		Vector3f.add(m_position, forward, m_position);
		Vector3f.add(m_position, left, m_position);
		Vector3f.add(m_position, up, m_position);
		
		setView();
	}
	public void lookUp(float p_angle)
	{
		float radHalfAngle = p_angle / 2.0f; 
		float sinVal = (float) Math.sin(radHalfAngle);
		float cosVal = (float) Math.cos(radHalfAngle);
		
		Quaternion qVector = new Quaternion(m_horizontal.x * sinVal, m_horizontal.y * sinVal, m_horizontal.z * sinVal, cosVal);
		Quaternion qVertical = new Quaternion(m_vertical.x, m_vertical.y, m_vertical.z, 0);
		Quaternion qTemp = new Quaternion();

		Quaternion.mul(qVector, qVertical, qTemp);
		Quaternion.mulInverse(qTemp, qVector, qTemp);
		qTemp.normalise();
		m_vertical.x = qTemp.x;
		m_vertical.y = qTemp.y;
		m_vertical.z = qTemp.z;

		Vector3f.cross(m_horizontal, m_vertical, m_direction);
		
		setView();
	}
	public void lookLeft(float p_angle)
	{
		float radHalfAngle = p_angle / 2.0f; 
		float sinVal = (float) Math.sin(radHalfAngle);
		float cosVal = (float) Math.cos(radHalfAngle);
		
		Quaternion qVector = new Quaternion(m_vertical.x * sinVal, m_vertical.y * sinVal, m_vertical.z * sinVal, cosVal);
		Quaternion qDirection = new Quaternion(m_direction.x, m_direction.y, m_direction.z, 0);
		Quaternion qTemp = new Quaternion();

		Quaternion.mul(qVector, qDirection, qTemp);
		Quaternion.mulInverse(qTemp, qVector, qTemp);
		qTemp.normalise();
		m_direction.x = qTemp.x;
		m_direction.y = qTemp.y;
		m_direction.z = qTemp.z;

		Vector3f.cross(m_vertical, m_direction, m_horizontal);
		
		setView();
	}
	public void lookSideway(float p_angle)
	{
		float radHalfAngle = p_angle / 2.0f; 
		float sinVal = (float) Math.sin(radHalfAngle);
		float cosVal = (float) Math.cos(radHalfAngle);
		
		Quaternion qVector = new Quaternion(m_direction.x * sinVal, m_direction.y * sinVal, m_direction.z * sinVal, cosVal);
		Quaternion qHorizontal = new Quaternion(m_horizontal.x, m_horizontal.y, m_horizontal.z, 0);
		Quaternion qTemp = new Quaternion();

		Quaternion.mul(qVector, qHorizontal, qTemp);
		Quaternion.mulInverse(qTemp, qVector, qTemp);
		qTemp.normalise();
		m_horizontal.x = qTemp.x;
		m_horizontal.y = qTemp.y;
		m_horizontal.z = qTemp.z;

		Vector3f.cross(m_direction, m_horizontal, m_vertical);
		
		setView();
	}
	public void lookAt(Vector3f position, Vector3f center, Vector3f up)
	{
		m_position = position;
		
		Vector3f.sub(center, position, m_direction);
		m_direction.normalise();
		
		Vector3f.cross(m_direction, up, m_horizontal);
		m_horizontal.normalise();
		m_horizontal.negate();
		
		Vector3f.cross(m_horizontal, m_direction, m_vertical);
		m_vertical.normalise();
		m_vertical.negate();
		
		setView();
	}
	//Get
	public Vector3f position() { return m_position; }
	public Vector3f direction() { return m_direction; }
	public Vector3f vertical() { return m_vertical; }
	//View matrix
	public Matrix4f viewMatrix() { return m_viewMatrix; }
	
	protected void setPerspective(float fovH, float ratio, float zNear, float zFar) 
	{		
		m_projMatrix.setIdentity();
		m_projMatrix.m11 = (float) (1.0f / Math.tan(0.5 * fovH * Math.PI / 180.0f / m_aspectRatio));
		m_projMatrix.m00 = m_projMatrix.m11 / m_aspectRatio;
		m_projMatrix.m22 = -(zFar + zNear) / (zFar - zNear);
		m_projMatrix.m32 = -2.0f * (zFar * zNear) / (zFar - zNear);
		m_projMatrix.m23 = -1;
		m_projMatrix.m33 = 0;
	}
	protected void setOrtho(float l, float r, float b, float t, float n, float f) 
	{
		m_projMatrix.setIdentity();
		m_projMatrix.m00 = 2.0f / (r - l);
		m_projMatrix.m11 = 2.0f / (t - b);
		m_projMatrix.m22 = -2.0f / (f - n);
		m_projMatrix.m30 = -(r + l) / (r - l);
		m_projMatrix.m31 = -(t + b) / (t - b);
		m_projMatrix.m32 = -(f + n) / (f - n);
		m_projMatrix.m33 = 1.0f;
	}
	protected void setProjection() 
	{
		switch(m_projectionType)
		{
		case PROJECTION_PERSP :
			setPerspective(
				m_FOVHorizontal,
				m_aspectRatio,
				m_nearPlane,
				m_farPlane);
			break;
		case PROJECTION_ORTHO :
			setOrtho(
				-m_width / 2.0f,
				m_width / 2.0f,
				-m_width / 2.0f / m_aspectRatio,
				m_width / 2.0f / m_aspectRatio,
				m_nearPlane,
				m_farPlane);
			break;
		}
	}
	
	protected void setView()
	{
		m_direction.normalise();
		m_horizontal.normalise();
		m_vertical.normalise();
		
		m_viewMatrix.setIdentity();
		m_viewMatrix.m00 = -m_horizontal.x;
		m_viewMatrix.m10 = -m_horizontal.y;
		m_viewMatrix.m20 = -m_horizontal.z;
		m_viewMatrix.m30 = Vector3f.dot(m_position, m_horizontal);
		m_viewMatrix.m01 = m_vertical.x;
		m_viewMatrix.m11 = m_vertical.y;
		m_viewMatrix.m21 = m_vertical.z;
		m_viewMatrix.m31 = -Vector3f.dot(m_position, m_vertical);
		m_viewMatrix.m02 = -m_direction.x;
		m_viewMatrix.m12 = -m_direction.y;
		m_viewMatrix.m22 = -m_direction.z;
		m_viewMatrix.m32 = Vector3f.dot(m_position, m_direction);
	}
}
