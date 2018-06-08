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
	
	public void main(String[] args) 
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
		
		player.roll();
		player.show();
		
		Cup computer = new Cup();
		computer.roll();
		int i=0;
		int amt=0;
		int value=0;
		
		// basic game loop
		while(player.getAmt()>0 && computer.getAmt()>0) 
		{
			// the overarching if-else and int i here is supposed to alternate which player's turn it is (even for human player, odd for comp)
			// this allows human player to call when it's their turn
			if(i%2==0) 
			{
				System.out.println(player.getName()+", what is your next call? Enter it in the form 'amt, value.'");
				String[] call = console.nextLine().split(",");
				amt = Integer.parseInt(call[0]);
				value = Integer.parseInt(call[1]);
				// 20% of the time, the computer will challenge your call
				if (Math.random()<0.2)
				{
					System.out.println("Your call has been challenged!");
					if(checkCall(amt, value, player, computer))
					{
						computer.removeDie();
						System.out.println("Nice! One die deducted from computer player- it now has "+computer.getAmt()+" dice left.");
					}
					else
					{ 
						player.removeDie();
						System.out.println("Sorry, you were wrong. One die deducted- you now have "+player.getAmt()+" dice left.");
					}
				}
			}
			
			
			// this has the computer make a call for it's turn
			else 
			{
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
					}
					else
					{ 
						computer.removeDie();
						System.out.println("Nice! One die deducted from computer player- it now has "+computer.getAmt()+" dice left.");
					}
				}
				
			}
			i++;
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
	public boolean checkCall (int amt, int value, Cup player, Cup computer) {
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
}
