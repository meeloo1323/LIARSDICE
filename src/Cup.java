import java.util.ArrayList;

public class Cup {
	private String name;
	private ArrayList<Die> dice;
	
	public Cup(String name)
	{
		dice = new ArrayList<Die>(5);
		this.name = name;
	}
	
	public void roll()
	{
		for (Die die : dice)
		{
			die.roll();
		}
	}
	
	public void removeDie()
	{
		dice.remove(0);
	}
}
