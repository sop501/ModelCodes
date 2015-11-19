package org.eclipse.epsilon.emg.operationContributors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.epsilon.emg.RandomGenerator;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.contributors.IterableOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

public class CollectionOperationContributor extends OperationContributor{

	Collection targetCollection;
	IterableOperationContributor contributor= new IterableOperationContributor();
	RandomGenerator random;
	public CollectionOperationContributor(){
		if(target instanceof Collection<?>){
			targetCollection = (Collection) target;// change the target to a collection
		}
		random= new RandomGenerator();
	}
	public CollectionOperationContributor(RandomGenerator rand){ 
		if(target instanceof Collection<?>){
			targetCollection = (Collection) target;// change the target to a collection
		}
		random= rand;
	}
	@Override
	public boolean contributesTo(Object target) {
		return target instanceof Collection<?>;
		//return true;
	}
	
		
		
		//check if there is a seed
		/*if(context.getFrameStack().contains("seed")){
			//seed = context.getFrameStack().get("seed").getValue();
			seed=(int) context.getFrameStack().get("seed").getValue();
		}/*
		else{
			//we create a new seed and add it to the context
			seed=(int) System.currentTimeMillis();
			context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
			//seed= new Variable("seed",seedValue,null);
			//context.getFrameStack().put(seed);
		}*/
		//global random or local random; performance or high predictability
		//context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
		
		/*String letter = new String("abcdefghijklmnopqrstuvwxyz");
		String capitalLetter = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String alphabet = 
		        new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		*/
		//IterableOperationContributor
		
		
		
		
		public Object randomD() throws EolRuntimeException {
			//target <- Student.all
			
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			int cSize=targetCollection.size();
			contributor.setTarget(targetCollection);
			return contributor.at(random.nextInt(cSize));
			//targetCollection.
			
			//IterableOperationContributor contributor= new IterableOperationContributor(targetCollection);
			//Collection<Object> out = contributor.createCollection(); // the output collection
			//Variable seed ;//seed
			//Variable limit;//limit of the search in the target collection
			
			/*//Variable size;// size of the return collection
			int seedValue,limitValue,sizeValue;//values of the variables

			
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seedValue=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seedValue=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limitValue=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limitValue=cSize;
				//context.getFrameStack().put(Variable.createReadOnlyVariable("limit", limitValue));
				//limit= new Variable("limit",limitValue,null);
				//context.getFrameStack().put(limit);
			}
			
			//check if there is a required size for the output collection
			if(context.getFrameStack().contains("sizes")){
				//size = context.getFrameStack().get("sizes");
				sizeValue = (int) context.getFrameStack().get("sizes").getValue();
			}
			else{
				Random random= new Random(seedValue);
				sizeValue = random.nextInt(limitValue);
				//sizeValue =1;
				//context.getFrameStack().put(Variable.createReadOnlyVariable("sizes", sizeValue));
				//size= new Variable("limit",sizeValue,null);
				//context.getFrameStack().put(size);
			}	*/	
			
			//Collection<Object> out = contributor.createCollection(); // the output collection
			
			
			//the if statement ensures the limit is less than the size of the input
			//its a criteria to see if no deletion has occur
			/*if(limitValue<=cSize){
				return contributor.at(random.nextInt(limitValue));
			}
			else{
				//E.g when items have been deleted
				Random rand = new Random(seedValue);
				return contributor.at(rand.nextInt(cSize));
			}*/
			
			//return deterministicRandomBase(sizeValue,seedValue,limitValue);	
			//return deterministicRandomBase(3,1000,5);	
			
		}
		public Object get(int num){
			return contributor.at(num);
			//return num;
		}
		public Collection<?> randomD(int size) throws EolRuntimeException {
			//target <- Student.all
			Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			if(size<0){
				System.err.println("size must not be less than 0");
				return null;
			}
			int  seed,limitValue;//values of the variables

			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limitValue=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limitValue=targetCollection.size();
				//limit different for each collections
				//context.getFrameStack().put(Variable.createReadOnlyVariable("limit", limitValue));
				//limit= new Variable("limit",limitValue,null);
				//context.getFrameStack().put(limit);
			}	
			/*//check if there is a general size
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seed=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				seed= (int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}*/
			
			
			
