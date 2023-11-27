package Blackjack;

import static Blackjack.Card.*;

//DeckOfCards class represents a deck of playing cards
class DeckOfCards 
{
    private static final int NUMBER_OF_CARDS = 312;

    private Card[] deck = new Card[NUMBER_OF_CARDS];
    private int currentCard = 0;

    // constructor fills deck of Cards
    public DeckOfCards()
    {
        Face[] faces = 
            {Face.Ace, Face.Deuce, Face.Three, 
            Face.Four, Face.Five, Face.Six, 
            Face.Seven, Face.Eight, Face.Nine, 
            Face.Ten, Face.Jack, Face.Queen, 
            Face.King };

        Suit[] suits = {
            Suit.Clubs, Suit.Diamonds, 
            Suit.Hearts, Suit.Spades};

        // populate deck with Card objects
        for (int count = 0; count < deck.length; count++)
        {
            deck[count] = 
                new Card(faces[count % 13], suits[(count % 52) / 13]);
        }
    }

    public int getDeckSize()
    {
        return this.NUMBER_OF_CARDS;
    }

    public Card[] getDeck()
    {
        return this.deck;
    }

    public void setDeck(Card[] deck)
    {
        this.deck = deck;
    }

    public int getCurrentCard()
    {
        return this.currentCard;
    }

    public void setCurrentCard(int value)
    {
        this.currentCard = value;
    }

}
