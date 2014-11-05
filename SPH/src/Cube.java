import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

public class Cube {
	private static final int s_cubeSize = 32;
	private static final int s_offsetToCounter = 147460;
	private static final int s_offsetToParticles = 147472;
	private static final int s_bufferSize = s_offsetToParticles + s_cubeSize*s_cubeSize*s_cubeSize*Particle.GLSLSize();
	private static IntBuffer s_clearBuffer = BufferUtils.createIntBuffer(1);
	private static boolean s_clearBufferSet = false;
	
	private IntBuffer m_buffer = BufferUtils.createIntBuffer(1);
	
	Cube()
	{
		GL15.glGenBuffers(m_buffer);
		GL30.glBindBufferBase(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				0, 
				m_buffer.get(0));
		GL15.glBufferData(
				GL43.GL_SHADER_STORAGE_BUFFER, 
				s_bufferSize, 
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
		IntBuffer amount = BufferUtils.createIntBuffer(3);
		
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
		
		/*//Check all invocation group values
		int x = amount.get(0);
		int y = amount.get(1);
		int z = amount.get(2);*/
		return amount.get(0);
	}
	
	public int particleIndexAt(int p_index)
	{
		IntBuffer particleIndex = BufferUtils.createIntBuffer(1);
		
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER,
				m_buffer.get(0));
		GL15.glGetBufferSubData(
				GL15.GL_ARRAY_BUFFER, 
				s_offsetToParticles + p_index*4, 
				particleIndex);
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
		
		return particleIndex.get(0);
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
	
	public void addParticleIndexes(IntBuffer p_particleIndexBuffer)
	{
		IntBuffer particleListBuffer = BufferUtils.createIntBuffer(p_particleIndexBuffer.limit()*4);
		IntBuffer amount = BufferUtils.createIntBuffer(1);
		
		for(int i = 0; i < p_particleIndexBuffer.limit(); i++){
			particleListBuffer.put(p_particleIndexBuffer.get());
			particleListBuffer.put(-1);
			particleListBuffer.put(-1);
			particleListBuffer.put(-1);
		}
		particleListBuffer.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m_buffer.get(0));
		GL15.glGetBufferSubData(GL15.GL_ARRAY_BUFFER, s_offsetToCounter, amount);
		
		GL15.glBufferSubData(
				GL15.GL_ARRAY_BUFFER, 
				s_offsetToParticles + amount.get(0) * 16, 
				particleListBuffer);
		amount.put(0, amount.get(0) + p_particleIndexBuffer.limit());
		
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
			ByteBuffer clearData = BufferUtils.createByteBuffer(s_bufferSize);
			for(int i = 0 ; i < s_bufferSize; i++)
				clearData.put(i, xff);

			clearData.putInt(s_offsetToCounter + 0, 0); //Actual amount of particle
			clearData.putInt(s_offsetToCounter + 4, 1); //Work group Y amount of invocations
			clearData.putInt(s_offsetToCounter + 8, 1); //Work group Z amount of invocations
			
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
				s_bufferSize);
		GL15.glBindBuffer(
				GL21.GL_PIXEL_UNPACK_BUFFER, 
				0);
		GL15.glBindBuffer(
				GL15.GL_ARRAY_BUFFER, 
				0);
	}
}
