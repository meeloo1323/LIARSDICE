import java.util.Scanner;

public class DiceGame {
	public static void main(String[] args) {
		
		Scanner console= new Scanner(System.in);
		System.out.println("Welcome to Liar's Dice! Do you know how to play?");
		if (console.nextLine().equalsIgnoreCase("no")) {
			System.out.println("Rules");
		}
		
		System.out.println("What is your name?");
		Cup player = new Cup(console.nextLine());
		
		console.close();
	}
}
