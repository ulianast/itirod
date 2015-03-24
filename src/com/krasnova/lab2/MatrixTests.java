package com.krasnova.lab2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.*;
import org.junit.Test;

public class MatrixTests {

	@Test
	public void multiply_0Matrixes() {
		//Initialization
		List<List<Integer>> firstElements = new ArrayList<>(4);
		List<List<Integer>> secondElements = new ArrayList<>(4);
		List<List<Integer>> expectedElements = new ArrayList<>(4);
		for(int i=0; i < 4; i++){
			firstElements.add( new ArrayList<Integer> ( Arrays.asList(0,0,0,0)));
			secondElements.add( new ArrayList<Integer> ( Arrays.asList(0,0,0,0)));
			expectedElements.add( new ArrayList<Integer> ( Arrays.asList(0,0,0,0)));
		}		
		
		Matrix firstMatrix = new Matrix(firstElements);
		Matrix secondMatrix = new Matrix(secondElements);
		Matrix expectedMatrix = new Matrix(expectedElements);
		//Test
		
		Matrix rezult = firstMatrix.multiply(secondMatrix);
		
		
		//Assert
		for(int i = 0; i< 4;i++){
			for(int j = 0 ; j < 4; j++){
				assertEquals(expectedMatrix.getElements().get(i).get(j), rezult.getElements().get(i).get(j));
			}
		}		
	}
	

	
	@Test
	public void multiply_0with1Matrixes() {
		//Initialization
		List<List<Integer>> firstElements = new ArrayList<>(4);
		List<List<Integer>> secondElements = new ArrayList<>(4);
		List<List<Integer>> expectedElements = new ArrayList<>(4);
		for(int i=0; i < 4; i++){
			firstElements.add( new ArrayList<Integer> ( Arrays.asList(1,1,1,1)));
			secondElements.add( new ArrayList<Integer> ( Arrays.asList(0,0,0,0)));
			expectedElements.add( new ArrayList<Integer> ( Arrays.asList(0,0,0,0)));
		}		
		
		Matrix firstMatrix = new Matrix(firstElements);
		Matrix secondMatrix = new Matrix(secondElements);
		Matrix expectedMatrix = new Matrix(expectedElements);
		//Test
		
		Matrix rezult = firstMatrix.multiply(secondMatrix);
		
		
		//Assert
		for(int i = 0; i< 4;i++){
			for(int j = 0 ; j < 4; j++){
				assertEquals(expectedMatrix.getElements().get(i).get(j), rezult.getElements().get(i).get(j));
			}
		}		
	}

	@Test
	public void multiply_1with1Matrixes() {
		//Initialization
		List<List<Integer>> firstElements = new ArrayList<>(4);
		List<List<Integer>> secondElements = new ArrayList<>(4);
		List<List<Integer>> expectedElements = new ArrayList<>(4);
		for(int i=0; i < 4; i++){
			firstElements.add( new ArrayList<Integer> ( Arrays.asList(1,1,1,1)));
			secondElements.add( new ArrayList<Integer> ( Arrays.asList(1,1,1,1)));
			expectedElements.add( new ArrayList<Integer> ( Arrays.asList(4,4,4,4)));
		}		
		
		Matrix firstMatrix = new Matrix(firstElements);
		Matrix secondMatrix = new Matrix(secondElements);
		Matrix expectedMatrix = new Matrix(expectedElements);
		//Test
		
		Matrix rezult = firstMatrix.multiply(secondMatrix);
		
		
		//Assert
		for(int i = 0; i< 4;i++){
			for(int j = 0 ; j < 4; j++){
				assertEquals(expectedMatrix.getElements().get(i).get(j), rezult.getElements().get(i).get(j));
			}
		}		
	}
	@Test
	public void multiply_RandWithRandMatrixes() {
		//Initialization
		List<List<Integer>> firstElements = new ArrayList<>(4);
		firstElements.add( new ArrayList<Integer> ( Arrays.asList(1,2,3,4)));
		firstElements.add( new ArrayList<Integer> ( Arrays.asList(11,12,31,41)));
		firstElements.add( new ArrayList<Integer> ( Arrays.asList(1,-21,1,0)));
		firstElements.add( new ArrayList<Integer> ( Arrays.asList(2,1,5,1)));
		
		List<List<Integer>> secondElements = new ArrayList<>(4);
		secondElements.add( new ArrayList<Integer> ( Arrays.asList(-4,1,0,1,2)));
		secondElements.add( new ArrayList<Integer> ( Arrays.asList(1,12,5,11)));
		secondElements.add( new ArrayList<Integer> ( Arrays.asList(0,-1,10,1)));
		secondElements.add( new ArrayList<Integer> ( Arrays.asList(1,1,18,1)));
		
		List<List<Integer>> expectedElements = new ArrayList<>(4);
		expectedElements.add( new ArrayList<Integer> ( Arrays.asList(2,26,112,30)));
		expectedElements.add( new ArrayList<Integer> ( Arrays.asList(9,165,1108,215)));
		expectedElements.add( new ArrayList<Integer> ( Arrays.asList(-25,-252,-95,-229)));
		expectedElements.add( new ArrayList<Integer> ( Arrays.asList(-6,10,73,19)));
	
		Matrix firstMatrix = new Matrix(firstElements);
		Matrix secondMatrix = new Matrix(secondElements);
		Matrix expectedMatrix = new Matrix(expectedElements);
		//Test
		
		Matrix rezult = firstMatrix.multiply(secondMatrix);
		
		
		//Assert
		for(int i = 0; i< 4;i++){
			for(int j = 0 ; j < 4; j++){
				assertEquals(expectedMatrix.getElements().get(i).get(j), rezult.getElements().get(i).get(j));
			}
		}		
	}
	
