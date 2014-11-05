import java.nio.IntBuffer;
import java.util.Random;
import java.util.Vector;

import org.lwjgl.util.vector.Vector3f;


public class SPH implements AbstractPass {
	private Eye m_eye;
	private SpaceMap m_spaceMap;
	private ParticleSet m_particleSet;
	private PassUpdate m_passUpdate;
	private PassRender m_passRender;
	;private Vector<Particle> m_toAddParticleVector = new Vector<Particle>();

	private int m_viewportWidth;
	private int m_viewportHeight;
	
	private Timer m_timer = new Timer();
	private long m_deltaT = 0;
	private int m_ite = 0;

	@Override
	public void initialize() {
		m_eye = new Eye();

		m_spaceMap = new SpaceMap();
		m_particleSet = new ParticleSet();

		m_passUpdate = new PassUpdate();
		m_passUpdate.initialize();
		m_passUpdate.setSpaceMap(m_spaceMap);
		m_passUpdate.setParticleSet(m_particleSet);

		m_passRender = new PassRender();
		m_passRender.initialize();
		m_passRender.setEye(m_eye);
		m_passRender.setSpaceMap(m_spaceMap);
		m_passRender.setParticleSet(m_particleSet);

		Random rand = new Random();
		for(int i = 0; i < 1000; i++)
		{
			Particle particle = new Particle();

			//Vector3f position = new Vector3f(16, 16, 16);
			Vector3f position = new Vector3f(
					(float) rand.nextInt(32) + (float) rand.nextInt(1000)/1000,
					(float) rand.nextInt(32) + (float) rand.nextInt(1000)/1000,
					(float) rand.nextInt(32) + (float) rand.nextInt(1000)/1000);
			particle.setPosition(position);
			
			addParticle(particle);
		}
		m_spaceMap.swap();
	}

	@Override
	public void render() 
	{
		//Add the new particles in the set. tell the cube about the new indexes.
		//There is no space discrimination, must be implemented at some point TODO
		IntBuffer particleIndexes = m_particleSet.addParticles(m_toAddParticleVector);
		m_spaceMap.front().addParticleIndexes(particleIndexes);
		m_toAddParticleVector.clear();
		
		//Apply the time difference and render
		m_passUpdate.setDeltaT(m_deltaT);
		m_passUpdate.render();
		m_passRender.render();
		
		//Swap cube buffers.
		m_spaceMap.swap();
		
		m_deltaT = m_timer.elapsed();
		m_timer.start();

		System.out.println(m_deltaT / 1000000);
	}
	
	public Eye eye() 
	{ 
		return m_eye; 
	}
	
	public void addParticle(Particle p_particle)
	{
		m_toAddParticleVector.add(p_particle);
	}
	
	public void setViewportSize(int p_width, int p_height)
	{
		m_viewportWidth = p_width;
		m_viewportHeight = p_height;
		
		m_passRender.setViewportSize(m_viewportWidth, m_viewportHeight);
	}

}
