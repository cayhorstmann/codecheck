package com.horstmann.codecheck;

import java.util.Random;

/*
 * Defines all supported functions which can be accessible from JS nashorn 
 */
public class Functions {
	public static final int DIGIT = 1;
	public static final int UPPERCASE = 2;
	public static final int LOWERCASE = 4;
	public static final int LETTER = 6;
	public static final int CHAR = 7;
	
	private Random randomGenarator;
	private Random selectGenarator;
	private WordCollection wordCollection;
	
	public Functions(long seed) {
		randomGenarator = new Random();
		selectGenarator = new Random(seed);
		wordCollection = WordCollection.getInstance();
	}
	
	public int randomInt() {
		return randomGenarator.nextInt();
	}
	
	public int selectInt() {
		return selectGenarator.nextInt();
	}
	
	public int randomInt(int range) {
		return randomGenarator.nextInt(range);
	}
	
	public int selectInt(int range) {
		return selectGenarator.nextInt(range);
	}

	public double randomDouble() {
		return randomGenarator.nextDouble();
	}
	
	public double selectDouble() {
		return selectGenarator.nextDouble();
	}
	
	public double randomDouble(int range) {
		return randomGenarator.nextInt(range) + randomGenarator.nextDouble();
	}
	
	public double selectDouble(int range) {
		return selectGenarator.nextInt(range) + selectGenarator.nextDouble();
	}
	
	public boolean randomBoolean() {
		return randomGenarator.nextBoolean();
	}
	
	public boolean selectBoolean() {
		return selectGenarator.nextBoolean();
	}
	
	public String randomDigitString(int length) {
		String r = "" + (char) (randomInt(9) + 1 + '0');
		for (int i = 1; i < length; i++)
			r += (char) (randomInt(10) + '0');
		return r;
	}
	
	public String randomDigitString(int length, int digit, int times) {
		String r = "";
		String x = "";
		String y = "";
		for (int i = 0; i < times; i++)
			x += (char) (digit + '0');
		for (int i = times; i < length; i++)
			y += (char) (randomInt(10) + '0');
		int indexX = 0;
		int indexY = 0;
		while (indexX < x.length() && indexY < y.length()) {
			r += randomBoolean() ? x.charAt(indexX++) : y.charAt(indexY++);
		}
		while (indexX < x.length())
			r += x.charAt(indexX++);
		while (indexY < y.length())
			r += y.charAt(indexY++);
		while (r.charAt(0) == '0') {
			if (digit == 0)
				r = r.substring(1) + '0';
			else
				r = r.substring(1) + (char) (randomInt(10) + '0');
		}
		return r;
	}
	
	public String selectDigitString(int length) {
		String r = "" + (char) (selectInt(9) + 1 + '0');
		for (int i = 0; i < length; i++)
			r += (char) (selectInt(10) + '0');
		return r;
	}
	
	public String randomCharString(int length) {
		String r = "";
		for (int i = 0; i < length; i++) {
			int v = randomBoolean() ? (int) 'a' : (int) 'A';
			v += randomInt(26);
			r += (char) v;
		}
		return r;
	}
	
	public Object randomItem(Object... listItems) {
		int index = randomInt(listItems.length);
		return listItems[index];
	}
	
	public String randomItemToString(Object... listItems) {
		return randomItem(listItems).toString();
	}
	
	public Object selectItem(Object... listItems) {
		int index = selectInt(listItems.length);
		return listItems[index];
	}
	
	public String selectItemToString(Object... listItems) {
		return selectItem(listItems).toString();
	}
	
	public int[] randomIntArray(int length) {
		int[] r = new int[length];
		for (int i = 0; i < length; i++)
			r[i] = randomInt();
		return r;
	}
	
	public int[] selectIntArray(int length) {
		int[] r = new int[length];
		for (int i = 0; i < length; i++)
			r[i] = selectInt();
		return r;
	}
	
	public int[] randomIntArray(int length, int lowerBound, int upperBound) {
		int[] r = new int[length];
		if (upperBound < 0) {
			r = randomIntArray(length, -upperBound, -lowerBound);
			for (int i = 0; i < length; i++) {
				r[i] *= -1;
			}
		} else {
			for (int i = 0; i < length; i++) {
				r[i] = lowerBound + randomInt(upperBound - lowerBound + 1);
			}
		}
		return r;
	}
	
	public int[] selectIntArray(int length, int lowerBound, int upperBound) {
		int[] r = new int[length];
		if (upperBound < 0) {
			r = selectIntArray(length, -upperBound, -lowerBound);
			for (int i = 0; i < length; i++) {
				r[i] *= -1;
			}
		} else {
			for (int i = 0; i < length; i++) {
				r[i] = lowerBound + selectInt(upperBound - lowerBound + 1);
			}
		}
		return r;
	}
	
	public double[] randomDoubletArray(int length) {
		double[] r = new double[length];
		for (int i = 0; i < length; i++)
			r[i] = randomDouble();
		return r;
	}
	
