/**
 * 
 */
package com.krasnova.lab3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author admin
 *
 */
public class FileRepository implements IRepository {

	/* (non-Javadoc)
	 * @see com.krasnova.lab3.IRepository#read(java.lang.String)
	 */
	@Override
	public String read(String fileName) throws FileNotFoundException, RuntimeException {
		StringBuilder sb = new StringBuilder();
	    
	    File file = new File(fileName);
	    if (!file.exists()){
	        throw new FileNotFoundException(file.getName());
	    }	 
	    try {
	        BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
	        try {
	            String s;
	            while ((s = in.readLine()) != null) {
	                sb.append(s);
	                sb.append("\n");
	            }
	        } finally {
	            in.close();
	        }
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	    return sb.toString();
	}

}
