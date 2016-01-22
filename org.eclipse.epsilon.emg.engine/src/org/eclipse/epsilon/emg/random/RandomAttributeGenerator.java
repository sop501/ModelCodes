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

import java.util.Collection;

import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

/**
 * The Random Attribute Generator interface defines the different 
 * @author Goblin
 *
 */
public interface RandomAttributeGenerator<K extends CharacterSet> {
	
	public enum DefaultCharacterSet implements CharacterSet {
		ID("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"), 
		NUMERIC("1234567890"),
		LETTER("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
		LETTER_UPPER("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), 
		LETTER_LOWER("abcdefghijklmnopqrstuvwxyz"), 
		UPPER_NUM("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"), 
		LOWER_NUM("abcdefghijklmnopqrstuvwxyz1234567890"),
		ID_SYMBOL("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()_+-=[]\\{}|;':\"<>?,./"), 
		HEX_LOWER("abcdef1234567890"),
		HEX_UPPER("ABCDEF1234567890"); 
		private String characters;

		private DefaultCharacterSet(String characters) {
			this.characters = characters;
		}

		public String getCharacters() {
			return characters;
		}

	}
	
	public enum Distribution {
		Beta,
		Binomial,
		Cauchy,
		ChiSquare,
		Exponential,
		F,
		Gamma,
		Gaussian,
		HyperGeometric,
		Uniform,
		Pascal,
		Poisson,
		T,
		Weibull,
		Zipf
	}
	
	
	public boolean nextBoolean() throws EolRuntimeException;
	
	public int nextIngeter(int upper) throws EolRuntimeException;
	public long nextLong(long upper) throws EolRuntimeException;
	public double nextDobule(double upper) throws EolRuntimeException;
	
	public int nextIngeter(int lower, int upper) throws EolRuntimeException;
	public long nextLong(long lower, long upper) throws EolRuntimeException;
	public double nextDobule(double lower, double upper) throws EolRuntimeException;
	
	/**
	 * Generates a value using the configured distribution
	 * @param Distribution
	 * @return
	 */
	public double nextValue() throws EolRuntimeException;
	
	public double nextBinomialValue(int numberOfTrials, double probabilityOfSuccess) throws EolRuntimeException;
	public double nextExponentialValue(double mean) throws EolRuntimeException;
	// TODO add more methods for other distributions
	
	/**
	 * Generates a random string of the given length using the specified 
	 * character set. Characters are picked from the set using a uniform
	 * distribution.
	 * @param charSet
	 * @param length
	 * @return
	 */
	public String nextString(String charSet, int length);
	
	/**
	 * Generates a random string of the given length using the specified 
	 * character set formatted in <a href="https://en.wikipedia.org/wiki/CamelCase">
	 * CameCase</a> format. Characters are picked from the LETTER set using a
	 * uniform distribution.
	 *
	 * @param length the length
	 * @param minWordLenght the minimum word length
	 * @return the string
	 * @throws EolRuntimeException 
	 */
	public String nextCamelCaseString(int length, int minWordLength) throws EolRuntimeException;
	
	/**
	 * Generates a random URI that complies to:
	 * http:[//host[:port]][/]path[?query][#fragment]
	 * 
	 * The scheme is The host is generated from a random string and uses a top-level domain.
	 * The optional parameters will add additional information to the URI.
	 * The number of paths and queries are random between 1 and 4.
	 * @param addPort
	 * @param addPath
	 * @param addQuery
	 * @param addFragment
	 * @return
	 */
	public String nextHttpURI(boolean addPort, boolean addPath, boolean addQuery, boolean addFragment) throws EolRuntimeException;
	
	/**
	 * Generates a random URI that complies to:
	 * scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
	 * 
	 * The scheme is randomly selected from:  http, ssh and ftp.
	 * For ssh and ftp, a user and pasword are randomly generated. 
	 * The host is generated from a random string and uses a top-level domain.
	 * The optional parameters will add additional information to the URI.
	 * The number of paths and queries are random between 1 and 4.
	 * @param addPort
	 * @param addPath
	 * @param addQuery
	 * @param addFragment
	 * @return
	 */
	public String nextURI(boolean addPort, boolean addPath, boolean addQuery, 
			boolean addFragment) throws EolRuntimeException;
	
	/**
	 * Generates a random URI. The port, path, query and fragment are added
	 * randomly.
	 * @return
	 */
	public String nextURI() throws EolRuntimeException;
	
	/**
	 * Returns a single object selected randomly from the Collection c using 
	 * a uniform distribution.
	 * @param list the list
	 * @return the t
	 */
	//public String nextFromCollection(Collection<String> c) throws EolRuntimeException;
	
	public Object nextFromCollection(Collection<?> c);
	
	/**
	 * Returns a single objects selected randomly from the list using 
	 * a uniform distribution. The listID must be the name of a parameter in the
	 * launch configuration. The value of the parameter can be either a CSV list
	 * of strings or the name of a file. The name of the file should be full
	 * path and each line in the file is considered a separate item. All items
	 * are treated as strings, it is the responsibility of the caller to do the
	 * appropriate cast.
	 *   
	 * @param listID the listID
	 * @return the t
	 */
	public String nextFromList(String listID) throws EolRuntimeException;
	
	/**
	 * The list is treated as a sample without replacement, i.e. each call
	 * will return a unique member of the list. Throws an exception if list is
	 * empty or if the method is invoked more times than the number of items
	 * in the list.
	 * @param listID
	 * @return
	 * @throws EolRuntimeException
	 */
	public String nextFromListAsSample(String listID) throws EolRuntimeException;
	
	
	/**
	 * Returns an array of k objects selected randomly from the Collection c
	 * using a uniform distribution.
	 *
	 * Sampling from c is without replacement; but if c contains identical
	 * objects, the sample may include repeats. If all elements of c are
	 * distinct, the resulting object collection represents a Simple Random
	 * Sample of size k from the elements of c.
	 */
	public Object[] nextSample(Collection<?> c, int k) throws EolRuntimeException;
	
	/**
	 * Returns an array of k objects selected randomly from the list using a
	 * uniform distribution.
	 * 
	 * The listID must be the name of a parameter in the launch configuration.
	 * The value of the parameter can be either a CSV list of strings or the
	 * name of a file. The name of the file should be full path and each line in
	 * the file is considered a separate item.
	 *
	 * Sampling from the list is without replacement; but if the list contains
	 * identical objects, the sample may include repeats. If all elements of the
	 * list are distinct, the resulting object collection represents a Simple
	 * Random Sample of size k from the elements of c.
	 */
	public String[] nextSample(String listID, int k) throws EolRuntimeException;
	
}
