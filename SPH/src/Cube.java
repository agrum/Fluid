import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

public class Cube {
	private static final int s_cubeSize = 32;
	private static final int s_offsetToCounter = 147460;
	private static final int s_offsetToParticles = 147472;
	private static IntBuffer s_clearBuffer = IntBuffer.allocate(1);
	private static boolean s_clearBufferSet = false;
	
	private IntBuffer m_buffer = IntBuffer.allocate(1);
	
	Cube()
	{
		int cubeVolume = s_cubeSize*s_cubeSize*s_cubeSize;
		int nbParticleSupported = cubeVolume;
		int linkedListSize = nbParticleSupported * Particle.GLSLSize();
		
		GL15.glGenBuffers(m_buffer);
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				0, 
				m_buffer.get(0));
		GL15.glBufferData(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				s_offsetToParticles + linkedListSize, 
				GL15.GL_DYNAMIC_DRAW);
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				0, 
				0);
		
		clear();
	}
	
	protected void finalize() {
		GL15.glDeleteBuffers(m_buffer);
	}
	
	public int amountParticles()
	{
		IntBuffer amount = IntBuffer.allocate(1);
		
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				m_buffer.get(0));
		GL15.glGetBufferSubData(
				GL15.GL_ARRAY_BUFFER, 
				s_offsetToCounter, 
				amount);
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);

		return amount.get(0);
	}
	
	public Particle particleAt(int p_index)
	{
		Particle particle = new Particle();
		
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER,
				m_buffer.get(0));
		GL15.glGetBufferSubData(
				GL15.GL_ARRAY_BUFFER, 
				s_offsetToParticles + Particle.GLSLSize() * p_index, 
				particle.buffer());
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
		
		return particle;
	}
	
	public void bindParticleArray(
			ShaderProgram p_program,
			String p_attribPositionName,
			String p_attribVelocityName,
			String p_attribDensityName)
	{
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER,
				m_buffer.get(0));
		
		p_program.setAttributeBuffer(
				p_attribPositionName,
				GL11.GL_FLOAT, 
				s_offsetToParticles + 0*4, 
				3, 
				Particle.GLSLSize());
		p_program.setAttributeBuffer(
				p_attribVelocityName,
				GL11.GL_FLOAT, 
				s_offsetToParticles + 4*4, 
				3, 
				Particle.GLSLSize());
		p_program.setAttributeBuffer(
				p_attribDensityName,
				GL11.GL_FLOAT, 
				s_offsetToParticles + 10*4, 
				1, 
				Particle.GLSLSize());
	}
	
	public void bind(int p_layout)
	{
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				p_layout, 
				m_buffer.get(0));
	}
	
	public int bindDispatchIndirect()
	{
		GL15.glBindBuffer(
				GL43.GL_DISPATCH_INDIRECT_BUFFER, 
				m_buffer.get(0));
		
		return s_offsetToCounter;
	}
	
	public void release(int p_layout)
	{
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				p_layout, 
				0);
	}
	
	public void addParticles(Vector<Particle> p_particleVector)
	{
		IntBuffer amount = IntBuffer.allocate(1);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m_buffer.get(0));
		GL15.glGetBufferSubData(GL15.GL_ARRAY_BUFFER, s_offsetToCounter, amount);
		
		for(int i = 0; i < p_particleVector.size(); i++)
		{
			GL15.glBufferSubData(
					GL15.GL_ARRAY_BUFFER, 
					s_offsetToParticles, 
					p_particleVector.get(i).buffer());
			amount.put(amount.get(0) + 1);
		}
		
		GL15.glBufferSubData(
				GL15.GL_ARRAY_BUFFER, 
				s_offsetToCounter, 
				amount);
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
	}
	
	public void clear()
	{
		if(!s_clearBufferSet)
		{
			Byte xff = Byte.decode("0xff");
			Byte x00 = Byte.decode("0x00");
			Byte x01 = Byte.decode("0x01");
			ByteBuffer clearData = ByteBuffer.allocate(s_offsetToParticles);
			for(int i = 0 ; i < s_offsetToParticles; i++)
				clearData.put(i, xff);

			clearData.put(s_offsetToCounter + 0, x00); //Actual amount of particle
			clearData.put(s_offsetToCounter + 1, x01); //Y compute shader invocation
			clearData.put(s_offsetToCounter + 2, x01); //Z compute shader invocation
			
			GL15.glGenBuffers(s_clearBuffer);
			GL15.glBindBuffer(
					GL21.GL_PIXEL_UNPACK_BUFFER, 
					s_clearBuffer.get(0));
			GL15.glBufferData(
					GL21.GL_PIXEL_UNPACK_BUFFER, 
					clearData, 
					GL15.GL_STATIC_COPY);
			GL15.glBindBuffer(
					GL21.GL_PIXEL_UNPACK_BUFFER, 
					0);
		}
		
		GL15.glBindBuffer(
				GL21.GL_PIXEL_UNPACK_BUFFER, 
				s_clearBuffer.get(0));
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				m_buffer.get(0));
		GL31.glCopyBufferSubData(
				GL21.GL_PIXEL_UNPACK_BUFFER,
				GL15.GL_ARRAY_BUFFER,
				0,
				0,
				s_offsetToParticles);
		GL15.glBindBuffer(
				GL21.GL_PIXEL_UNPACK_BUFFER, 
				0);
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
	}
}
