package com.example.imapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregator {
	
	private final static String bannedWordFileName = "badwords.txt";
	List<String> bannedWords;
	
	public Aggregator(){
		bannedWords = loadFile(bannedWordFileName);
	}
	
	private Map<Tag,Integer> updateTagMapWithString(String inputString, Map<Tag,Integer> tagMap){
		String[] subStrings = inputString.split(" ");
		for(String subString:subStrings){
			try{
				Tag thisTag = Tag.valueOf(subString);
				int count = 0;
				if(tagMap.containsKey(thisTag)){
					count+= tagMap.get(thisTag);
				}
				tagMap.put(thisTag, count);
			}catch(IllegalArgumentException e){
				System.out.println("Not a tag");
			}
		}
		return tagMap;
	}
	
	public Map<Tag,Integer> generateTagMapFromFile(List<String> inputFile){
		Map<Tag,Integer> tagMap = new HashMap<Tag,Integer>();
		for(String thisString:inputFile){
			tagMap = updateTagMapWithString(thisString, tagMap);
		}
		return tagMap;
	}
	
	private int getNumberOfBannedWordsInLine(String line){
		String[] subStrings = line.split(" ");
		int count = 0;
		for(String thisString:subStrings){
			if(bannedWords.contains(thisString)){
				count++;
			}
		}
		return count;
	}
	
	public int getNumberOfBannedWords(List<String> inputFile){
		int count = 0;
		for(String thisLine:inputFile){
			count+=getNumberOfBannedWordsInLine(thisLine);
		}
		return count;
	}
	
	private List<String> loadFile(String filename){
		List<String> stringList = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
		
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
				    stringList.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return stringList;
	}
}
