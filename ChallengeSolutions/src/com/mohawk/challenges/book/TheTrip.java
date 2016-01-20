package com.mohawk.challenges.book;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class TheTrip {

	public static String calculate(String[] input) {
		
		StringBuffer out = new StringBuffer();
		
		for (int s = 0; s < input.length; ++s) {
			int row = Integer.parseInt(input[s]);
			if (row == 0)
				continue;
			// sum values
			BigDecimal sum = new BigDecimal("0");
			for (int i = s + 1; i < s + row + 1; ++i)
				sum = sum.add(BigDecimal.valueOf(Double.valueOf(input[i])));
			
			// average
			BigDecimal avg = sum.divide(new BigDecimal(row), 2, RoundingMode.DOWN); 
			
			// difference
			BigDecimal dif = new BigDecimal("0");
			for (int i = s + 1; i < s+ row + 1; ++i) {
				BigDecimal val = BigDecimal.valueOf(Double.valueOf(input[i]));
				
				if (val.compareTo(avg) < 0)
					dif = dif.add(avg.subtract(val));
				
			}
			out.append("$" + dif + System.lineSeparator());
			s += row;
		}
		return out.toString();
	}
	
	public static void main(String[] args) {
		
		String[] input = new String[] { 
				"3",
				"10.00",
				"20.00",
				"30.00",
				"4",
				"15.00",
				"15.01",
				"3.00",
				"3.01",
				"0"
		};
		
		System.out.println(TheTrip.calculate(input));

	}

}
