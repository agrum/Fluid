import java.util.Map;
import java.util.TreeMap;

import org.omg.CORBA.UserException;


public class Lemon {
	private Lemon m_parent = null;
	private Map<String, Integer> m_seedMap = new TreeMap<String, Integer>();
	
	public void setParent(Lemon p_parent) throws Exception
	{
		if(p_parent == null)
			throw new Rock("No parent, unacccceeeptable!!");
		if(p_parent == this)
			throw new Rock("Lemon is itself, unacccceeeptable!!");
		
		m_parent = p_parent;
	}
	
	public void addSeed(
			String p_seed,
			int p_value) throws Exception
	{
		if(p_seed.isEmpty())
			throw new Rock("Lemon:addSeed() => Seed empty");
		if(m_seedMap.containsKey(p_seed))
			throw new Rock("Lemon:addSeed() => Map already contains this seed");
		
		m_seedMap.put(p_seed, p_value);
	}
	
	public Integer getSeed(
			String p_seed) throws Exception
	{
		if(!m_seedMap.containsKey(p_seed))
			throw new Rock("Lemon:getSeed() => Map doesn't contains this seed");
		
		return m_seedMap.get(p_seed);
	}
}
