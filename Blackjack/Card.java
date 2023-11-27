package Blackjack;

// Card class represents a playing card

class Card 
{
    public enum Face {
        None, Ace, Deuce, Three, Four, Five, Six, 
        Seven, Eight, Nine, Ten, Jack, Queen, King
    }

    public enum Suit {
        Hearts, Diamonds, Clubs, Spades
    }
    
    private final Face face;
    private final Suit suit;

    // two-argument constructor initializes card's face and suit
    public Card(Face cardFace, Suit cardSuit)
    {
        this.face = cardFace;
        this.suit = cardSuit;
    }

    // return String representation of Card
    public String toString()
    {
        return face + " of " + suit;
    }

    public static Face getFace(Card cardInput)
    {
        return cardInput.face;
    }

    public static Suit getSuit(Card cardInput)
    {
        return cardInput.suit;
    }
}
