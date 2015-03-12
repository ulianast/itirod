/**
 * 
 */
package com.krasnova.lab2;
import java.io.Serializable;
import java.util.*;

/**
 * @author admin
 *
 */
public class Matrix implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6872691646283012877L;
	private List<List<Integer>> elements;
	
	public List<List<Integer>> getElements(){
		return elements;
	}
	
	public void setElements(List<List<Integer>> p_elements){
		elements = p_elements;
	}
	
	public Matrix(List<List<Integer>> p_elements){
		this.elements = p_elements;
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getElements().size());
		sb.append("\n");                                    
        for(int i = 0; i < this.getElements().size(); i++ ){
        	sb.append(this.getElements().get(i).size());
        	sb.append("\n");
            for(int j = 0; j < this.getElements().get(i).size(); j++){
            	sb.append(this.getElements().get(i).get(j));
            	sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
	}
	
	public Matrix multiply( Matrix p_other){
		List<List<Integer>> table;
		
		if (ArrayList.class.isInstance( p_other.getElements()))
			 table = new ArrayList<>();
		else  
			table = new LinkedList<>();
		
		for (int i = 0; i < elements.size(); i++){
			List<Integer> tempList;
			
			if (ArrayList.class.isInstance( p_other.getElements()))
				 tempList = new ArrayList<Integer>();
			else  
				tempList = new LinkedList<Integer>();
           
            for(int j = 0; j < elements.size(); j++) {
                int number = 0;
                for (int k = 0; k < elements.get(i).size(); k++)
                    number += elements.get(i).get(k) * p_other.getElements().get(k).get(j);
                tempList.add(number);
            }
            table.add(tempList);
        }
		return new Matrix(table);
	}
}
