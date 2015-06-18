package Default;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;


public class ShaderProgram {
	// Shader variables
	protected int m_id = 0;
	protected boolean m_bound = false;
	private Map<Integer, Map.Entry<Integer, IntBuffer>> m_pendingUniformI = new TreeMap<Integer, Map.Entry<Integer, IntBuffer>>();
	private Map<Integer, Map.Entry<Integer, FloatBuffer>> m_pendingUniformF = new TreeMap<Integer, Map.Entry<Integer, FloatBuffer>>();
	private Map<Integer, Map.Entry<Integer, FloatBuffer>> m_pendingUniformM = new TreeMap<Integer, Map.Entry<Integer, FloatBuffer>>();
	
	public enum ShaderType {
		Vertex (GL20.GL_VERTEX_SHADER),
		Geometry (GL32.GL_GEOMETRY_SHADER),
		Fragment (GL20.GL_FRAGMENT_SHADER),
		Compute (GL43.GL_COMPUTE_SHADER);
		
		public int m;
		
		private ShaderType(int p){
			this.m = p;
		}
	}
	
	protected ShaderProgram() {
		m_id = GL20.glCreateProgram();
	}
	
	protected void finalize() {
		GL20.glDeleteProgram(m_id);
	}
	
	public void setUniform(String p_name, int p_v1)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		buffer.put(0, p_v1);
	
