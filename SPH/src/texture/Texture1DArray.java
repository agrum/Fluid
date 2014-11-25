package texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

import Default.ShaderProgram;

public class Texture1DArray extends Texture {
	private Sampler m_sampler = null;

	public Texture1DArray(String p_name, int p_internalFormat, int p_format) {
		super(p_name, p_internalFormat, p_format);
		// TODO Auto-generated constructor stub
	}

	@Override
	void bind(int p_textureUnit, ShaderProgram p_program, Sampler p_sampler)
	{
		p_program.setUniform(m_name, p_textureUnit);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + p_textureUnit);
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, m_id);
		if(m_sampler != null)
			m_sampler.bind(p_textureUnit);
	}

	void setSampler(Sampler p_sampler)
	{
		m_sampler = p_sampler;
	}

	@Override
	void setData(ByteBuffer p_data) throws Exception
	{
		if(m_type != GL11.GL_UNSIGNED_BYTE && m_type != GL11.GL_BYTE)
			throw new Exception("Invalid data type fed to the texture");
		
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, m_id);
		GL42.glTexStorage2D(GL30.GL_TEXTURE_1D_ARRAY, 1, m_internalFormat, p_data.limit(), 1);
		GL11.glTexSubImage2D(GL30.GL_TEXTURE_1D_ARRAY, 0, 0, 0, p_data.limit(), 1, m_format, m_type, p_data);
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, 0);
	}

	@Override
	void setData(IntBuffer p_data) throws Exception
	{
		if(m_type != GL11.GL_UNSIGNED_INT && m_type != GL11.GL_INT)
			throw new Exception("Invalid data type fed to the texture");
		
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, m_id);
		GL42.glTexStorage2D(GL30.GL_TEXTURE_1D_ARRAY, 1, m_internalFormat, p_data.limit(), 1);
		GL11.glTexSubImage2D(GL30.GL_TEXTURE_1D_ARRAY, 0, 0, 0, p_data.limit(), 1, m_format, m_type, p_data);
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, 0);
	}

	@Override
	void setData(FloatBuffer p_data) throws Exception
	{
		if(m_type != GL11.GL_FLOAT)
			throw new Exception("Invalid data type fed to the texture");
		
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, m_id);
		GL42.glTexStorage2D(GL30.GL_TEXTURE_1D_ARRAY, 1, m_internalFormat, p_data.limit(), 1);
		GL11.glTexSubImage2D(GL30.GL_TEXTURE_1D_ARRAY, 0, 0, 0, p_data.limit(), 1, m_format, m_type, p_data);
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, 0);
	}
}
