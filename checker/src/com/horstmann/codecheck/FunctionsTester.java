package com.horstmann.codecheck;

public class FunctionsTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Functions fn = new Functions(1231231);
		/*
		int[] r = fn.randomIntArray(20, -30, 10);
		printOutIntArray(r);
		
		r = fn.selectIntArray(20, -30, 10);
		printOutIntArray(r);
		
		printOutDoubleArray(fn.randomDoubleArray(20, -30, -10));
		*/
		/*
		for (int i = 0; i < 3; i++) {
			String w = fn.selectWord(12, 15);
			System.out.println(w);
		}
		*/
		
		String[] words = fn.randomStringArray(10, 2, 8, 7);
		printOut(words);
	}

	private static void printOut(Object[] array) {
		for (Object i : array)
			System.out.println(i.toString() + " ");
		System.out.println();
	}
	
	private static void printOutIntArray(int[] intArray) {
		for (int i : intArray)
			System.out.print(i + " ");
		System.out.println();
	}
	
	private static void printOutDoubleArray(double[] doubleArray) {
		for (double i : doubleArray)
			System.out.print(i + " ");
		System.out.println();
	}
}
