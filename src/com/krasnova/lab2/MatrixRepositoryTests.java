package com.krasnova.lab2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MatrixRepositoryTests {

	@Test
	public void testFileSave() {
		//Data preparation
		List<List<Integer>> matrix = new ArrayList<>(4);
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,2,3,4)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(11,12,31,41)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,-21,1,0)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(2,1,5,1)));
		
		//Test
		FileRepository.save(new Matrix(matrix), "E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab2\\qwerty.txt");
		Matrix savedMatr = null;
		try {
			savedMatr = FileRepository.load("E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab2\\qwerty.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertNotNull(savedMatr);
		for(int i = 0; i < matrix.size(); i++){
			assertEquals(matrix.size(), savedMatr.getElements().size());
			for(int j = 0; j< matrix.get(i).size(); j++){
				assertEquals(matrix.get(i).get(j), savedMatr.getElements().get(i).get(j));
			}
		}	
	}
	
	@Test(expected  = FileNotFoundException.class)
	public void testFileRead_FileNotFound() throws FileNotFoundException, RuntimeException {
		//Data preparation
		List<List<Integer>> matrix = new ArrayList<>(4);
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,2,3,4)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(11,12,31,41)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,-21,1,0)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(2,1,5,1)));
		
		//Test		
		Matrix savedMatr = null;		
		savedMatr = FileRepository.load("E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab2\\qwerty1.txt");
		//Assert
		assertNotNull(savedMatr);
		for(int i = 0; i < matrix.size(); i++){
			assertEquals(matrix.size(), savedMatr.getElements().size());
			for(int j = 0; j< matrix.get(i).size(); j++){
				assertEquals(matrix.get(i).get(j), savedMatr.getElements().get(i).get(j));
			}
		}	
	}
	
	@Test(expected  = FileNotFoundException.class)
	public void testDeserialization_FileNotFound() throws RuntimeException, IOException {
		//Data preparation
		List<List<Integer>> matrix = new ArrayList<>(4);
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,2,3,4)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(11,12,31,41)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,-21,1,0)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(2,1,5,1)));
		
		//Test		
		Matrix savedMatr = null;		
		savedMatr = SerializeRepository.load("E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab2\\qwerty1.txt");
		//Assert
		assertNotNull(savedMatr);
		for(int i = 0; i < matrix.size(); i++){
			assertEquals(matrix.size(), savedMatr.getElements().size());
			for(int j = 0; j< matrix.get(i).size(); j++){
				assertEquals(matrix.get(i).get(j), savedMatr.getElements().get(i).get(j));
			}
		}	
	}
	
	
	@Test
	public void testSerialization() {
		//Data preparation
		List<List<Integer>> matrix = new ArrayList<>(4);
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,2,3,4)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(11,12,31,41)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(1,-21,1,0)));
		matrix.add( new ArrayList<Integer> ( Arrays.asList(2,1,5,1)));
		
		//Test
		SerializeRepository.save(new Matrix(matrix), "E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab2\\qwertySerialization.txt");
		Matrix savedMatr = null;
		try {
			savedMatr = SerializeRepository.load("E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab2\\qwertySerialization.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertNotNull(savedMatr);
		for(int i = 0; i < matrix.size(); i++){
			assertEquals(matrix.size(), savedMatr.getElements().size());
			for(int j = 0; j< matrix.get(i).size(); j++){
				assertEquals(matrix.get(i).get(j), savedMatr.getElements().get(i).get(j));
			}
		}
		
	}


}
