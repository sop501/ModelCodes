package org.eclipse.epsilon.emg.operationContributors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.epsilon.emg.RandomGenerator;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;
import org.eclipse.epsilon.eol.models.IModel;

public class ObjectOperationContributor extends OperationContributor {
	//IterableOperationContributor contributor= new IterableOperationContributor();
	RandomGenerator random;
	IModel model;
	Map<String,Collection> classGroup;
	public ObjectOperationContributor(IModel mod){
		random= new RandomGenerator();
		model=mod;
	}
	public ObjectOperationContributor(RandomGenerator rand,IModel mod){
		model=mod;
		random= rand;
		classGroup= new HashMap<String,Collection>();
		
	}
	public ObjectOperationContributor(RandomGenerator rand,IModel mod, Map classGr){
		model=mod;
		random= rand;
		classGroup=classGr;
	}
	@Override
	public boolean contributesTo(Object target) {
		return target instanceof Object;
	}
	public Object createInstance() throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException{
		return model.createInstance(target.toString());
		//System.out.println(target.toString());
		
	}
	public Collection getCreatedElements(String name){
		if(classGroup.containsKey(name))
			return classGroup.get(name);
		return null;
	}
	public Collection createInstance(int num) throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException{
		Collection objects = new ArrayList<>();
		if(num<1)
			return null;
		for(int i=0;i<num;i++){
			Object mod=model.createInstance(target.toString());
			objects.add(mod);
		}
		return objects;
		//return model.createInstance(target.toString());
		//System.out.println(target.toString());
		
	}
	public void linkFeature(String name,Object value){
		if(!(target instanceof EObject)) return;
		EObject tg = (EObject) target;
		EStructuralFeature feature=tg.eClass().getEStructuralFeature(name);
		if(feature.isMany()){
			Collection val =(Collection) tg.eGet(feature);
			if(value instanceof Collection)
				val.addAll((Collection) value);
			else
				val.add(value);
		}
		else
			setSingleProperty(name,value);
		//System.out.println(val.toString());
		//tg.eSet(feature, value);
	}
	public void setProperty(String name,Object value){
		
		if(target instanceof EObject){
			
			setSingleProperty(name,value);
		}
		else if(target instanceof Collection){
			if(value instanceof Collection)
				setCollectionValueProperty(name,value);
			else
				setCollectionProperty(name,value);
		}
	}
	protected void setSingleProperty(String name,Object value){
		EObject tg = (EObject) target;
		EStructuralFeature feature=tg.eClass().getEStructuralFeature(name);
		
		tg.eSet(feature, value);
	}
	//for collection of objects and single value
	protected void setCollectionProperty(String name,Object value){
		Collection tga = (Collection) target;
		for(Object i : tga){
			if(i instanceof EObject){
				EObject tg = (EObject) i;
				EStructuralFeature feature=tg.eClass().getEStructuralFeature(name);
				tg.eSet(feature, value);
			}
		}
		
	}
	//collection of objects and collection values
	protected void setCollectionValueProperty(String name,Object value){
		Collection tc = (Collection) target;
		Collection val = (Collection) value;
		Object[] values=val.toArray();
		if(tc.size()<=val.size()){
			int j=0;
			for(Object i : tc){
				if(i instanceof EObject){
					EObject tg = (EObject) i;
					EStructuralFeature feature=tg.eClass().getEStructuralFeature(name);
					tg.eSet(feature, values[j++]);
				}
			}
		}
		else{
			int j=0;
			int size=val.size();
			for(Object i : tc){
				if(i instanceof EObject){
					if(j>=size) j=0;
					EObject tg = (EObject) i;
					EStructuralFeature feature=tg.eClass().getEStructuralFeature(name);
					tg.eSet(feature, values[j++]);
				}
			}
			
		}
		
	}

	
	
	
	
	
	public String randomString() throws EolRuntimeException{
		return random.generateString();
	}
	public String randomString(int num) throws EolRuntimeException{
		return random.generateString(num);
	}
	public String randomString(int min,int max) throws EolRuntimeException {
		return random.generateString(min, max);
	}
	public Collection randomManyString(int num) throws EolRuntimeException{
		Collection string = new ArrayList<>();
		if(num<1) return null;
		for(int i=0;i<num;i++)
			string.add(random.generateString());
		return string;
	}
	public String randomCapitalString(int min,int max) throws EolRuntimeException {
		return random.generateCapitalString(min, max);
	}
	public String randomAlphaString(int min,int max) throws EolRuntimeException {
		return random.generateAlphaString(min, max);
	}
	public int randomInteger(int num) throws EolRuntimeException {
		return random.generateInteger(0,num);
	}
	public int randomInteger(int min, int max) throws EolRuntimeException {
		return random.generateInteger(min, max);
	}
	public int randomUniformInteger(int min, int max) throws EolRuntimeException {
		return random.generateUniformInteger(min, max);
	}
	public int randomUniformInteger(int min, int max, int mean, int variance) throws EolRuntimeException {
		return random.generateUniformInteger(min, max, mean, variance);
	}
	public float randomReal(int min,int max) throws EolRuntimeException {
		return random.generateReal(min, max);
	}
	public float randomUniformReal(int min, int max) throws EolRuntimeException {
		return random.generateUniformReal(min, max);
	}
	public float randomUniformReal(int min, int max, int mean, int variance) throws EolRuntimeException {
		return random.generateUniformReal(min, max, mean, variance);			
	}
	public boolean randomBoolean() throws EolRuntimeException {
		return random.generateBoolean();			
	}
	public boolean randomBoolean(int scale) throws EolRuntimeException {
		return random.generateBoolean(scale);
		
	}
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
			
		}*/
		 
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
		

