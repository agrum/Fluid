package Default;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;


public class PassUpdate implements AbstractPass {
	private ComputeProgram m_program = null;
	private int m_exploreLimit = 5;

	private SpaceMap m_spaceMap = null;
	private float m_deltaT = 0;
	
	public void setSpaceMap(SpaceMap p_spaceMap)
	{
		m_spaceMap = p_spaceMap;
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
		Cube front = m_spaceMap.front();
		Cube back = m_spaceMap.back();

		if(m_exploreLimit > 4 && m_deltaT > 40f)
			m_exploreLimit--;
		else if(m_exploreLimit < 20 && m_deltaT < 20f)
			m_exploreLimit++;
		
		m_program.bind();

		m_program.setUniform("uDeltaT", m_deltaT);
		m_program.setUniform("uExploreLimit", m_exploreLimit);
			
		//Bind
		front.bind(0);
		int offset = front.bindDispatchIndirect();
		back.bind(1);
		
		//Dispatch
		GL43.glDispatchComputeIndirect(offset);

		//Barrier
		GL42.glMemoryBarrier(GL42.GL_ALL_BARRIER_BITS);

		//Release
		front.release(0);
		back.release(1);
		
		m_program.release();
	}

}
