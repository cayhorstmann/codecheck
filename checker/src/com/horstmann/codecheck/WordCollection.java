package com.horstmann.codecheck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordCollection {
	private String file = "/usr/share/dict/words";
	private int SIZE = 24;
	
	private ArrayList<String>[] words;
	private int total;
	private static WordCollection instance = null;
	
	@SuppressWarnings("unchecked")
	protected WordCollection() {
		words = new ArrayList[SIZE];
		total = 0;
		loadingWords();
	}
	
	public static WordCollection getInstance() {
		if (instance == null) 
			instance = new WordCollection();
		return instance;
	}
	
	private void loadingWords() {
		for (int i = 0; i < SIZE; i++) 
			words[i] = new ArrayList<String>();
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				int id = sCurrentLine.length();
				words[id].add(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 

		for (int i = 1; i < SIZE; i++)
			total += words[i].size();
	}
	
	public void reload() {
		if (total == 0)
			loadingWords();
	}
	
	public String getWord(int minLength, int maxLength, int id) {
		if (total == 0) reload();
		
		for (int i = minLength; i <= maxLength; i++) {
			if (id < words[i].size())
				return words[i].get(id);
			id -= words[i].size();
		}
		return "";
	}
	
	public int getTotal(int minLength, int maxLength) {
		if (total == 0) reload();
		
		int r = 0;
		for (int i = minLength; i <= maxLength; i++)
			r += words[i].size();
		return r;
	}
}
