/**
 * 
 */
package com.krasnova.lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author admin
 *
 */
public class FileRepository{
	public static void save(Matrix p_matr, String p_fileName) throws RuntimeException{
		if (p_matr == null)
			return;
		
		File file = new File(p_fileName);
	    try {
	        if(!file.exists()){
	            file.createNewFile();
	        }
	        Writer out = new FileWriter(file.getAbsoluteFile());
	        try {
	            out.write(p_matr.toString());
	        } finally {
	            out.close();
	        }
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	public static Matrix load(String p_fileName) throws FileNotFoundException, RuntimeException {		 
		File file = new File(p_fileName);
	    if (!file.exists()){
	        throw new FileNotFoundException(file.getName());
	    }
	    
	    List<List<Integer>> matr = null;
		 
		try {
			BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
		    try {
		        String s; 
		        //read rows quantity
		        if ((s = in.readLine()) != null) {
		        	int matrSize = Integer.parseInt(s); 
		        	matr = new ArrayList<>(matrSize);
		        	for(int i = 0; i < matrSize; i++){
		        		if ((s = in.readLine()) != null) {
				        	matr.add(new ArrayList<Integer>(Integer.parseInt(s)));
				        	s = in.readLine();
				        	String[] elements = s.split(" ");
				        	for(String elem : elements){
				        		matr.get(i).add(Integer.parseInt(elem));
				        	}
		        		}else break;				        	
		        	}
		        }
		    } finally {
		         in.close();
		    }
		} catch(IOException e) {
		     throw new RuntimeException(e);
		}
		
		return new Matrix(matr);
	}

}
