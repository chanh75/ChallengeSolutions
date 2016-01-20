package com.mohawk.challenges.book;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class PokerHands {
	
	private static class Card {
		private enum Rank {
			_2, _3, _4, _5, _6, _7, _8, _9, _T, _J, _Q, _K, _A
		}
		private enum Suit {
			H, D, S, C
		}
		private Rank _rank;
		private Suit _suite;
		public Card(String cardString) {
			_rank = Rank.valueOf("_" + cardString.charAt(0));
			_suite = Suit.valueOf(String.valueOf(cardString.charAt(1)));

		}
		public Rank getRank() {
			return _rank;
		}
		public Suit getSuite() {
			return _suite;
		}
		public String toString() {
			return _rank.toString() + _suite.toString();
		}
	}
	
	private static class CardHand implements Comparable<CardHand> {
		private HandType _type;
		private Card.Rank _highCard;
		public enum HandType {
			HIGH_CARD, PAIR, TWO_PAIRS, THREE_KIND, 
			STRAIGHT, FLUSH, FULL_HOUSE, FOUR_KIND, STRAIGHT_FLUSH
		}
		public CardHand(HandType type, Card.Rank rank) {
			// determine high card
			_type = type;
			_highCard = rank;
		}
		
		public HandType getHandType() {
			return _type;
		}
		
		public Card.Rank getHighCard() {
			return _highCard;
		}
		
		public String toString() {
			return _type.toString();
		}
		
		public static CardHand resolve(final Card[] cards) {
			LinkedList<Card> queue = new LinkedList<>();
			for (Card card : cards)
				queue.push(card);
			
			CardHand hand;
			hand = isStraightFlush(queue);
			if (hand != null) return hand;
			hand = isFourKind(queue);
			if (hand != null) return hand;
			hand = isFullHouse(queue);
			if (hand != null) return hand;
			hand = isFlush(queue);
			if (hand != null) return hand;
			hand = isStraight(queue);
			if (hand != null) return hand;
			hand = isThreeKind(queue);
			if (hand != null) return hand;
			hand = isTwoPairs(queue);
			if (hand != null) return hand;
			hand = isPair(queue);
			if (hand != null) { System.out.println("ispair."); return hand; }
			
			return null;
		}
		
		private static CardHand isStraightFlush(LinkedList<Card> cards) {
			Card card0 = cards.get(0);
			for (int i = 1; i < cards.size(); ++i){
				Card card1 = cards.get(i);
				if (card0.getRank().ordinal() != card1.getRank().ordinal() + 1
						|| card0.getSuite() != card1.getSuite())
					return null;
				else
					card0 = card1;
			}
			return new CardHand(HandType.STRAIGHT_FLUSH, card0.getRank());
		}
		
		private static CardHand isFourKind(LinkedList<Card> cards) {
			HashMap<Card.Rank, Integer> map = new HashMap<>();
			for (int i = 0; i < cards.size(); ++i) {
				Card card = cards.get(i);
				if (map.containsKey(card.getRank())) {
					int counter = map.get(card.getRank());
					map.put(card.getRank(), ++counter);
				} else
					map.put(card.getRank(), 1);
			}
			for (Card.Rank rank : map.keySet()) {
				int c = map.get(rank);
				if (c >= 4)
					return new CardHand(HandType.FOUR_KIND, rank);
			}
			return null;
		}
		 
		private static CardHand isFullHouse(LinkedList<Card> cards) {
			HashMap<Card.Rank, Integer> map = new HashMap<>();
			for (int i = 0; i < cards.size(); ++i) {
				Card card = cards.get(i);
				if (map.containsKey(card.getRank())) {
					int counter = map.get(card.getRank());
					map.put(card.getRank(), ++counter);
				} else
					map.put(card.getRank(), 1);
			}
			Card.Rank highRank = null;
			boolean has3 = false;
			boolean has2 = false;
			for (Card.Rank rank : map.keySet()) {
				int c = map.get(rank);
				if (c == 3) {
					highRank = rank;
					has3 = true;
				} else if (c == 2)
					has2 = true;
			}
			if (has3 && has2)
				return new CardHand(HandType.FULL_HOUSE, highRank);
			else
				return null;
		}
		
		private static CardHand isFlush(LinkedList<Card> cards) {
			Card.Rank highRank = cards.get(0).getRank();
			Card.Suit firstSuite = cards.get(0).getSuite();
			
			for (int i = 1; i < cards.size(); ++i) {
				Card card = cards.get(i);
				if (card.getSuite().compareTo(firstSuite) !=0)
					return null;
				else if (card.getRank().compareTo(highRank) > 0)
					highRank = card.getRank();
			}
			return new CardHand(HandType.FLUSH, highRank);
		}
		
		private static CardHand isStraight(LinkedList<Card> cards) {
			LinkedList<Card> temp = (LinkedList<Card>) cards.clone();
			Collections.sort(temp, new Comparator<Card>(){
				@Override
				public int compare(Card arg0, Card arg1) {
					return arg0.getRank().compareTo(arg1.getRank());
				}});
			//System.out.println("temp>> " + temp);
			for(int i = 0; i < temp.size() - 1; ++i) {
				Card c0 = temp.get(i);
				Card c1 = temp.get(i + 1);
				if (c0.getRank().ordinal() != c1.getRank().ordinal() - 1)
					return null;
			}
			return new CardHand(HandType.STRAIGHT, temp.get(temp.size() - 1).getRank());
		}
		
		private static CardHand isThreeKind(LinkedList<Card> cards) {
			HashMap<Card.Rank, Integer> map = new HashMap<>();
			for (int i = 0; i < cards.size(); ++i) {
				Card card = cards.get(i);
				if (map.containsKey(card.getRank())) {
					int counter = map.get(card.getRank());
					map.put(card.getRank(), ++counter);
				} else
					map.put(card.getRank(), 1);
			}
			Card.Rank highRank = null;
			boolean has3 = false;
			boolean has1 = false;
			for (Card.Rank rank : map.keySet()) {
				int c = map.get(rank);
				if (c == 3) {
					highRank = rank;
					has3 = true;
				} else if (c == 1)
					has1 = true;
			}
			if (has3 && has1)
				return new CardHand(HandType.THREE_KIND, highRank);
			else
				return null;
		}
		
		private static CardHand isTwoPairs(LinkedList<Card> cards) {
			HashMap<Card.Rank, Integer> map = new HashMap<>();
			for (int i = 0; i < cards.size(); ++i) {
				Card card = cards.get(i);
				if (map.containsKey(card.getRank())) {
					int counter = map.get(card.getRank());
					map.put(card.getRank(), ++counter);
				} else
					map.put(card.getRank(), 1);
			}
			Card.Rank highRank = null;
			boolean has2a = false;
			boolean has2b = false;
			for (Card.Rank rank : map.keySet()) {
				int c = map.get(rank);
				if (!has2a && c == 2) {
					highRank = rank;
					has2a = true;
				} else if (!has2b && c == 2) {
					if (rank.ordinal() > highRank.ordinal())
						highRank = rank;
					has2b = true;
				}
			}
			if (has2a && has2b)
				return new CardHand(HandType.TWO_PAIRS, highRank);
			else
				return null;
		}
		
		private static CardHand isPair(LinkedList<Card> cards) {
			HashMap<Card.Rank, Integer> map = new HashMap<>();
			for (int i = 0; i < cards.size(); ++i) {
				Card card = cards.get(i);
				if (map.containsKey(card.getRank())) {
					int counter = map.get(card.getRank());
					map.put(card.getRank(), ++counter);
				} else
					map.put(card.getRank(), 1);
			}
			Card.Rank highRank = null;
			boolean has2 = false;
			for (Card.Rank rank : map.keySet()) {
				int c = map.get(rank);
				if (c == 2) {
					highRank = rank;
					has2 = true;
				}
			}
			if (has2 && map.size() == 4)
				return new CardHand(HandType.PAIR, highRank);
			else
				return null;
		}

		@Override
		public int compareTo(CardHand arg0) {
			// TODO Auto-generated method stub
			int i = this.getHandType().compareTo(arg0.getHandType());
			if (i != 0)
				return i;
			else {
				// same hand rank, compare on high card
				return this.getHighCard().compareTo(arg0.getHighCard());
			}
		}
		
	}
	
	public String[] determine(String[] input) throws Exception {
		String[] ret = new String[input.length];
		for (int i = 0; i < input.length; ++i) {
			ret[i] = determine0(input[i]);
		}
		return ret;
	}
	
	private String determine0(String input) throws Exception {
		
		Card[][] hands = new Card[2][5];
		
		// parse out the hands
		String[] cards = input.split(" ");
		int counter = 0;
		for (int h = 0; h < 2; ++h)
			for (int i = 0; i < 5; ++i)
				hands[h][i] = new Card(cards[counter++]);
		
		
		CardHand[] handObjs = new CardHand[2];
		
		for (int i = 0; i < hands.length; i++)
			handObjs[i] = CardHand.resolve(hands[i]);
		
		// compare the hands
		if (handObjs[0].compareTo(handObjs[1]) > 0)
			return "Black wins.";
		else if (handObjs[0].compareTo(handObjs[1]) < 0)
			return "White wins.";
		else 
			return "Tie.";
	}
	
	public static void main(String[] args) {
		
		
		try {
			String[] input = new String[] {
					//"2H 2D 2S 2C KD 2D 3D 4D 5D 6D",
					//"2H 2D 2S 2C KD 6H JH 8H AH QH"
					"4H JC JH 7D AD 7H QC 3S QD 9D"
					//"2H 3D 5S 9C KD 2C 3H 4S 8C AH",
					//"2H 4S 4C 2D 4H 2S 8S AS QS 3S",
					//"2H 3D 5S 9C KD 2C 3H 4S 8C KH",
					//"2H 3D 5S 9C KD 2D 3H 5C 9S KH"
			};
			
			String[] output = (new PokerHands()).determine(input);
			for (String out : output)
				System.out.println(out);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
