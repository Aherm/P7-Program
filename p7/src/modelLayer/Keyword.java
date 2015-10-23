package modelLayer;

public class Keyword {
	private String name;
	private String regex;
	private int weight;

	public Keyword(String name, String regex, int weight) {
		this.name = name;
		this.regex = regex;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int cost) {
		this.weight = cost;
	}
}