		if(!m_bound)
			m_pendingUniformI.put(loc, new AbstractMap.SimpleImmutableEntry<>(1, buffer));
		else
			GL20.glUniform1(loc, buffer);
	}
	
	public void setUniform(String p_name, int p_v1, int p_v2)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		IntBuffer buffer = BufferUtils.createIntBuffer(2);
		buffer.put(0, p_v1);
		buffer.put(1, p_v2);
	
		if(!m_bound)
			m_pendingUniformI.put(loc, new AbstractMap.SimpleImmutableEntry<>(2, buffer));
		else
			GL20.glUniform2(loc, buffer);
	}
	
	public void setUniform(String p_name, int p_v1, int p_v2, int p_v3)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		IntBuffer buffer = BufferUtils.createIntBuffer(3);
		buffer.put(0, p_v1);
		buffer.put(1, p_v2);
		buffer.put(2, p_v3);
	
		if(!m_bound)
			m_pendingUniformI.put(loc, new AbstractMap.SimpleImmutableEntry<>(3, buffer));
		else
			GL20.glUniform3(loc, buffer);
	}
	
	public void setUniform(String p_name, int p_v1, int p_v2, int p_v3, int p_v4)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		IntBuffer buffer = BufferUtils.createIntBuffer(4);
		buffer.put(0, p_v1);
		buffer.put(1, p_v2);
		buffer.put(2, p_v3);
		buffer.put(3, p_v4);
	
		if(!m_bound)
			m_pendingUniformI.put(loc, new AbstractMap.SimpleImmutableEntry<>(4, buffer));
		else
			GL20.glUniform4(loc, buffer);
	}
	
	public void setUniform(String p_name, float p_v1)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;

		FloatBuffer buffer = BufferUtils.createFloatBuffer(1);
		buffer.put(0, p_v1);
	
		if(!m_bound)
			m_pendingUniformF.put(loc, new AbstractMap.SimpleImmutableEntry<>(1, buffer));
		else
			GL20.glUniform1(loc, buffer);
	}
	
	public void setUniform(String p_name, float p_v1, float p_v2)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;

		FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
		buffer.put(0, p_v1);
		buffer.put(1, p_v2);
	
		if(!m_bound)
			m_pendingUniformF.put(loc, new AbstractMap.SimpleImmutableEntry<>(2, buffer));
		else
			GL20.glUniform2(loc, buffer);
	}
	
	public void setUniform(String p_name, float p_v1, float p_v2, float p_v3)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;

		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		buffer.put(0, p_v1);
		buffer.put(1, p_v2);
		buffer.put(2, p_v3);
	
		if(!m_bound)
			m_pendingUniformF.put(loc, new AbstractMap.SimpleImmutableEntry<>(3, buffer));
		else
			GL20.glUniform3(loc, buffer);
	}
	
	public void setUniform(String p_name, float p_v1, float p_v2, float p_v3, float p_v4)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(0, p_v1);
		buffer.put(1, p_v2);
		buffer.put(2, p_v3);
		buffer.put(3, p_v4);
	
		if(!m_bound)
			m_pendingUniformF.put(loc, new AbstractMap.SimpleImmutableEntry<>(4, buffer));
		else
			GL20.glUniform4(loc, buffer);
	}
	
	public void setUniform(String p_name, Matrix2f p_mat)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		p_mat.store(buffer);  buffer.flip();
	
		if(!m_bound)
			m_pendingUniformM.put(loc, new AbstractMap.SimpleImmutableEntry<>(2, buffer));
		else
			GL20.glUniformMatrix2(loc, false, buffer);
	}
	
	public void setUniform(String p_name, Matrix3f p_mat)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
		p_mat.store(buffer);  buffer.flip();
		
		if(!m_bound)
			m_pendingUniformM.put(loc, new AbstractMap.SimpleImmutableEntry<>(3, buffer));
		else
			GL20.glUniformMatrix3(loc, false, buffer);
	}
	
	public void setUniform(String p_name, Matrix4f p_mat)
	{
		int loc; 
		if((loc = GL20.glGetUniformLocation(m_id, p_name)) == -1)
			return;
	
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		p_mat.store(buffer);  buffer.flip();

		if(!m_bound)
			m_pendingUniformM.put(loc, new AbstractMap.SimpleImmutableEntry<>(4, buffer));
		else
			GL20.glUniformMatrix4(loc, false, buffer);
	}
	
	protected void addShaderFromFile(
			ShaderType shaderType,
			String filePath) {
		int vsId = this.loadShader(filePath, shaderType.m);
		
		GL20.glAttachShader(m_id, vsId);
	}
	
	protected void link() {
		GL20.glLinkProgram(m_id);
		GL20.glValidateProgram(m_id);

		this.exitOnGLError("setupShaders");
	}
	
	protected void setPendingUniforms()
	{
		for (Map.Entry<Integer, Map.Entry<Integer, IntBuffer>> entry : m_pendingUniformI.entrySet())
		{
			switch(entry.getValue().getKey()){
			case 1 : GL20.glUniform1(entry.getKey(), entry.getValue().getValue()); break;
			case 2 : GL20.glUniform2(entry.getKey(), entry.getValue().getValue()); break;
			case 3 : GL20.glUniform3(entry.getKey(), entry.getValue().getValue()); break;
			case 4 : GL20.glUniform4(entry.getKey(), entry.getValue().getValue()); break;
			}
		}
		m_pendingUniformI.clear();
		
		for (Map.Entry<Integer, Map.Entry<Integer, FloatBuffer>> entry : m_pendingUniformF.entrySet())
		{
			switch(entry.getValue().getKey()){
			case 1 : GL20.glUniform1(entry.getKey(), entry.getValue().getValue()); break;
			case 2 : GL20.glUniform2(entry.getKey(), entry.getValue().getValue()); break;
			case 3 : GL20.glUniform3(entry.getKey(), entry.getValue().getValue()); break;
			case 4 : GL20.glUniform4(entry.getKey(), entry.getValue().getValue()); break;
			}
		}
		m_pendingUniformI.clear();
		
		for (Map.Entry<Integer, Map.Entry<Integer, FloatBuffer>> entry : m_pendingUniformM.entrySet())
		{
			switch(entry.getValue().getKey()){
			case 2 : GL20.glUniformMatrix2(entry.getKey(), false, entry.getValue().getValue()); break;
			case 3 : GL20.glUniformMatrix3(entry.getKey(), false, entry.getValue().getValue()); break;
			case 4 : GL20.glUniformMatrix4(entry.getKey(), false, entry.getValue().getValue()); break;
			}
		}
		m_pendingUniformI.clear();
	}
	
	protected void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}
	
	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could not compile shader\n" + GL20.glGetShaderInfoLog(shaderID, 1000));
			System.exit(-1);
		}
		
		this.exitOnGLError("loadShader");
		
		return shaderID;
	}
}
