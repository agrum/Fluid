import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;


public class PassRender implements AbstractPass {
	private ShaderProgram m_program;
	private IntBuffer m_vao = BufferUtils.createIntBuffer(1);
	private IntBuffer m_ibo = BufferUtils.createIntBuffer(1);
	
	private SpaceMap m_spaceMap = null;
	private Eye m_eye = null;
	private int m_viewportWidth = 0;
	private int m_viewportHeight = 0;
	
	public void setSpaceMap(SpaceMap p_spaceMap)
	{
		m_spaceMap = p_spaceMap;
	}
	
	public void setEye(Eye p_eye)
	{
		m_eye = p_eye;
	}

	public void setViewportSize(
			int p_width,
			int p_height)
	{
		m_viewportWidth = p_width;
		m_viewportHeight = p_height;
	}
	
	@Override
	public void initialize() {
		//Init shader program
		m_program = new ShaderProgram();
		m_program.addShaderFromFile(ShaderProgram.ShaderType.Vertex, "resource/billboard.vert");
		m_program.addShaderFromFile(ShaderProgram.ShaderType.Geometry, "resource/billboard.geom");
		m_program.addShaderFromFile(ShaderProgram.ShaderType.Fragment, "resource/billboard.frag");
		m_program.link();

		//Indice buffer
		int maxIndices = 100000;
		IntBuffer indices = BufferUtils.createIntBuffer(maxIndices);
		for(int i = 0; i < maxIndices; i++)
			indices.put(i);
		indices.flip();
		GL15.glGenBuffers(m_ibo);
		GL15.glBindBuffer(
				GL15.GL_ELEMENT_ARRAY_BUFFER, 
				m_ibo.get(0));
		GL15.glBufferData(
	    		GL15.GL_ELEMENT_ARRAY_BUFFER, 
	    		indices, 
	    		GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(
				GL15.GL_ELEMENT_ARRAY_BUFFER, 
				0);

		GL30.glGenVertexArrays(m_vao);
		exitOnGLError("meh");
	}

	@Override
	public void render() {
		Cube front = m_spaceMap.front();
		
		// Set the display viewport
		GL11.glViewport(0, 0, m_viewportWidth, m_viewportHeight);
		
		// Clear color buffer
		GL11.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		float width  = (float) m_viewportWidth;
		float height = (float) m_viewportHeight;
		m_eye.setAspectRatio(width / height);
		m_eye.setFOVHorizontal((float) (180.0 / Math.PI * Math.sin(width / 1000.0)));

		m_program.bind();

		FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);
		Matrix4f viewProjMatrix = new Matrix4f();
		Matrix4f.mul(m_eye.projMatrix(), m_eye.viewMatrix(), viewProjMatrix);
		
		m_eye.projMatrix().store(matrix44Buffer);  matrix44Buffer.flip();
		GL20.glUniformMatrix4(
				GL20.glGetUniformLocation(m_program.id(), "uP"),
				false,
				matrix44Buffer);
		m_eye.viewMatrix().store(matrix44Buffer);  matrix44Buffer.flip();
		GL20.glUniformMatrix4(
				GL20.glGetUniformLocation(m_program.id(), "uMV"),
				false,
				matrix44Buffer);
		viewProjMatrix.store(matrix44Buffer);  matrix44Buffer.flip();
		GL20.glUniformMatrix4(
				GL20.glGetUniformLocation(m_program.id(), "uMVP"),
				false,
				matrix44Buffer);
		GL20.glUniform1f(
				GL20.glGetUniformLocation(m_program.id(), "uWindowWidth"),
				(float) width);
		GL20.glUniform1f(
				GL20.glGetUniformLocation(m_program.id(), "uWindowHeight"),
				(float) height);

		GL30.glBindVertexArray(m_vao.get(0));

		Particle plop = front.particleAt(0);
		Particle plip = front.particleAt(9);
		Vector3f pos1 = plop.getPosition();
		Vector3f pos2 = plip.getPosition();

		GL20.glEnableVertexAttribArray(
				GL20.glGetAttribLocation(m_program.id(), "aPosition"));
		/*GL20.glEnableVertexAttribArray(
				GL20.glGetAttribLocation(m_program.id(), "aVelocity"));*/
		GL20.glEnableVertexAttribArray(
				GL20.glGetAttribLocation(m_program.id(), "aDensity"));

		exitOnGLError("0");
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, m_ibo.get(0));
		front.bindParticleArray(
			m_program,
			"aPosition",
			"aVelocity",
			"aDensity");
		exitOnGLError("2");
		GL11.glDrawElements(
				GL11.GL_POINTS, 
				front.amountParticles(), 
				GL11.GL_UNSIGNED_INT, 
				0);

		exitOnGLError("1");
		GL20.glDisableVertexAttribArray(
				GL20.glGetAttribLocation(m_program.id(), "aPosition"));
		/*GL20.glDisableVertexAttribArray(
				GL20.glGetAttribLocation(m_program.id(), "aVelocity"));*/
		GL20.glDisableVertexAttribArray(
				GL20.glGetAttribLocation(m_program.id(), "aDensity"));
		exitOnGLError("3");

		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
		GL15.glBindBuffer(
				GL15.GL_ELEMENT_ARRAY_BUFFER, 
				0);
		
		GL30.glBindVertexArray(0);

		m_program.release();
		exitOnGLError("4");
	}

	private void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}
}
