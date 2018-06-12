import java.util.Scanner;

/* Some ideas for Sung Joon (stuff that I think could be nice but idk how to do it):
 * - Add a way to switch players turns based off of who lost the last round (instead of simple alternating)
 * - Catching errors and displaying messages (can't go down amt or value)
 * - Allow user to ask to display total amount of dice whenever they want to
 * - Allow the computer to increment by more than 1 randomly
 * - Make a hard mode where the computer will use probability to call you out (instead of randomly)
 * - Wild card mode where 1 counts as everything
 * 
 * Most importantly, I didn't do this betting option, which is necessary for the game
 * 		"Bet a higher quantity of lower face than the person before them."
 */

public class DiceGame {
	
	public static void main(String[] args) 
	{
		
		Scanner console= new Scanner(System.in);
		System.out.println("Welcome to Liar's Dice! Do you know how to play?");
		if (console.nextLine().equalsIgnoreCase("no")) 
		{
			System.out.println("Liar's Dice Rules:\r\n" + 
					" \r\n" + 
					"The game starts off with 5 dice per person who roll the dice randomly.\r\n" + 
					" \r\n" + 
					"Each person bets a certain quantity of a certain dice's face. (ex: there are at least 3 dice of face 2). They can:\r\n" + 
					" \r\n" + 
					"Bet a higher quantity of lower face than the person before them.\r\n" + 
					"OR\r\n" + 
					"Bet the same quantity of higher face than the person before them.\r\n" + 
					"OR \r\n" + 
					"Bet a higher quantity of higher face than the person before them. \r\n" + 
					" \r\n" + 
					"If a lower quantity of lower face or same quantity of lower face is entered, program asks you to \"try again\".\r\n" + 
					" \r\n" + 
					"At any time, if a certain quantity of a face seems unreasonable, you can challenge the call and a die will be taken from either the person calling \"liar\" or the person previously placing the bet, according to the result. The game ends when the losing player runs out of dice.\r\n" + 
					"");
		}
		
		System.out.println("What is your name?");
		Cup player = new Cup(console.nextLine());
		Cup computer = new Cup();
		
		int amt=0;
		int value=0;
		int called = 1; //1: player was right, 2: computer was right, 0: nothing was called and it's the computer's turn, 3: nothing was called and it's the player's turn
		
		// basic game loop
		while(player.getAmt()>0 && computer.getAmt()>0) 
		{
			// the overarching if-else and int i here is supposed to alternate which player's turn it is (even for human player, odd for comp)
			// this allows human player to call when it's their turn
			if(called%2==1) 
			{
				if (called!=3)
				{
					player.roll();
					computer.roll();
					amt=0;
					value=0;
				}
				player.show();
				System.out.println(player.getName()+", what is your next call? Enter it in the form 'amt, value.'");
				
				String input;
				String call[];
				while (true)
				{
					input = console.nextLine();
					if (!validInt(input)) 
						System.out.println("Not a valid call!");
					else if (!validBet(amt,value,Integer.parseInt(input.split(", ")[0]), Integer.parseInt(input.split(", ")[1])))
					{
						System.out.println("Not a valid call! Your call has to have a higher value or amount");
					}
					else break;
				}
				call = input.split(", ");
				amt = Integer.parseInt(call[0]);
				value = Integer.parseInt(call[1]);

				//Using a rough probability finder, call out a player
				if (probability(player, computer, amt, value)<Math.random() && probability(player, computer, amt, value)<0.1)
				{
					System.out.println("Your call has been challenged!");
					if(checkCall(amt, value, player, computer))
					{
						computer.removeDie();
						System.out.println("Nice! One die deducted from computer player- it now has "+computer.getAmt()+" dice left.");
						called = 1;
					}
					else
					{ 
						player.removeDie();
						System.out.println("Sorry, you were wrong. One die deducted- you now have "+player.getAmt()+" dice left.");
						called = 2;
					}
				}
				else
					called = 0;
			}
			
			
			// this has the computer make a call for it's turn
			else 
			{
				if (called!=0)
				{
					player.roll();
					computer.roll();
					player.show();
					amt=0;
					value=0;
				}
				if (value==6)
				{
					amt++;
				}
				else if(value==0 && amt==0)
				{
					amt=1;
					value=1;
				}
				else
				{
					double call = Math.random();
					if (call<0.5)
					{
						amt++;
					}
					else
					{
						value++;
					}
				}
				// the call is displayed, and the human player can challenge
				System.out.println("The computer's call is: "+amt+" dice of value "+value+". Would you like to challenge? Type 'yes' or 'no'.");
				if(console.nextLine().equalsIgnoreCase("yes"))
				{
					if(checkCall(amt, value, player, computer))
					{
						player.removeDie();
						System.out.println("Sorry, you were wrong. One die deducted- you now have "+player.getAmt()+" dice left.");
						called = 2;
					}
					else
					{ 
						computer.removeDie();
						System.out.println("Nice! One die deducted from computer player- it now has "+computer.getAmt()+" dice left.");
						called = 1;
					}
				}
				else
					called = 3;
			}
		}
		
		if (player.getAmt()==0) {
			System.out.println("Sorry, you lost this game.");
		}
		else
		{
			System.out.println("You won! Good job.");
		}
		console.close();
	}
	
	// if the current call is challenged, this checks the call's validity- true for valid call, false for bluff
	public static boolean checkCall (int amt, int value, Cup player, Cup computer) {
		int actual=0;
		for (int j=0; j<player.getAmt(); j++)
		{
			if (player.getDieValue(j)==value)
			{
				actual++;
			}
		}
		for (int j=0; j<computer.getAmt(); j++)
		{
			if (computer.getDieValue(j)==value)
			{
				actual++;
			}
		}
		System.out.println("There were "+actual+" dice of value "+value+".");
		if (actual>=amt)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean validBet (int preAmt, int preValue, int amt, int value)
	{
		if (amt>preAmt) return true;
		else if (amt==preAmt && value>preValue) return true;
		else return false;
	}
	
	public static boolean validInt(String input)
	{
		try
		{
			String[] call = input.split(", ");
			Integer.parseInt(call[0]);
			Integer.parseInt(call[1]);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static double probability(Cup player, Cup computer, int amt, int value)
	{
		double temp = 0;
		int amtPossess = 0;
		for (Die die : computer.dice)
			if (die.getNum()==value)
				amtPossess++;
		if (amtPossess>=amt)
			temp = 1.0;
		else if (amt-amtPossess>player.dice.size())
			return 0;
		else
		{
			int tempAmt = amt-amtPossess;
			temp = Math.pow(1/6.,tempAmt)*player.dice.size();
		}
		
		return temp;
	}
}
