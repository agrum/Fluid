import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;


public class ParticleSet {	
	private IntBuffer m_buffer = BufferUtils.createIntBuffer(1);
	
	ParticleSet()
	{
		int nbParticleSupported = 100000;
		int linkedListSize = nbParticleSupported * Particle.GLSLSize();
		
		GL15.glGenBuffers(m_buffer);
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				0, 
				m_buffer.get(0));
		GL15.glBufferData(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				linkedListSize, 
				GL15.GL_DYNAMIC_DRAW);
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				0, 
				0);
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
				0, 
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
				Particle.GLSLSize() * p_index, 
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
				0*4, 
				3, 
				Particle.GLSLSize());
		/*p_program.setAttributeBuffer(
				p_attribVelocityName,
				GL11.GL_FLOAT, 
				4*4, 
				3, 
				Particle.GLSLSize());*/
		p_program.setAttributeBuffer(
				p_attribDensityName,
				GL11.GL_FLOAT, 
				10*4, 
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
	
	public void release(int p_layout)
	{
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				p_layout, 
				0);
	}
	
	public IntBuffer addParticles(Vector<Particle> p_particleVector)
	{
		IntBuffer indexesCreated = BufferUtils.createIntBuffer(p_particleVector.size());
		IntBuffer amount = BufferUtils.createIntBuffer(1);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m_buffer.get(0));
		GL15.glGetBufferSubData(GL15.GL_ARRAY_BUFFER, 0, amount);
		
		for(int i = 0; i < p_particleVector.size(); i++)
		{
			ByteBuffer buffer = p_particleVector.get(i).buffer();
			GL15.glBufferSubData(
					GL15.GL_ARRAY_BUFFER, 
					amount.get(0) * Particle.GLSLSize(), 
					buffer);
			amount.put(0, amount.get(0) + 1);
			indexesCreated.put(amount.get(0));
		}
		indexesCreated.flip();
		
		GL15.glBufferSubData(
				GL15.GL_ARRAY_BUFFER, 
				0, 
				amount);
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
		
		return indexesCreated;
	}
}
