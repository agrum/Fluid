
public class Timer {
	private boolean m_on = false;
	private long m_start = 0;
	private long m_stop = 0;
	
	public void start()
	{
		m_on = true;
		m_start = System.nanoTime();
		m_stop = System.nanoTime();
	}

	public long elapsed()
	{
		long last;
		
		if(m_on)
			last = System.nanoTime();
		else
			last = m_stop;
		
		return last - m_start;
	}

	public void stop()
	{
		if(m_on)
		{
			m_on = false;
			m_stop = System.nanoTime();
		}
	}
}
