import java.lang.Math;

public class Die {
	private int number;
	
	public Die()
	{
		number=1;
	}
	
	public void roll()
	{
		number=(int) (Math.random()*6+1);
	}
	
	public int getNum()
	{
		return number;
	}
	
	public String toString() { return Integer.toString(number); }
	
}
