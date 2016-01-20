package com.mohawk.challenges.book;

import java.util.LinkedList;

public class WarCardGame {

	private enum Card {
		ACE, KING, QUEEN, JOKER, _10, _9, _8, _7, _6, _5, _4, _3, _2;
	}
	
	private static LinkedList<Card>[] deal(Card[] cards, int times) {
		for (int i = 0; i < 26 * times; i ++) {
			int i1 = (int) (52 * (Math.random()));
			int i2 = (int) (52 * (Math.random()));
			//swap
			Card temp = cards[i1];
			cards[i1] = cards[i2];
			cards[i2] = temp;
		}
		
		// deal the cards
		LinkedList<Card> stack1 = new LinkedList<Card>();
		LinkedList<Card> stack2 = new LinkedList<Card>();
		for (int i = 0; i < cards.length; ++i) {
			stack1.push(cards[i]);
			stack2.push(cards[++i]);
		}
		return new LinkedList[] { stack1, stack2 };
	}
	
	public int simulate(LinkedList<Card> hand1, LinkedList<Card> hand2) {
		LinkedList<Card> temp = new LinkedList<>();
		int compares = 0;
		
		while (hand1.size() > 0 && hand2.size() > 0) {
			//System.out.println("hand1>> " + hand1.size());
			//System.out.println("hand2>> " + hand2.size());
			Card card1 = hand1.pop();
			Card card2 = hand2.pop();
			System.out.println("card compare>> " + card1 + ":" + card2);
			compares++;
			
			int compare = card1.compareTo(card2);
			if (compare > 0) { // less than
				System.out.println("card2 won");
				if (temp.size() > 0) {
					hand2.addAll(temp);
					temp.clear();
				}
				hand2.addLast(card2);
				hand2.addLast(card1);
			} else if (compare < 0) { // greater than
				System.out.println("card1 won");
				if (temp.size() > 0) {
					hand1.addAll(temp);
					temp.clear();
				}
				hand1.addLast(card1);
				hand1.addLast(card2);
			} else { // tied
				System.out.println("card tied!");
				// add to temporary list
				temp.addLast(card1);
				temp.addLast(card2);
				temp.addLast(hand1.pop());
				temp.addLast(hand2.pop());
			}
	
		}
		System.out.println("Number of compares>> " + compares);
		return hand1.size() == 0 ? 1 : 0;
	}
	
	public static void main(String[] args) {
		
		// initalize the deck
		Card[] DECK = new Card[52];
		int i = 0;
		for (Card card : Card.values()) {
			DECK[i++] = card;
			DECK[i++] = card;
			DECK[i++] = card;
			DECK[i++] = card;
		}
		
		LinkedList[] hands = WarCardGame.deal(DECK, 5);
		System.out.println("hand1>> " + hands[0]);
		System.out.println("hand2>> " + hands[1]);
		
		int winner = (new WarCardGame()).simulate(hands[0], hands[1]);
		System.out.println("Winner is>> " + winner);
	}

}
