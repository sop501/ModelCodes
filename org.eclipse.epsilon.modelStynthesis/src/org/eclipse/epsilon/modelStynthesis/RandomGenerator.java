package org.eclipse.epsilon.modelStynthesis;

import java.util.Collection;
import java.util.Random;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
public class RandomGenerator extends Random {
	protected final long seed = 16758904;
	String letter = new String("abcdefghijklmnopqrstuvwxyz");
	String capitalLetter = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	String alphabet = 
	        new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
	public RandomGenerator(){
		super.setSeed(seed);
	}
	public RandomGenerator(long seeds){
		//super.
		super.setSeed(seeds);
	}
	public String generateString() {
		return generateString(5,10);
	}
	public String generateString(int n) {
		return generateString(n,n);
	}
	public String generateString(int min,int max) {
		if(max<min) return "";
		String result="";
		int ind= nextInt(max-min)+min;
		for (int i=0; i<ind; i++) //12
		    result = result + letter.charAt(nextInt(26));
		return result;		
	}
	public String generateCapitalString(int min,int max){
		if(max<min) return "";
		String result="";
		int ind= nextInt(max-min)+min;
		for (int i=0; i<ind; i++) //12
		    result = result + capitalLetter.charAt(nextInt(26));
		return result;		
	}
	public String generateAlphaString(int min,int max) {
		
			if(max<min) return null;
			String result="";
			int ind= nextInt(max-min)+min;
			for (int i=0; i<ind; i++)
			    result = result + alphabet.charAt(nextInt(26));
			return result;				
	}
	public int generateInteger(int min, int max) {
		if(max<=min)
			return max;
		return nextInt(max-min)+min;				
	}
	public int generateUniformInteger(int min, int max) {
		if(max-min<6)
			return generateInteger(min,max);
		int diff=max-min;
		int mean =(diff/2)+min;
		int variance=diff/6;
		return generateUniformInteger(min,max,mean,variance);				
	}
	public int generateUniformInteger(int min, int max, int mean, int variance){
		if(max<=min||mean+variance>max||mean-variance<min||variance<0)
			return 0;
		int index=min-1;
		double ind;
		while(index<min||index>max){
			ind=nextGaussian()*variance+mean;
			index= (int)ind;
		}
		return index;				
	}
	public float generateReal(int min,int max) {
		int diff=max-min;
		if(max<=min)
			return max;
		return nextFloat()*diff+min;	
		
	}
	public float generateUniformReal(int min, int max) {
		if(max-min<6)
			return generateReal(min,max);
		int diff=max-min;
		int mean =(diff/2)+min;
		int variance=diff/6;
		return generateUniformReal(min,max,mean,variance);				
	}
	public float generateUniformReal(int min, int max, int mean, int variance)  {
		if(max<=min||mean+variance>max||mean-variance<min||variance<0)
			return 0;
		float index=min-1;
		double ind;
		while(index<min||index>max){
			ind=nextGaussian()*variance+mean;
			index= (float) ind;
		}
		return index;					
	}
	public boolean generateBoolean(){
		if (nextInt(2)==0)
			return true;	
		return false;
		
	}
	public boolean generateBoolean(int scale) {
		if(scale<1||scale>10) return generateBoolean();
		if (nextInt(10)<scale)
			return true;	
		return false;
		
	}

	public static void main(String[] args) {
		RandomGenerator test = new RandomGenerator();
		System.out.println(test.generateAlphaString(5, 8));
		// TODO Auto-generated method stub

	}

}
