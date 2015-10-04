package modelLayer;

public class Keyword {
	private String name;
	private int weight;
	
	public Keyword(String name, int weight)
	{
		this.name = name;
		this.weight = weight;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setWeight(int cost)
	{
		this.weight = cost;
	}
}
