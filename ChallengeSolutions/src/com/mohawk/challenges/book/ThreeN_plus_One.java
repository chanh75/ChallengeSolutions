package com.mohawk.challenges.book;

public class ThreeN_plus_One {

	public int max(int one, int two) {
		int max = 0;
		for (int i = one; i <= two; ++i) {
			int val = calculate(i);
			if (val > max)
				max = val;
		}
		return max;
	}
	
	private int calculate(int i) {
		int totalNum = 0;
		int val = i;
		while (val != 1) {
			totalNum++;
			if (val % 2 == 0) // even
				val = val / 2;
			else // odd
				val = (3 * val) + 1;
		}
		totalNum++; // account for 1
		return totalNum;
	}
	
	public static void main(String[] args) {

		ThreeN_plus_One prob = new ThreeN_plus_One();
		System.out.println(prob.max(1, 10));
		System.out.println(prob.max(100, 200));
		System.out.println(prob.max(201, 210));
		System.out.println(prob.max(900, 1000));
	}

}
