package Blackjack;

import static Blackjack.Card.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;

public class MethodManager 
{
    public static final int WIN_LIMIT = 21;
    // shuffle deck of Cards with one-pass algorithm
    public static DeckOfCards shuffle(DeckOfCards ourDeck)
    {
        DeckOfCards nextDeck = new DeckOfCards();
        nextDeck.setDeck(ourDeck.getDeck());
        nextDeck.setCurrentCard(0);
        Card[] deck = nextDeck.getDeck();
        SecureRandom randomNumbers = new SecureRandom();

        // for each Card, pick another random Card (0-51) and swap them
        for(int first = 0; first < deck.length; first++)
        {
            // selects random number
            int second = randomNumbers.nextInt(nextDeck.getDeckSize());

            // swaps current card with random card
            Card temp = deck[first];
            deck[first] = deck[second];
            deck[second] = temp;
        }

        return nextDeck;
    }

    // converts a deck into a stack
    public static Stack<Card> deckToStack(Card[] inputDeck, int limit)
    {
        Stack<Card> deck = new Stack<>();

        for(int i = 0; i < inputDeck.length - limit; i++)
        {
            deck.push(inputDeck[inputDeck.length - 1 - i]);
        }

        return deck;
    }

    // game menu method
    public static void menu(Scanner s, Stack<Card> deck, ArrayList<Card> hand)
    {
        boolean start = true;

        String selection = "";

        int balance = 2000; 

        int games = 0;

        while (start == true)
        {
            hand = new ArrayList<Card>();

            if (balance > 0)
            {
                menuIntro();
                selection = s.next();
            }
            else 
            {
                selection = "q";
                System.out.println();
                System.out.println("GAME OVER");
            }

            if (selection.toLowerCase().charAt(0) == 'p')
            {
                balance = playGame(s, balance, deck, hand);
                games++;
            }
            else if (selection.toLowerCase().charAt(0) == 'q')
            {
                System.out.println("Your final balance: " + "$" + balance);
                System.out.println("Your total games: " + games);
                start = false;
            }
        }
    }

    // intro text for menu
    private static void menuIntro()
    {
        System.out.printf("%s%n%s%n%s%n%s", 
    "Welcome to Blackjack!",
            "What would you like to do?",
            "Play a round (p) ",
            "Quit game (q) ");
        System.out.println();
    }

    // defunct code
    public static DeckOfCards initializeDeck()
    {
        DeckOfCards deck = new DeckOfCards();
        deck = shuffle(deck);

        return deck;
    }

    // Puts card in hand
    public static ArrayList<Card> startingHand(ArrayList<Card> hand, Stack<Card> deck)
    {
        hand.add(deck.pop());
        hand.add(deck.pop());
        return hand;
    }

    // main game method
    public static int playGame(Scanner s, int balance, Stack<Card> deck, ArrayList<Card> hand)
    {
        boolean start = true;
        boolean isBlackjack = false;

        String selection = "";

        int bet = placeBet(s, balance);

        int whoWon = -2;

        ArrayList<Card> dealerHand = new ArrayList<Card>();

        hand = startingHand(hand, deck);

        dealerHand = startingHand(dealerHand, deck);

        while (start == true)
        {
            viewHand(hand);
            playGameIntro();
            
            if (blackjackChecker(hand))
            {
                selection = "f";
                isBlackjack = true;
            }
            else
            {
                selection = s.next();
            }

            if (selection.toLowerCase().charAt(0) == 'd')
            {
                hand.add(deck.pop());
            }
            else if (selection.toLowerCase().charAt(0) == 'f')
            {
                whoWon = results(s, deck, hand, dealerHand, isBlackjack);
                balance = adjustBalance(whoWon, balance, bet);
                start = false;
            }
        }

        return balance;
    }

