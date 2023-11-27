package Blackjack;

import java.util.Stack;
import java.util.Scanner;
import static Blackjack.MethodManager.*;
import java.util.ArrayList;

class BradStrossBlackjack
{
    public static void main(String args[])
    {
        Scanner userInput = new Scanner(System.in);

        DeckOfCards ourDeck = initializeDeck();

        Stack<Card> gameDeck = deckToStack(ourDeck.getDeck(), 60);

        ArrayList<Card> hand = new ArrayList<Card>();

        menu(userInput, gameDeck, hand);
    }
}