	@Test
	public void multiply_100with100ArayMatrixes() {
		//Initialization
		List<List<Integer>> firstElements = new ArrayList<>(100);
		List<List<Integer>> secondElements = new ArrayList<>(100);
		Random rand = new Random();
		for(int i=0; i < 100; i++){
			ArrayList<Integer> firstLine = new ArrayList<Integer>(100);
			ArrayList<Integer> secondLine = new ArrayList<Integer>(100);
			for(int j = 0; j < 100; j++){
				firstLine.add(Integer.valueOf(rand.nextInt()));
				secondLine.add(Integer.valueOf(rand.nextInt()));
			}
			firstElements.add( firstLine);
			secondElements.add( secondLine);
		}		
		
		Matrix firstMatrix = new Matrix(firstElements);
		Matrix secondMatrix = new Matrix(secondElements);
		//Test
		
		firstMatrix.multiply(secondMatrix);
	}
	
	@Test
	public void multiply_100with100ListMatrixes() {
		//Initialization
		List<List<Integer>> firstElements = new LinkedList<>();
		List<List<Integer>> secondElements = new LinkedList<>();
		Random rand = new Random();
		for(int i=0; i < 100; i++){
			LinkedList<Integer> firstLine = new LinkedList<Integer>();
			LinkedList<Integer> secondLine = new LinkedList<Integer>();
			for(int j = 0; j < 100; j++){
				firstLine.add(Integer.valueOf(rand.nextInt()));
				secondLine.add(Integer.valueOf(rand.nextInt()));
			}
			firstElements.add( firstLine);
			secondElements.add( secondLine);
		}		
		
		Matrix firstMatrix = new Matrix(firstElements);
		Matrix secondMatrix = new Matrix(secondElements);
		//Test
		
		firstMatrix.multiply(secondMatrix);
	}
	
	@Test(expected  = RuntimeException.class)
	public void multiply_NotSoglasovanieMatrixes() {
		//Initialization
				List<List<Integer>> firstElements = new ArrayList<>(4);
				firstElements.add( new ArrayList<Integer> ( Arrays.asList(1,2,3,4,5,5)));
				firstElements.add( new ArrayList<Integer> ( Arrays.asList(11,12,31,41,5,5)));
				firstElements.add( new ArrayList<Integer> ( Arrays.asList(1,-21,1,0,5,5)));
				firstElements.add( new ArrayList<Integer> ( Arrays.asList(2,1,5,1,5,5)));
				
				List<List<Integer>> secondElements = new ArrayList<>(4);
				secondElements.add( new ArrayList<Integer> ( Arrays.asList(-4,1,0,1)));
				secondElements.add( new ArrayList<Integer> ( Arrays.asList(1,12,5,11)));
				secondElements.add( new ArrayList<Integer> ( Arrays.asList(0,-1,10,1)));
				secondElements.add( new ArrayList<Integer> ( Arrays.asList(1,1,18,1)));
				
				Matrix firstMatrix = new Matrix(firstElements);
				Matrix secondMatrix = new Matrix(secondElements);
				
				Matrix rezult = firstMatrix.multiply(secondMatrix);
		
	}
}
