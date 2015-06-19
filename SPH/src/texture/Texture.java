package texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import lemon.program.ShaderProgram;

import org.lwjgl.opengl.GL11;

public abstract class Texture {
	protected String m_name;
	protected int m_id;
	protected int m_internalFormat;
	protected int m_format;
	protected int m_type;
	protected boolean m_updated;

	public Texture(String p_name, int p_internalFormat, int p_format)
	{
		m_id = GL11.glGenTextures();
		m_name = p_name;
		m_internalFormat = p_internalFormat;
		m_format = p_format;
	}
	
	public void finalize()
	{
		GL11.glDeleteTextures(m_id);
	}

	abstract void bind(int p_textureUnit, ShaderProgram p_program, Sampler p_sampler);
	abstract void setData(ByteBuffer p_data) throws Exception;
	abstract void setData(IntBuffer p_data) throws Exception;
	abstract void setData(FloatBuffer p_data) throws Exception;
}