			return deterministicRandomBase(size,limitValue);
			
		}
		public Collection<?> randomD(int minSize,int maxSize) throws EolRuntimeException {
			
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			
			int seed,limit;
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				seed = (int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//Random random = new Random(seed);
				//we create a new seed and add it to the context
				seed = (int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			if(maxSize-minSize<0 && maxSize!= -1){
				System.err.println("maximum size must not be less than minimum size");
				return null;
			}
			if(minSize<0){
				System.err.println("minimum size must not be less than 0");
				return null;
			}
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seed=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seed=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limit=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limit=targetCollection.size();
				
			}	
			int size;
			//Random random= new Random(seed);
			if(maxSize==-1) size=random.nextInt()+minSize;
			else size=random.nextInt((maxSize-minSize)+1)+minSize;
							
			
			return deterministicRandomBase(size,limit);		
			
		}
		protected Collection<?> deterministicRandomBase(int size,int limit) throws EolRuntimeException {
			//target <- Student.all
			/*
			 * *this method generates a deterministic collection as output
			 * the limit is used in case new things has been added to the collection
			 * so we stop at the previous size of the collection
			 */
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			contributor.setTarget(targetCollection);
			Collection<Object> out = contributor.createCollection(); // the output collection
			
			//Random random=new Random(seed);
			//the if statement ensures the limit is less than the size of the input
			//its a criteria to see if no deletion has occur
			if(limit<=targetCollection.size()){
				for(int i=0;i<size;i++)
					out.add(contributor.at(random.nextInt(limit)));
				//System.out.println(out.toString());
			}
			else{
				//E.g when items have been deleted
				Random rand = new Random();
				int temp;
				int cSize=targetCollection.size();
				for(int i=0;i<size;i++){
					temp =random.nextInt(limit);
					if(temp<=cSize)
						out.add(contributor.at(temp));
					else
						out.add(contributor.at(rand.nextInt(cSize)));
				}
					
				
			}
			return out;
			
			
		}
		public Object uniqueRandom(int size) throws EolRuntimeException {
			if(target instanceof Collection<?> ){
				targetCollection = (Collection) target;// change the target to a collection
				if( size> targetCollection.size()){
					System.err.println("size is greater than the number of objects!");
					return null;
				}
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			contributor.setTarget(targetCollection);
			
			Collection<Object> out = contributor.createCollection(); // the output collection
			List<Integer> num= new ArrayList<Integer>();
			int tSize =targetCollection.size();
			if(tSize < (size*2)){
				for(int i=0;i<tSize;num.add(i++));
				Collections.shuffle(num, random);
				for(int i=0;i<size;i++)
					out.add(contributor.at(num.get(i)));
			}
			else{
				Set<Integer> numb= new HashSet();
				while(numb.size()<=size){
					numb.add(random.nextInt(tSize));
				}
				Object[] number;
				number=numb.toArray();
				for(int i=0;i<size;i++)
					out.add(contributor.at((int) number[i]));
			}
			//Random random=new Random(seed);
			//the if statement ensures the limit is less than the size of the input
			//its a criteria to see if no deletion has occur
			/*int limit=0;
			if(limit<=targetCollection.size()){
				for(int i=0;i<size;i++)
					out.add(contributor.at(random.nextInt(limit)));
				//System.out.println(out.toString());
			}
			else{
				//E.g when items have been deleted
				Random rand = new Random();
				int temp;
				int cSize=targetCollection.size();
				for(int i=0;i<size;i++){
					temp =random.nextInt(limit);
					if(temp<=cSize)
						out.add(contributor.at(temp));
					else
						out.add(contributor.at(rand.nextInt(cSize)));
				}
					
				
			}*/
			return out;
			
		}
		public Object uniqueInteger(int size,int limit) throws EolRuntimeException {
			return null;
		}
		public Object uniformRandom() throws EolRuntimeException {
			//target <- Student.all
			//Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			int cSize=targetCollection.size();
			if(cSize<1){
				return null;
			}
			else if(cSize<5){
				return randomD();
			}
			else{
				int mean= cSize/2;
				int variance= cSize/5;
				return uniformRandom(mean,variance);
			}
			
		}
		public Object uniformRandom(int mean,int variance) throws EolRuntimeException {
			//target <- Student.all
			//Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			int cSize=targetCollection.size();
			int index=-1;
			double ind;
			while(index<0||index>=cSize){
				ind=random.nextGaussian()*variance+mean;
				index= (int)ind;
			}
			contributor.setTarget(targetCollection);
			//System.out.println(mean+"  "+variance);
			//System.out.println("ght  "+(random.nextGaussian()*variance+mean));
			return contributor.at(index);
			
		}
		
		/*public String generateString(int min,int max) throws EolRuntimeException {
			if(max<min) return "";
			String result="";
			int ind= random.nextInt(max-min)+min;
			for (int i=0; i<ind; i++) //12
			    result = result + letter.charAt(random.nextInt(26));
			return result;		
		}
		public String generateCapitalString(int min,int max) throws EolRuntimeException {
			if(max<min) return "";
			String result="";
			int ind= random.nextInt(max-min)+min;
			for (int i=0; i<ind; i++) //12
			    result = result + capitalLetter.charAt(random.nextInt(26));
			return result;		
		}
		public String generateAlphaString(int min,int max) throws EolRuntimeException {
			
				if(max<min) return null;
				String result="";
				int ind= random.nextInt(max-min)+min;
				for (int i=0; i<ind; i++)
				    result = result + alphabet.charAt(random.nextInt(26));
				return result;				
		}
		public int generateInteger(int min, int max) throws EolRuntimeException {
			if(max<=min)
				return max;
			return random.nextInt(max-min)+min;				
		}
		public int generateUniformInteger(int min, int max) throws EolRuntimeException {
			if(max-min<6)
				return generateInteger(min,max);
			int diff=max-min;
			int mean =(diff/2)+min;
			int variance=diff/6;
			return generateUniformInteger(min,max,mean,variance);				
		}
		public int generateUniformInteger(int min, int max, int mean, int variance) throws EolRuntimeException {
			if(max<=min||mean+variance>max||mean-variance<min||variance<0)
				return 0;
			int index=min-1;
			double ind;
			while(index<min||index>max){
				ind=random.nextGaussian()*variance+mean;
				index= (int)ind;
			}
			return index;				
		}
		public float generateReal(int min,int max) throws EolRuntimeException {
			int diff=max-min;
			if(max<=min)
				return max;
			return random.nextFloat()*diff+min;	
			
		}
		public float generateUniformReal(int min, int max) throws EolRuntimeException {
			if(max-min<6)
				return generateReal(min,max);
			int diff=max-min;
			int mean =(diff/2)+min;
			int variance=diff/6;
			return generateUniformReal(min,max,mean,variance);				
		}
		public float generateUniformReal(int min, int max, int mean, int variance) throws EolRuntimeException {
			if(max<=min||mean+variance>max||mean-variance<min||variance<0)
				return 0;
			float index=min-1;
			double ind;
			while(index<min||index>max){
				ind=random.nextGaussian()*variance+mean;
				index= (float) ind;
			}
			return index;					
		}
		public boolean generateBoolean() throws EolRuntimeException {
			if (random.nextInt(2)==0)
				return true;	
			return false;
			
		}
		public boolean generateBoolean(int scale) throws EolRuntimeException {
			if(scale<1||scale>10) return generateBoolean();
			if (random.nextInt(10)<scale)
				return true;	
			return false;
			
		}
		 
		/*public Collection<?> deterministicRandomSize(int minSize,int maxSize) throws EolRuntimeException {
			//target <- Student.all
			Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			if(maxSize-minSize<0 && maxSize!= -1){
				System.err.println("maximum size must not be less than minimum size");
				return null;
			}
			int seedValue,limitValue;//values of the variables
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seedValue=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seedValue=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limitValue=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limitValue=targetCollection.size();
				
			}	
			Random random= new Random(seedValue);
			int size;
			if(maxSize==-1) size=random.nextInt()+minSize;
			else size=random.nextInt(maxSize-minSize)+minSize;
			
			return deterministicRandomBase(size,seedValue,limitValue);				
			
		}
		
		public Collection<?> deterministicRandomSize(int size) throws EolRuntimeException {
			//target <- Student.all
			Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			if(size<0){
				System.err.println("size must not be less than 0");
				return null;
			}
			int seedValue,limitValue;//values of the variables
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seedValue=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seedValue=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limitValue=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limitValue=targetCollection.size();
				
			}	
			
			return deterministicRandomBase(size,seedValue,limitValue);				
			
		}*/
		
		
	
	

}