    // produces new balance after game
    private static int adjustBalance(int whoWon, int balance, int bet)
    {
        int newBalance = balance;

        if (whoWon == 1)
        {
            newBalance += bet;
        }
        if (whoWon == -1)
        {
            newBalance -= bet;
        }

        return newBalance;
    }

    // checks to see if you have a blackjack
    private static boolean blackjackChecker(ArrayList<Card> hand)
    {
        boolean firstCheck = false;
        boolean secondCheck = false;

        if (hand.size() == 2)
        {
            for (Card card : hand)
            {
                if (getFace(card) == Face.Ace)
                {
                    firstCheck = true;
                }
                if (getFace(card) == Face.Ten
                || getFace(card) == Face.Jack
                || getFace(card) == Face.Queen
                || getFace(card) == Face.King)
                {
                    secondCheck = true;
                }
            }
        }

        if (firstCheck && secondCheck == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // intro text for main game
    private static void playGameIntro()
    {
        System.out.printf(
        "%n%s%n%s%n%s", 
    "Your move:", 
            "Hit (d) ", 
            "Finalize hand (f) ");
        System.out.println();
    }

    // allows player to bet
    private static int placeBet(Scanner s, int balance)
    {
        boolean validInput = false;

        int bet = 0;

        System.out.println();
        System.out.println("Your balance is " + "$" + balance);
        System.out.println("Place your bet for this round: ");

        while (validInput == false)
        {
            bet = getBetValue(s);

            if (bet <= balance)
            {
                validInput = true;
            }
            else
            {
                System.out.println("Your bet must be less than your balance.");
            }
        }

        return bet;
    }

    // prompts user for bet value
    private static int getBetValue(Scanner s)
    {
        boolean check = false; 
        int bet = 0;

        while (check ==  false)
        {
            try
            {
                s = new Scanner(System.in);
                bet = s.nextInt();
                check = true;
            }
            catch (Exception NumberFormatException)
            {
                System.out.println("Please input a valid integer.");
            }
        }

        return bet;
    }

    // shows player their hand
    private static void viewHand(ArrayList<Card> hand)
    {
        System.out.println();
        System.out.println("Your hand: ");
        for(int i = 0; i < hand.size(); i++)
        {
            System.out.println(hand.get(i) + " (" + getCardValue(hand.get(i)) + ")");
        }
        System.out.println(cardTotal(hand));
    }

    // returns the value of a single card as a string
    private static String getCardValue(Card card)
    {
        int value = 0; 
        Face[] faces = 
            {Face.Ace, Face.Deuce, Face.Three, 
            Face.Four, Face.Five, Face.Six, 
            Face.Seven, Face.Eight, Face.Nine, 
            Face.Ten, Face.Jack, Face.Queen, 
            Face.King };

        Arrays.sort(faces);
        
        value = Arrays.binarySearch(
            faces, 0, faces.length, Card.getFace(card));

        if (value > 9)
        {
            return "10"; 
        }
        else if (value > 0)
        {
            return "" + (value + 1);
        }
        else
        {
            return "1 or 11";
        }
    }

    // different win/loss permutations
    private static int results(
        Scanner s, Stack<Card> deck, ArrayList<Card> hand, 
        ArrayList<Card> dealerHand, boolean isBlackjack)
    {
        int playerTotal = 0;
        int dealerTotal = 0;
        int whoWon = -2;

        playerTotal = finalizeHand(s, hand, isBlackjack);
        dealerTotal = finalizeDealerHand(deck, dealerHand);

        if(playerTotal <= WIN_LIMIT)
        {
            if (playerTotal < dealerTotal && dealerTotal <= WIN_LIMIT)
            {
                whoWon = resultsText(playerTotal, dealerTotal, -1);
            }
            if (playerTotal < dealerTotal && dealerTotal > WIN_LIMIT)
            {
                whoWon = resultsText(playerTotal, dealerTotal, 1);
            }
            if (playerTotal > dealerTotal)
            {
                whoWon = resultsText(playerTotal, dealerTotal, 1);
            }
            if (playerTotal == dealerTotal && isBlackjack == false)
            {
                whoWon = resultsText(playerTotal, dealerTotal, 0);
            }
            if (playerTotal == dealerTotal && isBlackjack == true)
            {
                whoWon = resultsText(playerTotal, dealerTotal, 1);
            }
        }
        else if (playerTotal > WIN_LIMIT)
        {
            if (dealerTotal <= WIN_LIMIT)
            {
                whoWon = resultsText(playerTotal, dealerTotal, -1);
            }
            else
            {
                whoWon = resultsText(playerTotal, dealerTotal, 0);
            }
        }

        return whoWon;
    }

    // tells you whether you won or lost
    private static int resultsText(int playerTotal, int dealerTotal, int value)
    {
        System.out.println("Your total: " + playerTotal);
        System.out.println("Dealer's total: " + dealerTotal);
        if (value == 1)
        {
            System.out.println("You win!");
        }
        else if (value == 0)
        {
            System.out.println("It's a draw!");
        }
        else if (value == -1)
        {
            System.out.println("You lost!");
        }
        System.out.println();

        return value;
    }

    // automatically processes dealer's aces
    private static int processDealerAces(ArrayList<Card> dealerHand)
    {
        int count = 0;
        int total = cardTotal(dealerHand);
        
        for(Card card : dealerHand)
        {
            if (getFace(card) == Face.Ace)
            {
                count++;
            }
        }

        if (total < 11 && count < 2) // 11 is the value of an Ace in the case of a blackjack
        {
            total += 11;
        }
        else 
        {
            for(int i = 0; i < count; i++)
            {
                total += 1;
            }
        }

        return total;
    }

    // gets the total of a dealer hand
    private static int finalizeDealerHand(
        Stack<Card> deck, ArrayList<Card> dealerHand)
    {
        int count = 0;

        while (processDealerAces(dealerHand) < 17) // 17 is the dealer's target hand value
        {
            dealerHand.add(deck.pop());   
        }

        count = processDealerAces(dealerHand);

        return count;
    }
    
    // gets the total of a player hand 
    private static int finalizeHand(Scanner s, ArrayList<Card> hand, boolean isBlackjack)
    {
        int total = cardTotal(hand);

        if (isBlackjack)
        {
            total += 11;
        }
        else
        {
            total += processAces(s, hand);
        }

        return total;
    }

    // goes through each ace and chooses value
    private static int processAces(Scanner s, ArrayList<Card> hand)
    {
        int total = 0;

        for(Card card : hand)
        {
            if (getFace(card) == Face.Ace)
            {
                total += aceLogic(s);
            }
        }

        return total;
    }

    // for the player to choose the value of their aces
    public static int aceLogic(Scanner s)
    {
        String selection = "";

        System.out.printf( "%n%s%n%s%n%s",
    "How much should this ace be worth?",
            "One point (o) ", "Eleven points (e) ");

        selection = s.next();
        if (selection.toLowerCase().charAt(0) == 'o')
        {
            return 1;
        }
        else if (selection.toLowerCase().charAt(0) == 'e')
        {
            return 11;
        }

        return 0;
    }

    // calculates the total card value in a given hand
    private static int cardTotal(ArrayList<Card> hand)
    {
        int total = 0;
        int current = -1;

        Face[] faces = 
            {Face.Ace, Face.Deuce, Face.Three, 
            Face.Four, Face.Five, Face.Six, 
            Face.Seven, Face.Eight, Face.Nine, 
            Face.Ten, Face.Jack, Face.Queen, 
            Face.King };

        Arrays.sort(faces);

        for(Card card : hand)
        {
            current = Arrays.binarySearch(
                faces, 0, faces.length, Card.getFace(card));

            if (current > 0 && current < 10)
            {
                total += (current + 1);
            }
            else if (current > 9)
            {
                total += 10;
            }
        }    
        
        return total;
    }
}
