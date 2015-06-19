package Default;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import lemon.program.RenderProgram;

import org.lwjgl.BufferUtils;
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
	private static IntBuffer s_clearBuffer = BufferUtils.createIntBuffer(1);
	private static boolean s_clearBufferSet = false;
	
	private IntBuffer m_buffer = BufferUtils.createIntBuffer(1);
	
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
		IntBuffer amount = BufferUtils.createIntBuffer(1);
		
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
			RenderProgram p_program,
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
		/*p_program.setAttributeBuffer(
				p_attribVelocityName,
				GL11.GL_FLOAT, 
				s_offsetToParticles + 4*4, 
				3, 
				Particle.GLSLSize());*/
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
		IntBuffer amount = BufferUtils.createIntBuffer(1);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m_buffer.get(0));
		GL15.glGetBufferSubData(GL15.GL_ARRAY_BUFFER, s_offsetToCounter, amount);
		
		for(int i = 0; i < p_particleVector.size(); i++)
		{
			ByteBuffer buffer = p_particleVector.get(i).buffer();
			GL15.glBufferSubData(
					GL15.GL_ARRAY_BUFFER, 
					s_offsetToParticles + amount.get(0) * Particle.GLSLSize(), 
					buffer);
			amount.put(0, amount.get(0) + 1);
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
			Byte xff = Byte.decode("-0x01");
			ByteBuffer clearData = BufferUtils.createByteBuffer(s_offsetToParticles);
			for(int i = 0 ; i < s_offsetToParticles; i++)
				clearData.put(i, xff);

			clearData.putInt(s_offsetToCounter + 0, 0); //Actual amount of particle
			clearData.putInt(s_offsetToCounter + 4, 1); //Actual amount of particle
			clearData.putInt(s_offsetToCounter + 8, 1); //Actual amount of particle
			
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
			
			s_clearBufferSet = true;
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
