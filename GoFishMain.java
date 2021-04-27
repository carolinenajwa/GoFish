// Gavin Stuart and Caroline El Jazmi

// CS145 - 4/27/2021 

// Lab 4 - Card Game

// This program outputs a card game called Go Fish!
// It uses a deck of 52 cards made up of 4 suits and 13 ranks. 
// In this game, you will go first by default.
// Rules are as follows, each player ( You vs. Computer) will be delt 7 cards each.
// This cards will be removed from a shuffled deck. Both players will take turns asking
// the other for a card that the player already has in its hand. When requested, if the 
// other player does own that card(s) They must hand over every matching card in their
// hand of that particular card. After receiving the card(s). The original player can 
// then ask for another card. If the other player does not have the requested card, 
// they will respond "Go Fish!", which means the original player must pick a new card 
// from the deck. If either players do not have any cards in their hand, they are not 
// allowed to request for a card and must pick a new card from the shuffled deck. When
// all suits of card have been found, the game ends. Winner is the player with the most suits.

//import tools
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

public class GoFishMain {

	// TODO: no class constants
	private static final int STARTING_HAND_SIZE = 7;

	// main
	public static void main(String[] args) {

		// create a new deck of cards
		ArrayList<Integer> deck = newDeck();

		// grab some input
		Scanner input = new Scanner(System.in);

		// suffle the deck
		Collections.shuffle(deck);

		// play a game
		playOneGame(deck, input);
	}

	// Plays one game.
	public static void playOneGame(ArrayList<Integer> deck, Scanner input) {

		// Emptry array of cards in the hand
		ArrayList<Integer> computer = new ArrayList<Integer>();
		ArrayList<Integer> human = new ArrayList<Integer>();

		// Empty array of cards in the pile
		ArrayList<Integer> computerPile = new ArrayList<Integer>();
		ArrayList<Integer> humanPile = new ArrayList<Integer>();

		// Deals cards to user and computer
		dealHands(deck, human, computer);

		// Displays card-hand to user
		showGame(human, computerPile, humanPile);

		Random randomNumber = new Random();

		// define who is playing
		String opponent = null;

		// while the deck is not empty and we don't have more than 52 cards
		// keep the game looping between players
		while (computerPile.size() + humanPile.size() <= 51 && deck.size() > 0) {

			// User plays first
			if (!human.isEmpty() && deck.size() > 0) {
				// Requests card from opponent
				System.out.println("What card will you ask for? (Enter card number)");

				opponent = "Computer";

				int card;

				try {
					card = input.nextInt();
					if (card < 1 || card > 13) {
						throw new Exception();
					}
				} catch (Exception e) {
					do {
						System.out.println("Please enter a number from 1 - 13!");
						card = input.nextInt();
					} while (card < 1 || card > 13);
				}

				// Plays one turn
				playTurn(card, human, computer, humanPile, computerPile, deck, opponent);

			} else {
				int cardFromPile = randomNumber.nextInt(deck.size());
				human.add(deck.get(cardFromPile));
				deck.remove(cardFromPile);
			}

			System.out.printf("There are %d cards left in the deck.\n", deck.size());

			// Computer's turn
			if (!computer.isEmpty() && deck.size() > 0) {

				opponent = "User";
				int card = computer.get((int) (Math.random() * computer.size()));
				System.out.println(deck.size());
				// print the question to the user using printf
				System.out.printf("Do you have any %d's?\n", card);

				// Plays one turn
				playTurn(card, computer, human, computerPile, humanPile, deck, opponent);

			} else if (!deck.isEmpty()) {

				// TODO: Let the computer draw from the deck
				int cardFromPile = randomNumber.nextInt(deck.size());
				computer.add(deck.get(cardFromPile));
				deck.remove(cardFromPile);
			}

			System.out.printf("There are %d cards left in the deck.\n", deck.size());

			showGame(human, computerPile, humanPile);
		}

		// Displays winner
		if (humanPile.size() > computerPile.size()) {
			System.out.printf("You win with %d book(s)!", humanPile.size());
		}
		if (humanPile.size() < computerPile.size()) {
			System.out.printf("You lost to the computer, who has %d book(s)...", computerPile.size());
		}
		if (humanPile.size() == computerPile.size()) {
			System.out.printf("Its a tie! You each have %d book(s)!", computerPile.size());
		}
	}

