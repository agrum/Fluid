import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Particle {
	protected static final int POSITION_X = 0*4;
	protected static final int POSITION_Y = 1*4;
	protected static final int POSITION_Z = 2*4;
	protected static final int POSITION_W = 3*4;
	protected static final int VELOCITY_X = 4*4;
	protected static final int VELOCITY_Y = 5*4;
	protected static final int VELOCITY_Z = 6*4;
	protected static final int VELOCITY_W = 7*4;
	protected static final int MASS = 8*4;
	protected static final int AMASS = 9*4;
	protected static final int DENSITY = 10*4;
	protected static final int REZ = 11*4;
	protected static final int NEXT = 15*4;
	
	protected static final int MAX = 16*4;
	
	ByteBuffer m_information = BufferUtils.createByteBuffer(MAX);
	
	Particle ()
	{
		setPosition(new Vector3f(0,0,0));
		setVelocity(new Vector3f(0,0,0));
		setMass(1f);
		setDensity(1f);
		m_information.putInt(NEXT, 0xffffffff);
	}
	
	ByteBuffer buffer()
	{
		return m_information;
	}
	
	public static int GLSLSize()
	{
		return 16 * (1 + (MAX - 1)/16);
	}
	
	Vector3f getPosition()
	{
		Vector3f position = new Vector3f();
		
		position.setX(m_information.getFloat(POSITION_X));
		position.setY(m_information.getFloat(POSITION_Y));
		position.setZ(m_information.getFloat(POSITION_Z));
		
		return position;
	}
	
	void setPosition(Vector3f p_position)
	{
		m_information.putFloat(POSITION_X, p_position.getX());
		m_information.putFloat(POSITION_Y, p_position.getY());
		m_information.putFloat(POSITION_Z, p_position.getZ());
		m_information.putFloat(POSITION_W, 1.0f);
	}
	
	void setVelocity(Vector3f p_velocity)
	{
		m_information.putFloat(VELOCITY_X, p_velocity.getX());
		m_information.putFloat(VELOCITY_Y, p_velocity.getY());
		m_information.putFloat(VELOCITY_Z, p_velocity.getZ());
		m_information.putFloat(VELOCITY_W, 1.0f);
	}
	
	void setMass(float p_mass)
	{
		m_information.putFloat(MASS, p_mass);
	}
	
	void setDensity(float p_radius)
	{
		m_information.putFloat(DENSITY, p_radius);
	}
}
