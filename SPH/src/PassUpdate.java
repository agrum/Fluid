import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.glu.GLU;


public class PassUpdate implements AbstractPass {
	private ComputeProgram m_program = null;
	private int m_exploreLimit = 5;

	private SpaceMap m_spaceMap = null;
	private ParticleSet m_particleSet = null;
	private float m_deltaT = 0;
	
	public void setSpaceMap(SpaceMap p_spaceMap)
	{
		m_spaceMap = p_spaceMap;
	}
	
	public void setParticleSet(ParticleSet p_particleSet)
	{
		m_particleSet = p_particleSet;
	}
	
	public void setDeltaT(long deltaT)
	{
		m_deltaT = (float) (deltaT / 1000) / 1000f;
	}
	
	@Override
	public void initialize() {
		//Init shader program
		m_program = new ComputeProgram("resource/sphUpdateEasy.comp");

		m_exploreLimit = 15;
	}

	@Override
	public void render() {
		exitOnGLError("PassUpdate::render()_0");
		Cube front = m_spaceMap.front();
		Cube back = m_spaceMap.back();

		if(m_exploreLimit > 4 && m_deltaT > 40f)
			m_exploreLimit--;
		else if(m_exploreLimit < 20 && m_deltaT < 20f)
			m_exploreLimit++;

		exitOnGLError("PassUpdate::render()_0.01");
		m_program.bind();
		exitOnGLError("PassUpdate::render()_0.1");

		m_program.setUniform("uDeltaT", m_deltaT);
		m_program.setUniform("uExploreLimit", m_exploreLimit);
		exitOnGLError("PassUpdate::render()_1");
		
		//Bind
		front.bind(0);
		back.bind(1);
		m_particleSet.bind(2);
		
		//Dispatch
		int offset = front.bindDispatchIndirect();
		GL43.glDispatchComputeIndirect(offset);
		//GL43.glDispatchCompute(1000, 1, 1);
		
		back.amountParticles();

		//Barrier
		GL42.glMemoryBarrier(GL42.GL_ALL_BARRIER_BITS);
		exitOnGLError("PassUpdate::render()_2");

		//Release
		front.release(0);
		back.release(1);
		m_particleSet.release(2);
		
		m_program.release();
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
