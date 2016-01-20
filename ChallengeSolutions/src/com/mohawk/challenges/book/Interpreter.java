package com.mohawk.challenges.book;


public class Interpreter {

	private static class ThreeDigitInteger {
		private int _value = 0;
		
		public ThreeDigitInteger(String instruct) {
			int test = Integer.parseInt(String.valueOf(instruct));
			if (test > 999)
				_value = 0 % 1000;
			else
				_value = test;
		}
		public int getValue() {
			return _value;
		}
		public int[] getValueAsIntArray() {
			StringBuffer sb = new StringBuffer();
			if (_value < 99)
				sb.append("0");
			if (_value < 9)
				sb.append("0");
			sb.append(_value);
			return new int[] {
				Character.getNumericValue(sb.charAt(0)),
				Character.getNumericValue(sb.charAt(1)),
				Character.getNumericValue(sb.charAt(2))
			};
		}
		public void setValue(int val) throws Exception {
			if (val > 999)
				_value = val % 1000;
			else
				_value = val;
		}
	}
	
	private static ThreeDigitInteger[] _register = new ThreeDigitInteger[10];
	private static ThreeDigitInteger[] _RAM = new ThreeDigitInteger[1000];
	
	static {
		for (int i = 0; i < 10; i++) {
			_register[i] = new ThreeDigitInteger("0");
		}
		
	}
	
	public int run(String[] input) throws Exception {
		int cases = 0;
		int c = 0;
		
		// populate RAM from input
		for (int i = 0; i < input.length; i++) {
			if (cases == 0) {
				cases = Integer.parseInt(input[i]);
				continue;
			} else if (input[i].trim().length() == 0)
				continue;
			else {
				// fill RAM
				_RAM[c++] = new ThreeDigitInteger(input[i]);
			}
		}
		int numExec = 0;
		
		for (int i = 0; i < _RAM.length; i++) {
			if (_RAM[i] == null)
				break;
			//System.out.println("running instr>> " + _RAM[i].getValue());	
			int[] in = _RAM[i].getValueAsIntArray();
			int d = in[1];
			int n = in[2];
			
			switch (in[0]) {
				case 1:
					return ++numExec;
				case 2: // set reg d to n
					setRegister(_register, d, n); //printReg(_register);
					++numExec;
					break;
				case 3:
					addRegister(_register, d, n); //printReg(_register);
					++numExec;
					break;
				case 4:
					multipleRegister(_register, d, n); //printReg(_register);
					++numExec;
					break;
				case 5:
					setRegisterFromAnother(_register, d, n);
					++numExec;
					break;
				case 6:
					addRegisterFromAnother(_register, d, n); //printReg(_register);
					++numExec;
					break;
				case 7:
					multipleRegisterFromAnother(_register, d, n);
					++numExec;
					break;
				case 8:
					setRegisterFromRAM(_register, _RAM, d, n);
					++numExec;
					break;
				case 9:
					setRAMFromAddress(_register, _RAM, d, n);
					++numExec;
					break;
				case 0:
					int tempC = gotoAddress(_register, d, n); //printReg(_register);
					if (tempC != -1)
						i = tempC - 1;
					//System.out.println("i>> " + i);
					++numExec;
					break;
			}
		}
		throw new Exception("Halt command never executed.");
	}
	public void setRegister(ThreeDigitInteger[] reg, int d, int n) throws Exception {
		reg[d].setValue(n);
	}
	public void addRegister(ThreeDigitInteger[] reg, int d, int n) throws Exception {
		int val = reg[d].getValue();
		reg[d].setValue(val + n);
	}
	public void multipleRegister(ThreeDigitInteger[] reg, int d, int n) 
	throws Exception {
		int val = reg[d].getValue();
		reg[d].setValue(val * n);
	}
	public void setRegisterFromAnother(ThreeDigitInteger[] reg, int d, int s) 
	throws Exception {
		reg[d].setValue(reg[s].getValue());
	}
	public void addRegisterFromAnother(ThreeDigitInteger[] reg, int d, int s) 
	throws Exception {
		int val = reg[d].getValue();
		reg[d].setValue(val + reg[s].getValue());
	}
	public void multipleRegisterFromAnother(ThreeDigitInteger[] reg, int d, int s) 
	throws Exception {
		int val = reg[d].getValue();
		reg[d].setValue(val * reg[s].getValue());
	}
	public void setRegisterFromRAM(ThreeDigitInteger[] reg, ThreeDigitInteger[] RAM,
			int d, int a) throws Exception {
		int address = reg[a].getValue();
		reg[d].setValue(RAM[address].getValue());
	}
	public void setRAMFromAddress(ThreeDigitInteger[] reg, ThreeDigitInteger[] RAM,
			int s, int a) throws Exception {
		int address = reg[a].getValue();
		RAM[address] = reg[s];
	}
	public int gotoAddress(ThreeDigitInteger[] reg, int d, int s) {
		if (reg[s].getValue() == 0)
			return -1;
		else
			return reg[d].getValue();
	}
	public void printReg(ThreeDigitInteger[] reg) {
		for (int i = 0; i < reg.length; i++) {
			System.out.print("[" + i + "]:" + reg[i].getValue() + ",");
		} 
		System.out.print('\n');
	}
	
	public static void main(String[] args) {
		String[] input = new String[] { 
				"1",
				"",
				"299","492","495","399","492","495","399",
				"283","279","689","078","100","000","000","000"
		};
				
		try {
			Interpreter it = new Interpreter();
			System.out.println("num instructions>> " + it.run(input));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
