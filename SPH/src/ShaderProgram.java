import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.glu.GLU;


public class ShaderProgram {
	// Shader variables
	private int m_id = 0;
	
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
	
	public ShaderProgram() {
		m_id = GL20.glCreateProgram();
	}
	
	protected void finalize() {
		GL20.glDeleteProgram(m_id);
	}
	
	public int id() {
		return m_id;
	}
	
	public void addShaderFromFile(
			ShaderType shaderType,
			String filePath) {
		int vsId = this.loadShader(filePath, shaderType.m);
		
		GL20.glAttachShader(m_id, vsId);
	}
	
	public void link() {
		GL20.glLinkProgram(m_id);
		GL20.glValidateProgram(m_id);

		this.exitOnGLError("setupShaders");
	}
	
	public void bind() {
		GL20.glUseProgram(m_id);
	}
	
	public void release() {
		GL20.glUseProgram(0);
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
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		
		this.exitOnGLError("loadShader");
		
		return shaderID;
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
