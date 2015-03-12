/**
 * 
 */
package com.krasnova.lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author admin
 *
 */
public class SerializeRepository {
	public static void save(Matrix p_matr, String p_fileName) throws RuntimeException{
		if (p_matr == null)
			return;
		
		File file = new File(p_fileName);
	    try {
	        if(!file.exists()){
	            file.createNewFile();
	        }
	        ObjectOutputStream objectOutputStream = null;
	        try{
	        	objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
	        	objectOutputStream.writeObject(p_matr);
	        }finally{
	        	objectOutputStream.close();
	        }
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	public static Matrix load(String p_fileName) throws FileNotFoundException {		 
		File file = new File(p_fileName);
	    if (!file.exists()){
	        throw new FileNotFoundException(file.getName());
	    }
	    Matrix matrix = null;
	    ObjectInputStream objectInputStream = null; 
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(file));			
	        matrix = (Matrix)objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try{
				objectInputStream.close();	
			}catch(RuntimeException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return matrix;
	}

}

