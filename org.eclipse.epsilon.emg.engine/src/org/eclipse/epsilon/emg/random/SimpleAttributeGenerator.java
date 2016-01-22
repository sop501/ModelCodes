/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Saheed Popoola - aditional functionality
 *     Horacio Hoyos - aditional functionality
 ******************************************************************************/
package org.eclipse.epsilon.emg.random;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

/**
 * The simplest implementation of the RandomAttributeGenerator interface.
 * All attributes are generated as strings. It is the responsibility of the 
 * calling class/method to do the appropriate conversion. This classes uses
 * the Apache Commons Math RandomDataGenerator.
 *
 */
public class SimpleAttributeGenerator extends OperationContributor
		implements RandomAttributeGenerator<RandomAttributeGenerator.DefaultCharacterSet> {
			
			
	/**
	 * Be default we use a normal distribution
	 */
	private Distribution globalDistribution = Distribution.Uniform;
	private final RandomDataGenerator generator = new RandomDataGenerator();
	private double firstArg;
	private double secondArg;
	private final IEolContext context;
	private Map<String, List<Integer>> listSamples;
	

	public SimpleAttributeGenerator(IEolContext context) {
		super();
		this.context = context;
	}
	
	public SimpleAttributeGenerator(IEolContext context, int seed) {
		super();
		int[] seeds = new int[1];
		seeds[0] = seed;
		this.context = context;
		this.generator.reSeed(RandomGeneratorFactory.convertToLong(seeds));
	}
	
	@Override
	public boolean contributesTo(Object target) {
		return target instanceof Object;
	}

	public void useBinomialDistribution(int numberOfTrials, double probabilityOfSuccess) {
		this.globalDistribution = Distribution.Binomial;
		this.firstArg = numberOfTrials;
		this.secondArg = probabilityOfSuccess;
	}
	
	public void useExponentialDistribution(double mean) {
		this.globalDistribution = Distribution.Exponential;
		this.firstArg = mean;
	}
	
	@Override
	public boolean nextBoolean() throws EolRuntimeException {
		return generator.getRandomGenerator().nextBoolean();
	}
	
	@Override
	public int nextIngeter(int upper) throws EolRuntimeException {
		
		return nextIngeter(0, upper);
	}

	@Override
	public long nextLong(long upper) throws EolRuntimeException {

		return nextLong(0, upper);
	}

	@Override
	public double nextDobule(double upper) throws EolRuntimeException {

		return nextDobule(0, upper);
	}


	@Override
	public int nextIngeter(int lower, int upper) throws EolRuntimeException {
		
		int value = 0;
		try {
			value = generator.nextInt(lower, upper);
		} catch (NumberIsTooLargeException e) {
			EolRuntimeException.propagate(e);
		}
		return value;
	}

	@Override
	public long nextLong(long lower, long upper) throws EolRuntimeException {
		
		long value = 0;
		try {
			value = generator.nextLong(lower, upper);
		} catch (NumberIsTooLargeException e) {
			EolRuntimeException.propagate(e);
		}
		return value;
	}
	
	@Override
	public double nextDobule(double lower, double upper) {
		
		double diff = upper-lower;
		if (diff == 0) {
			return lower;
		}
		if(upper<lower) {
			return generator.getRandomGenerator().nextDouble()*diff + upper;
		}
		return generator.getRandomGenerator().nextDouble()*diff + lower;
	}


	@Override
	public double nextValue() {
		switch(globalDistribution) {
			case Binomial:
				return nextBinomialValue((int) firstArg, secondArg);
			case Exponential:
				return nextExponentialValue(firstArg);
			case Uniform:
				return generator.nextUniform(firstArg, secondArg);
			default:
				return 0;
		}
	}

	@Override
	public double nextBinomialValue(int numberOfTrials, double probabilityOfSuccess) {
		return generator.nextBinomial((int) firstArg, secondArg);
	}
	
	@Override
	public double nextExponentialValue(double mean) {
		return generator.nextExponential(firstArg);
	}
	
	
	@Override
	public String nextString(String charSet, int length) {
		String chars = "";
		for (DefaultCharacterSet cs : DefaultCharacterSet.values()) {
	        if (cs.name().equals(charSet)) {
	        	chars = cs.getCharacters();
	        }
	    }
		int upper = chars.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(generator.nextInt(0, upper-1)));
		}
		return sb.toString();
	}
	
	@Override
	public String nextCamelCaseString(int length, int minWordLength) throws EolRuntimeException {
		
		StringBuilder sb = new StringBuilder();
		String upper = DefaultCharacterSet.LETTER_UPPER.getCharacters();
		int upperLenght = upper.length() -1;
		// Pick the first word length
		int remaning = length;
		do {
			sb.append(upper.charAt(nextIngeter(upperLenght)));
			int lastLength = nextIngeter(minWordLength, remaning-minWordLength) + 1; // +1 for the Capital
			sb.append(nextString("LETTER_LOWER", lastLength));
			remaning -= lastLength;
		} while (remaning > minWordLength);
		sb.append(upper.charAt(nextIngeter(upperLenght)));
		sb.append(nextString("LETTER_LOWER", remaning-1));
		return sb.toString();
	}

	@Override
	public Object nextFromCollection(Collection<?> c) {
		
		int upper = c.size()-1;
		int index = 0;
		try {
			index = nextIngeter(0, upper);
		} catch (NumberFormatException | EolRuntimeException e) {
			// Should never get here
			e.printStackTrace();
		}
		Object[] objects = c.toArray();
		//String result = (String) objects[index];
		return objects[index];
	}

	@Override
	public String nextFromList(String listID) throws EolRuntimeException {
		// Get the list from the context
		String list = (String) context.getFrameStack().get(listID).getValue();
		List<String> valuesList = getValuesFromList(list);
		return (String) nextFromCollection(valuesList);
	}

	
	
	@Override
	public String nextFromListAsSample(String listID) throws EolRuntimeException {
		// Get the list from the context
		String list = (String) context.getFrameStack().get(listID).getValue();		
		List<String> valuesList = getValuesFromList(list);
		int size = valuesList.size();
		String[] values = new String[size];
		values = valuesList.toArray(values); 
		Map<String, List<Integer>> sampleList = getListSamples();
		List<Integer> index;
		if (sampleList.containsKey(listID)) {
			index = sampleList.get(listID);	
		} else {
			int[] indexArray = null;
			try {
				indexArray = generator.nextPermutation(size, size);
			} catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
				EolRuntimeException.propagate(e);
			}
			index = new ArrayList<Integer>();
			for (int i = 0; i < indexArray.length; i++) {
				index.add(indexArray[i]);
			}
		}
        String result = null;
		try {
			result = valuesList.get(index.remove(0));
		} catch (IndexOutOfBoundsException e) {
			EolRuntimeException.propagate(e);
		}
        return result;
	}

	@Override
	public Object[] nextSample(Collection<?> c, int k) throws EolRuntimeException {

		Object[] sample = null;
		try {
			sample = generator.nextSample(c, k);
		} catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
			EolRuntimeException.propagate(e);
		}
		return sample;
	}

	@Override
	public String[] nextSample(String listID, int k) throws EolRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private final String[] scheme = {"http", "ssh", "ftp"};
	private final String[] domain = {".com", ".org", ".net", ".int", ".edu", ".gov", ".mil"};
	
	@Override
	public String nextURI(boolean addPort, boolean addPath, boolean addQuery, boolean addFragment) throws EolRuntimeException {
		
		StringBuilder sb = new StringBuilder();
		// scheme
		String uriScheme = getRandomUriScheme();
		sb.append(uriScheme);
		sb.append("://");
		// user:password
		if (!uriScheme.equals("http")) {
			if (nextBoolean()) {
				sb.append(nextString("LETTER_LOWER", nextIngeter(6, 10)));
				if (nextBoolean()) {
					sb.append(":");
					sb.append(generator.nextSecureHexString(nextIngeter(6, 10)));
				}
				sb.append("@");
			}
		}
		// Host
		sb.append("www.");
		sb.append(nextString("LETTER", nextIngeter(6, 10)));
		sb.append(getRandomUriDomain());
		if (addPort) {
			sb.append(":");
			sb.append(nextIngeter(9999));
		}
		sb.append("/");
		if (addPath) {
			for (int i = 0; i < nextIngeter(1, 4); i++) {
				sb.append(nextString("LETTER_LOWER", nextIngeter(3, 6)));
				sb.append("/");
			}
		}
		if (addQuery) {
			String separator = "?";
			for (int i = 0; i < nextIngeter(1, 4); i++) {
				sb.append(separator);
				sb.append(nextString("LETTER_LOWER", nextIngeter(3, 5)));
				sb.append("=");
				sb.append(nextString("NUMERIC", nextIngeter(5, 8)));
				separator = "&";
			}
		}
		if (addFragment) {
			sb.append("#");
			sb.append(nextString("ID_SYMBOL", nextIngeter(1, 15)));
		}
		return sb.toString();
	}

	@Override
	public String nextURI() throws EolRuntimeException {
		
		return nextURI(nextBoolean(), nextBoolean(), nextBoolean(), nextBoolean());
	}

	@Override
	public String nextHttpURI(boolean addPort, boolean addPath, 
			boolean addQuery, boolean addFragment) throws EolRuntimeException {
		
		StringBuilder sb = new StringBuilder();
		// scheme
		sb.append("http");
		sb.append("://");
		// Host
		sb.append("www.");
		sb.append(nextString("LETTER", nextIngeter(6, 10)));
		sb.append(getRandomUriDomain());
		if (addPort) {
			sb.append(":");
			sb.append(nextIngeter(9999));
		}
		sb.append("/");
		if (addPath) {
			for (int i = 0; i < nextIngeter(1, 4); i++) {
				sb.append(nextString("LETTER_LOWER", nextIngeter(3, 6)));
				sb.append("/");
			}
		}
		if (addQuery) {
			String separator = "?";
			for (int i = 0; i < nextIngeter(1, 4); i++) {
				sb.append(separator);
				sb.append(nextString("LETTER_LOWER", nextIngeter(3, 5)));
				sb.append("=");
				sb.append(nextString("NUMERIC", nextIngeter(5, 8)));
				separator = "&";
			}
		}
		if (addFragment) {
			sb.append("#");
			sb.append(nextString("ID_SYMBOL", nextIngeter(1, 15)));
		}
		return sb.toString();
	}


	
	private String getRandomUriScheme() {
		return (String) nextFromCollection(Arrays.asList(scheme));
	}
	
	private String getRandomUriDomain() {
		return (String) nextFromCollection(Arrays.asList(domain));
	}

	
	/**
	 * @return the listSamples
	 */
	private Map<String, List<Integer>> getListSamples() {
		if (listSamples == null) {
			listSamples = new HashMap<String, List<Integer>>();
		}
		return listSamples;
	}
	
	private List<String> getValuesFromList(String list) throws EolRuntimeException {
		// TODO We asume URI/paths don't have commas
		String[] values = list.split(",");
		List<String> valuesList = null;
		if (values.length == 1) {
			// It should be a path
			File file = new File(list);
			if (file.isDirectory())
			   throw new EolRuntimeException("Given list path is not a valid file.");
			if (file.exists()){
				Scanner s = null;
				try {
					s = new Scanner(file);
				} catch (FileNotFoundException e) {
					EolRuntimeException.propagate(e);
				}
				if (s != null) {
					valuesList = new ArrayList<String>();
					while (s.hasNext()){
						valuesList.add(s.next());
					}
					s.close();
				}
			}
		}
		else {
			valuesList = Arrays.asList(values);
			
		}
		return valuesList;
	}


	
}
