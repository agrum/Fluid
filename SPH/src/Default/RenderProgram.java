package Default;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class RenderProgram extends ShaderProgram {
	private Set<Integer> m_attribMap = new TreeSet<Integer>();
	
	public RenderProgram(
			String p_vertexShaderPath,
			String p_geometryShaderPath,
			String p_fragmentShaderPath)
	{
		super();

		addShaderFromFile(ShaderProgram.ShaderType.Vertex, p_vertexShaderPath);
		addShaderFromFile(ShaderProgram.ShaderType.Geometry, p_geometryShaderPath);
		addShaderFromFile(ShaderProgram.ShaderType.Fragment, p_fragmentShaderPath);
		
		link();
		
		exitOnGLError("PLOP");
	}
	
	public void bind(int p_vao)
	{
		GL30.glBindVertexArray(p_vao);
		exitOnGLError("1");
		GL20.glUseProgram(m_id);
		exitOnGLError("2");
		m_bound = true;
		exitOnGLError("3");
		
		setPendingUniforms();
		
		for(Integer loc : m_attribMap)
			GL20.glEnableVertexAttribArray(loc);
		exitOnGLError("4");
	}
	
	public void release() {
		for(Integer loc : m_attribMap)
			GL20.glDisableVertexAttribArray(loc);
		
		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
		m_bound = false;
	}
	
	void addVertexAttribArray(String p_name)
	{
		int loc = GL20.glGetAttribLocation(m_id, p_name);
		
		if(loc != -1 && !m_attribMap.contains(loc))
			m_attribMap.add(loc);
	}

	public void setAttributeBuffer(
			String p_name,
			int p_type,
			int p_offset,
			int p_tupleSize,
			int p_stride)
	{
		GL20.glVertexAttribPointer(
				GL20.glGetAttribLocation(m_id, p_name),
				p_tupleSize,
				p_type,
				true,
				p_stride,
				p_offset);
	}
}
