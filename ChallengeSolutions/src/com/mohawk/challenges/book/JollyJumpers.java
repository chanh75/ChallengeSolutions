package com.mohawk.challenges.book;

import java.util.Arrays;
import java.util.LinkedList;

public class JollyJumpers {

	public String[] determine(String[] input) {
		String[] output = new String[input.length];
		
		for (int i = 0; i < input.length; ++i)
			output[i] = determine0(input[i]);
		
		return output;
	}
	
	private String determine0(String input) {
		// parse
		String[] numbers = input.split(" "); 
		System.out.println(Arrays.toString(numbers));
		
		LinkedList<Integer> list = new LinkedList<>();
		for (String num : numbers)
			list.add(Integer.parseInt(num));
		
		int len = list.size();
		int val1 = list.poll();
		while (list.size() > 0) {
			int val2 = list.poll();
			if (Math.abs(val2 - val1) > len)
				return "Not jolly";
			val1 = val2;
		}
		return "Jolly";
	}
	
	public static void main(String[] args) {
		
		String[] input = new String[] { 
				"4 1 4 2 3",
				"5 1 4 2 -1 6"
		};
		
		String[] output = (new JollyJumpers()).determine(input);
		for (String o : output)
			System.out.println(o);
	}
}
