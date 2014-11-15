import java.util.Vector;


public class SpaceMap {
	private Cube m_front = new Cube();
	private Cube m_back = new Cube();
	private Vector<Particle> m_toAddParticleVector = new Vector<Particle>();
	
	public void addParticle(Particle p_particle)
	{
		m_toAddParticleVector.add(p_particle);
	}
	
	public Cube front()
	{
		return m_front;
	}
	
	public Cube back()
	{
		return m_back;
	}
	
	public void swap()
	{
		m_front.clear();
		
		Cube temp = m_back;
		m_back = m_front;
		m_front = temp;
		
		m_front.addParticles(m_toAddParticleVector);
		m_toAddParticleVector.clear();
	}
}
