import java.util.Random;

import org.lwjgl.util.vector.Vector3f;


public class SPH implements AbstractPass {
	private Eye m_eye;
	private SpaceMap m_spaceMap;
	//private PassUpdate m_passUpdate;
	private PassRender m_passRender;

	private int m_viewportWidth;
	private int m_viewportHeight;
	//private long m_deltaT;

	@Override
	public void initialize() {
		m_eye = new Eye();
		m_eye.move(16, 16, 16);
		m_eye.lookLeft(-2);
		m_eye.move(-40, 0, 0);
		m_eye.setFarVisibility(1);
		m_eye.setFarVisibility(40);
		
		m_spaceMap = new SpaceMap();

		m_passRender = new PassRender();
		m_passRender.initialize();
		m_passRender.setEye(m_eye);
		m_passRender.setSpaceMap(m_spaceMap);

		Random rand = new Random();
		for(int i = 0; i < 10; i++)
		{
			Particle particle = new Particle();
			particle.setRadius(1);
			particle.setMass(1); 
			particle.setVelocity(new Vector3f(0, 0, 0));
			
			Vector3f position = new Vector3f(
				(float) rand.nextInt(32) - 16f + (float) rand.nextInt(1000)/1000,
				(float) rand.nextInt(32) - 16f + (float) rand.nextInt(1000)/1000,
				(float) rand.nextInt(32) - 16f + (float) rand.nextInt(1000)/1000);
			particle.setPosition(position);
			addParticle(particle);
		}
		m_spaceMap.swap();
	}

	@Override
	public void render() 
	{
		Timer timer = new Timer();
		timer.start();
		
		m_passRender.render();
		
		//m_deltaT = timer.elapsed();
	}
	
	public Eye eye() 
	{ 
		return m_eye; 
	}
	public void addParticle(Particle p_particle)
	{
		m_spaceMap.addParticle(p_particle);
	}
	public void setViewportSize(int p_width, int p_height)
	{
		m_viewportWidth = p_width;
		m_viewportHeight = p_height;
		
		m_passRender.setViewportSize(m_viewportWidth, m_viewportHeight);
	}

}
