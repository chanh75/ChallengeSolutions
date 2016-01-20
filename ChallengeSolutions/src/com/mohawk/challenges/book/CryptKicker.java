package com.mohawk.challenges.book;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CryptKicker {

	public static class Word {
		
		private char[][] _chars; // 2-dim array
		private int _length;
		
		public Word(String s) {
			_length = s.length();
			_chars = new char[2][_length];
			for (int i = 0; i < s.length(); ++i)
				_chars[0][i] = s.charAt(i);
			Arrays.fill(_chars[1], '*');
		}
		public int length() {
			return _length;
		}
		public TranslatePattern join(String translateWord) throws Exception {
			if (length() != translateWord.length()) // if not same length, reject
				return null;
			
			for (int i = 0; i < translateWord.length(); ++i) {
				char c = _chars[1][i];
				if (c != '*' && c != translateWord.charAt(i)) // prefilled, but some mismatch of characters
					return null;
			}
			HashMap<Character, Character> translateMap = new HashMap<>();
			for (int i = 0; i < translateWord.length(); ++i) {
				char c = _chars[1][i];
				if (c == '*') { // is not set
					if (translateMap.containsKey(_chars[0][i]))
						_chars[1][i] = translateMap.get(_chars[0][i]);
					else {
						char t = translateWord.charAt(i);
						
						// check that it's not already mapped
						if (translateMap.containsValue(t)) {
							reset();
							return null;
						}
						
						_chars[1][i] = t;
						// do downstream of the other characters
						translateMap.put(_chars[0][i], t);
					}
				}
			}
			return new TranslatePattern(translateMap);
		}
		public void join(TranslatePattern pattern) {
			for (int i = 0; i < length(); ++i) {
				if (_chars[1][i] == '*') //if empty
					_chars[1][i] = pattern.get(_chars[0][i]);
			}
		}
		public String getDecrypted() {
			return new String(_chars[1]);
		}
		public boolean fullyDecrypted() {
			return ((new String(_chars[1])).indexOf('*') == -1);
		}
		public void reset() {
			Arrays.fill(_chars[1], '*');
		}
		public String toString() {
			return Arrays.toString(_chars[0]) + ':' + Arrays.toString(_chars[1]);
		}
	}
	
	public static class TranslatePattern {
		private HashMap<Character, Character> _map = new HashMap<>();
		public TranslatePattern() {
		}
		public TranslatePattern(HashMap<Character, Character> map) {
			_map.putAll(map);
		}
		public void add(TranslatePattern pattern) {
			for (char c : pattern._map.keySet()) {
				if (!this._map.containsKey(c))
					this._map.put(c, pattern._map.get(c));
			}
		}
		public void remove(TranslatePattern pattern) {
			for (char c : pattern._map.keySet()) {
				if (this._map.containsKey(c))
					this._map.remove(c);
			}
		}
		public char get(char c) {
			if (_map.containsKey(c))
				return _map.get(c);
			else
				return '*';
		}
		public String toString() {
			return _map.toString();
		}
		public void clear() {
			_map.clear();
		}
	}
	
	private List<String> _translateWords;
	private List<String> _encryptedLines;
	
	public CryptKicker read(File file) throws IOException {
		
		List<String> _inputLines = Files.readAllLines(file.toPath());
		int num = -1;
		_translateWords = new ArrayList<>();
		_encryptedLines = new ArrayList<>();
		
		for (String line : _inputLines) {
			if (num < 0)
				num = Integer.parseInt(line);
			else if (_translateWords.size() < num)
				_translateWords.add(line);
			else
				_encryptedLines.add(line);
		}
		
		return this;
	}
	
	public CryptKicker read(String[] lines) throws IOException {
		int num = -1;
		_translateWords = new ArrayList<>();
		_encryptedLines = new ArrayList<>();
		
		for (String line : lines) {
			if (num < 0)
				num = Integer.parseInt(line);
			else if (_translateWords.size() < num)
				_translateWords.add(line);
			else
				_encryptedLines.add(line);
		}
		
		return this;
	}
	
	public String decryptFile() throws Exception {
		
		final Pattern p = Pattern.compile("\\w+");
		StringBuffer sb = new StringBuffer();
		
		// go through each encrypted line
		for (String encryptedLine : _encryptedLines) {
			
			Deque<Word> encryptedWords = new ArrayDeque<Word>();
			Matcher m = p.matcher(encryptedLine); // split up the words via regex
			while (m.find()) {
				String word = m.group();
				encryptedWords.add(new Word(word));
			}
		
			LinkedList<Word> solvedWords = new LinkedList<>();
			decrypt(encryptedWords, 
					new LinkedList<String>(_translateWords), 
					solvedWords, 
					new TranslatePattern());
			
			// print out the results
			if (encryptedWords.size() > 0)
				for (Word encryptedWord : encryptedWords)
					sb.append(encryptedWord.getDecrypted()).append(' ');
			else
				for (Word solvedWord : solvedWords)
					sb.append(solvedWord.getDecrypted()).append(' ');
			
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	private boolean decrypt(Deque<Word> encryptedWords, 
			LinkedList<String> translateWords, 
			LinkedList<Word> solvedWords,
			TranslatePattern pattern) 
	throws Exception {
		
		if (encryptedWords.size() == 0)
			return true;
		
		Word word = encryptedWords.pop();
		word.join(pattern);
		
		if (word.fullyDecrypted()) { // fully decrypted from using master pattern
			solvedWords.add(word);
			if (decrypt(encryptedWords, translateWords, solvedWords, pattern))
				return true;
			else {
				word.reset();
				solvedWords.pop();
				return false;
			}
		}
		
		int i = 0;
		
		while (i < translateWords.size()) { // go through each translate word
			String translateWord = translateWords.get(i);
			
			TranslatePattern newPattern = word.join(translateWord);
			System.out.println("translate>> " + word + ">>" + translateWord);
			if (newPattern != null) { // matched!
				// put on the return queue
				solvedWords.add(word);
				// remove from translated words
				String removedTranslate = translateWords.remove(i);
				// add the translation pattern to the master list
				pattern.add(newPattern);
				
				// solve the next encrypted word
				if (decrypt(encryptedWords, translateWords, solvedWords, pattern))
					return true; // solved, do not need to go to next translated word
				else {
					// reset operation
					word.reset();
					translateWords.add(i, removedTranslate);
					solvedWords.pop();
					pattern.remove(newPattern);
				}
			} 
			i++;
			
		}
		System.out.println("no match, returning...");
		word.reset();
		encryptedWords.push(word);
		return false;
	}
	
	public static void main(String[] args) {
		
		//String location = "/Users/cnguyen/Downloads/test.txt";
		String[] lines = new String[] { 
				"6",
				"puff",
				"and",
				"spot",
				"jane",
				"dick",
				"yertle",
				"bjvg xsb hxsn xsb qymm xsb rqat xsb pnetfn",
				"xxxx yyy zzzz www yyyy aaa bbbb ccc dddddd"
		};
		
		try {
			String output = (new CryptKicker())
								//.read(new File(location))
								.read(lines)
								.decryptFile();
			System.out.println("output>>\n" + output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
