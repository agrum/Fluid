package Default;
import org.lwjgl.opengl.GL20;


public class ComputeProgram extends ShaderProgram{
	public ComputeProgram(String p_ComputeShaderPath)
	{
		super();

		addShaderFromFile(ShaderProgram.ShaderType.Compute, p_ComputeShaderPath);
		
		link();
		
		exitOnGLError("PLOP");
	}
	
	public void bind() {
		GL20.glUseProgram(m_id);
		m_bound = true;
		
		setPendingUniforms();
	}
	
	public void release() {
		GL20.glUseProgram(0);
		m_bound = false;
	}
}
