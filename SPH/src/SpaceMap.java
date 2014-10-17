import java.util.Vector;


public class SpaceMap {
	private Cube m_front = new Cube();
	private Cube m_back = new Cube();
	private Vector<Particle> m_toAddParticleVector;
	
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
		
	}
}