	// Displays human's hand and completed books
	public static void showGame(ArrayList<Integer> human, ArrayList<Integer> computerPile,
			ArrayList<Integer> humanPile) {

		System.out.println("Here are your cards:");
		showCards(human);

		System.out.println("Here is your pile:");
		showCards(humanPile);

		System.out.println("Here is the computer's pile:");
		showCards(computerPile);
	}

	// Alternates turns. Requester = Player requesting card(S) and Giver = Player
	// who is being asked a card(s)
	public static void playTurn(int card, ArrayList<Integer> requester, ArrayList<Integer> giver,
			ArrayList<Integer> requesterPile, ArrayList<Integer> giverPile, ArrayList<Integer> deck, String sendingPlayer) {

		// if the giver has the card
		if (giver.contains(card)) {
			// Transfers card(s) from giver to requester
			transferCards(card, requester, giver);

			// Removes suit of 4 from hand and sets it asside
			for (int x = 0; x < requester.size(); x++) {
				int books = 0;

				if (card == requester.get(x)) {
					books = books + 1;
				}

				if (books == 4) {
					requesterPile.add(card);
					for (int r = 0; r < requester.size() + 1; r++) {
						if (requester.get(r) == card) {
							requester.remove(r);
						}
					}
				}

				books = 0;
			}

			// giver does not contain the card
		} else {

			// alert the user
			System.out.printf("%s says Go fish!\n", sendingPlayer);

			// Draws card from shuffled deck
			Random randomNumber = new Random();

			// get a card location to pull from the pile
			int cardFromPile = randomNumber.nextInt(deck.size());
			requester.add(deck.get(cardFromPile));
			deck.remove(cardFromPile);

			// TODO: If there is a set of four matching cards, put them on the table
			int books = 0;

			for (int x = 0; x < requester.size(); x++) {
				if (card == requester.get(x)) {
					books++;
				}
			}

			if (books == 4) {
				switch (sendingPlayer) {
					case "User":
						System.out.println("Computer just got another deck!");
						break;
					default:
						System.out.println("User just got another deck");
						break;
					}
				

				requesterPile.add(card);
				for (int r = 0; r < requester.size(); r++) {
					if (requester.get(r) == card) {
						requester.remove(r);
					}
				}
			}

			books = 0;

		}
	}

	// Transfers card(s) from one player to another.
	public static void transferCards(int card, ArrayList<Integer> destination, ArrayList<Integer> source) {
		while (source.contains(card)) {
			destination.add(card);
			source.remove(new Integer(card)); // this is that tricky thing from the handout
		}
	}

	// Deals even shuffled cards from deck to each player
	public static void dealHands(ArrayList<Integer> deck, ArrayList<Integer> hand1, ArrayList<Integer> hand2) {

		// generate a random number
		Random randomNumber = new Random();

		// keep track of iterations
		int count = 0;

		while (count <= STARTING_HAND_SIZE) {
			int randomIndex = randomNumber.nextInt(deck.size());
			hand1.add(deck.get(randomIndex));
			deck.remove(deck.get(randomIndex));
			count++;
		}

		count = 0;

		while (count <= STARTING_HAND_SIZE) {
			int randomIndex = randomNumber.nextInt(deck.size());
			hand2.add(deck.get(randomIndex));
			deck.remove(deck.get(randomIndex));
			count++;
		}

	}

	// Creates deck of 52 cards, 4 of each rank numbered 1 - 13.
	public static ArrayList<Integer> newDeck() {

		ArrayList<Integer> createDeck = new ArrayList<Integer>();
		int i = 0;
		while (i <= 51) {
			int addRankings = i % 13 + 1;
			createDeck.add(addRankings);
			i++;
		}

		// return the final ArrayList<INT> deck
		return createDeck;
	}

	// Displays and sorts cards
	public static void showCards(ArrayList<Integer> cards) {

		// sort cards by size
		Collections.sort(cards);

		// print out cards
		if (!cards.isEmpty()) {
			for (Integer i : cards) {
				switch (i) {
				case 11:
					System.out.print("Jack ");
					break;
				case 12:
					System.out.print("Queen ");
					break;
				case 13:
					System.out.print("King ");
					break;
				default:
					System.out.printf("%d ", i);
					break;
				}
			}
		} else {
			// don't have any cards?
			System.out.print("No Cards");
		}

		// jump to a new line
		System.out.println();
	}

}