	public double[] selectDoubletArray(int length) {
		double[] r = new double[length];
		for (int i = 0; i < length; i++)
			r[i] = selectDouble();
		return r;
	}
	
	public double[] randomDoubleArray(int length, int lowerBound, int upperBound) {
		double[] r = new double[length];
		if (upperBound < 0) {
			r = randomDoubleArray(length, -upperBound, -lowerBound);
			for (int i = 0; i < length; i++) {
				r[i] *= -1;
			}
		} else {
			for (int i = 0; i < length; i++) {
				r[i] = lowerBound + randomDouble(upperBound - lowerBound + 1);
			}
		}
		return r;
	}
	
	public double[] selectDoubleArray(int length, int lowerBound, int upperBound) {
		double[] r = new double[length];
		if (upperBound < 0) {
			r = selectDoubleArray(length, -upperBound, -lowerBound);
			for (int i = 0; i < length; i++) {
				r[i] *= -1;
			}
		} else {
			for (int i = 0; i < length; i++) {
				r[i] = lowerBound + selectDouble(upperBound - lowerBound + 1);
			}
		}
		return r;
	}
	
	public String randomString(int length, String characters) {
		String r = "";
		for (int i = 0; i < length; i++)
			r += characters.charAt(randomInt(characters.length()));
		return r;
	}
	
	public String selectString(int length, String characters) {
		String r = "";
		for (int i = 0; i < length; i++)
			r += characters.charAt(selectInt(characters.length()));
		return r;
	}
	
	private String charString(int flag) {
		String digit = "0123456789";
		String upper = "QWERTYUIOPLKJHGFDSAZXCVBNM";
		String lower = "qwertyuioplkjhgfdsazxcvbnm";
		String r = "";
		switch (flag) {
		case DIGIT:
			r = digit;
			break;
		case UPPERCASE:
			r = upper;
			break;
		case LOWERCASE:
			r = lower;
			break;
		case LETTER:
			r = upper + lower;
			break;
		case CHAR:
			r = upper + lower + digit;
			break;
		}
		
		return r;
	}
	
	public String randomString(int length, int flag) {
		String characters = charString(flag);
		return randomString(length, characters);
	}
	
	public String selectString(int length, int flag) {
		String characters = charString(flag);
		return selectString(length, characters);
	}
	
	public String[] randomStringArray(int lengthOfArray, int minLengthOfString, int maxLengthOfString, String setOfcharacters) {
		String[] r = new String[lengthOfArray];
		for (int i = 0; i < lengthOfArray; i++) {
			int length = minLengthOfString + randomInt(maxLengthOfString - minLengthOfString + 1);
			r[i] = randomString(length, setOfcharacters);
		}
		return r;
	}
	
	public String[] selectStringArray(int lengthOfArray, int minLengthOfString, int maxLengthOfString, String setOfcharacters) {
		String[] r = new String[lengthOfArray];
		for (int i = 0; i < lengthOfArray; i++) {
			int length = minLengthOfString + selectInt(maxLengthOfString - minLengthOfString + 1);
			r[i] = selectString(length, setOfcharacters);
		}
		return r;
	}
	
	public String[] randomStringArray(int lengthOfArray, int minLengthOfString, int maxLengthOfString, int flag) {
		String setOfcharacters = charString(flag);
		return randomStringArray(lengthOfArray, minLengthOfString, maxLengthOfString, setOfcharacters);
	}
	
	public String[] selectStringArray(int lengthOfArray, int minLengthOfString, int maxLengthOfString, int flag) {
		String setOfcharacters = charString(flag);
		return selectStringArray(lengthOfArray, minLengthOfString, maxLengthOfString, setOfcharacters);
	}
	
	public String randomWord(int minLength, int maxLength) {
		String r = "";
		int count = wordCollection.getTotal(minLength, maxLength);
		int id = randomInt(count);
		r = wordCollection.getWord(minLength, maxLength, id);
		return r;
	}
	
	public String selectWord(int minLength, int maxLength) {
		String r = "";
		int count = wordCollection.getTotal(minLength, maxLength);
		int id = selectInt(count);
		r = wordCollection.getWord(minLength, maxLength, id);
		return r;
	}
	
	public String[] randomWordArray(int lengthOfArray, int minLengthOfWord, int maxLengthOfWord) {
		String[] r = new String[lengthOfArray];
		for (int i = 0; i < lengthOfArray; i++) {
			r[i] = randomWord(minLengthOfWord, maxLengthOfWord);
		}
		return r;
	}
	
	public String[] selectWordArray(int lengthOfArray, int minLengthOfWord, int maxLengthOfWord) {
		String[] r = new String[lengthOfArray];
		for (int i = 0; i < lengthOfArray; i++) {
			r[i] = selectWord(minLengthOfWord, maxLengthOfWord);
		}
		return r;
	}
}