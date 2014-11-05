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
		exitOnGLError("ComputeProgram::bind()_0");
		GL20.glUseProgram(m_id);
		exitOnGLError("ComputeProgram::bind()_1");
		m_bound = true;
		
		setPendingUniforms();
		exitOnGLError("ComputeProgram::bind()_2");
	}
	
	public void release() {
		GL20.glUseProgram(0);
		m_bound = false;
	}
}
