/**
 * 
 */
package com.krasnova.lab3;

import java.io.FileNotFoundException;

/**
 * @author admin
 *
 */
public interface IRepository {
	String read(String fileName) throws FileNotFoundException, RuntimeException;
}
