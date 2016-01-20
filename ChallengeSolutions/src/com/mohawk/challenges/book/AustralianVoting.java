package com.mohawk.challenges.book;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class AustralianVoting {

	public static class Record {
		private String _name;
		private HashMap<Integer, Integer> _voteCounts = new HashMap<>();
		public void setName(String s) {
			_name = s;
		}
		public String getName() {
			return _name;
		}
		public void addChoice(int counter) {
			Integer num = _voteCounts.get(counter);
			if (num == null)
				_voteCounts.put(counter, 0);
			else
				_voteCounts.put(counter, ++num);
		}
		public int getChoice(int counter) {
			return _voteCounts.get(counter);
		}
	}
	
	public String findWinner(String[] candidates, int[][] votes) {
		
		Record[] recs = new Record[candidates.length];
		for (int i = 0; i < candidates.length; i++) {
			Record rec = new Record();
			rec.setName(candidates[i]);
			recs[i] = rec;
		}
		
		int numVotes = votes.length;
		for (int i = 0; i < numVotes; i++) {
			for (int j = 0; i < votes[i].length; j++) {
				int selected = votes[i][j];
				recs[selected].addChoice(j);
			}
		}
		
		// if one candidates receives more than 50%, then candidate is selected
		HashMap<Record, Double> over50 = new HashMap<>();
		for (Record r : recs) {
			int total = r.getChoice(0);
			double percent = total / (double) numVotes;
			if (percent > .5)
				return r.getName();
		}
		
		// all candidates tied for lowest number of votes are eliminated
		HashMap<Record, Integer> lowest = new HashMap<Record, Integer>();
		
		return null;
	}
	
	@Test
	public void testme() {
	
		String[] input = new String[] { 
				"1,","",
				"3",
				"John Doe", "Jane Smith", "Jane Austen",
				"1 2 3",
				"2 1 3",
				"2 3 1",
				"1 2 3",
				"3 1 2"
		};
		
		int caseNum = 0;
		int numCandidates = 0;
		String[] candidates = null;
		ArrayList<int[]> voteRecords = new ArrayList<>();
		
		for (int i = 0; i < input.length; i++) {
			if (caseNum == 0)
				caseNum = Integer.parseInt(input[i]);
			else if (caseNum > 0 && numCandidates == 0) {
				numCandidates = Integer.parseInt(input[i]);
				candidates = new String[numCandidates];
				for (int j = 0; j < numCandidates; ++j)
					candidates[j] = input[++i];
			} else if (caseNum > 0 && numCandidates > 0) {
				try {
					for (; !input[i].equals(""); i++)
						voteRecords.add(parseVote(input[i++]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (input[i].equals("") && caseNum > 0 && numCandidates > 0)
				numCandidates = 0;	
		}
		
		AustralianVoting av = new AustralianVoting();
		System.out.println(av.findWinner(candidates, voteRecords.toArray(new int[][]{})));
	}
	
	public static int[] parseVote(String input) throws Exception {
		if (input == null)
			throw new Exception("Invalid input>> " + input);
		
		String[] tokens = input.split(" ");
		int[] retArray = new int[tokens.length];
		int counter = 0;
		
		for (String token : tokens)
			retArray[counter++] = Integer.parseInt(token);
		return retArray;
	}
}
