/**
 * 
 */
package com.krasnova.lab3;


import java.io.FileNotFoundException;
import java.util.HashMap;


/**
 * @author admin
 *
 */
public class FrequencyDictionary extends HashMap<Character,Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7967284373372904676L;
	private String text;
	private IRepository repository;
	
	
	public String getText(){
		return text;
	}
	
	public void setText(String p_text){
		this.text = p_text;
	}
	
	public FrequencyDictionary(String p_text,IRepository p_repository){
		super();
		this.text = p_text;
		this.repository = p_repository;
		for (Character i = 'a'; i<='z'; i++)
            put(i, 0);
	}
	
	public FrequencyDictionary(IRepository p_repository){
		super();
		this.text = "";
		this.repository = p_repository;
		for (Character i = 'a'; i<='z'; i++)
            put(i, 0);
	}
	
	@Override
	public String toString(){
		int countLetters = text.length();
		StringBuilder sBuilder = new StringBuilder();
		
		for (Character k : this.keySet()) {
			sBuilder.append(k)
            	.append(" -> ")
            	.append(((double)get(k)) / countLetters)
            	.append ("%")
            	.append(System.lineSeparator());
		}		
		return sBuilder.toString();
	}
	
	public Integer put(Character key){
		key = Character.toLowerCase(key);
		if (containsKey(key)){
			Integer oldValue = get(key) +1;
			replace(key,oldValue );
		} 
		return get(key);
	}	
	
	public void buildDictionary(){
		Integer oldValue;
		String upperText = text.toLowerCase();
		for(char c : upperText.toCharArray() ){
			if (containsKey(c)){
				oldValue = get(c) +1;
				replace(c,oldValue );
			}
		}
	}
	
	public void read(String fileName)throws FileNotFoundException, RuntimeException{
		this.text = repository.read(fileName);
	}
}
