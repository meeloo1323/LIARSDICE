import java.util.ArrayList;

public class Cup {
	private String name;
	private ArrayList<Die> dice;
	
	public Cup()
	{
		dice = new ArrayList<Die>(5);
	}
	
	public Cup(String name)
	{
		dice = new ArrayList<Die>(5);
		this.name = name;
	}
	
	
	public int getAmt()
	{
		return (dice.size());
	}
	
	public String getName()
	{
		return name;
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
	
	public void show()
	{
		for (Die die : dice)
		{
			System.out.println(die);
		}
	}
	
	// the user inputs the arraylist index of the desired die. the value of that die is returned
	public int getDieValue(int num)
	{ 
		return (dice.get(num)).getNum();
	}
	
}
