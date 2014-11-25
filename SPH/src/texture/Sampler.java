package texture;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL33;

public class Sampler {
	private int m_id;
	
	public enum WrapMode {
		Repeat (GL11.GL_REPEAT),
		MirroredRepeat (GL14.GL_MIRRORED_REPEAT),
		ClampToEdge (GL12.GL_CLAMP_TO_EDGE),
		ClampToBorder (GL13.GL_CLAMP_TO_BORDER);
		
		public int m;
		
		private WrapMode(int p){
			this.m = p;
		}
	}
	
	public enum InterpolationMagFilter {
		Nearest (GL11.GL_NEAREST),
		Linear (GL11.GL_LINEAR);
		
		public int m;
		
		private InterpolationMagFilter(int p){
			this.m = p;
		}
	}
	
	public enum InterpolationMinFilter {
		Nearest (GL11.GL_NEAREST),
		Linear (GL11.GL_LINEAR),
		Nearest_MipmapNearest (GL11.GL_NEAREST_MIPMAP_NEAREST),
		Linear_MipmapNearest (GL11.GL_LINEAR_MIPMAP_NEAREST),
		Nearest_MipmapLinear (GL11.GL_NEAREST_MIPMAP_LINEAR),
		Linear_MipmapLinear (GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		public int m;
		
		private InterpolationMinFilter(int p){
			this.m = p;
		}
	}
	
	public Sampler()
	{
		m_id = GL33.glGenSamplers();
	}
	
	public void finalize()
	{
		GL33.glDeleteSamplers(m_id);
	}
	
	public void bind(int p_textureUnit)
	{
		GL33.glBindSampler(p_textureUnit, m_id);
	}
	
	public void setWrapHorizontal(WrapMode p_wrapMode)
	{
		GL33.glSamplerParameteri(m_id, GL11.GL_TEXTURE_WRAP_S, p_wrapMode.m);
	}
	
	public void setWrapVertical(WrapMode p_wrapMode)
	{
		GL33.glSamplerParameteri(m_id, GL11.GL_TEXTURE_WRAP_T, p_wrapMode.m);
	}
	
	public void setBorderColor(FloatBuffer p_color)
	{
		GL33.glSamplerParameter(m_id, GL11.GL_TEXTURE_BORDER_COLOR, p_color);
	}
	
	public void setMagFilter(InterpolationMagFilter p_interpolationFilter)
	{
		GL33.glSamplerParameteri(m_id, GL11.GL_TEXTURE_MAG_FILTER, p_interpolationFilter.m);
	}
	
	public void setMinFilter(InterpolationMinFilter p_interpolationFilter)
	{
		GL33.glSamplerParameteri(m_id, GL11.GL_TEXTURE_MIN_FILTER, p_interpolationFilter.m);
	}
	
	public void setCustom(int p_name, int p_param)
	{
		GL33.glSamplerParameteri(m_id, p_name, p_param);
	}
}
