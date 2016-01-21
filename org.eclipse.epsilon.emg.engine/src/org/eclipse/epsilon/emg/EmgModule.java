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
package org.eclipse.epsilon.emg;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emg.operationContributors.CollectionOperationContributor;
import org.eclipse.epsilon.emg.operationContributors.ObjectOperationContributor;
//import org.eclipse.epsilon.emg.operationContributors.ObjectOperationContributor;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.epl.EplModule;
import org.eclipse.epsilon.epl.execute.PatternMatchModel;

/**
 * The Emg Module is responsible for execution emg scripts. Emg scripts are used to generate EMF models.
 */
public class EmgModule extends EplModule implements IModule, IEolExecutableModule {
	
	/** The model generator. */
	//private ModelGenerator mod;
	
	/** The random generator */
	private RandomGenerator random;
	
	/** The seed used for random generation. */
	private int seed;
	
	private boolean useSeed;


	/** A maps to keep track of objects created by operations */
	private Map<String, Collection<Object>> classGroup= new HashMap<String, Collection<Object>>(); //

	//private EmfModel outputModel;
	
	/**
	 * Instantiates a new emg module.
	 */
	public EmgModule(){
		reset();
		random= new RandomGenerator();
	}
	
	/**
	 * @param seed the seed to set
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	
	/**
	 * @param useSeed the useSeed to set
	 */
	public void setUseSeed(boolean useSeed) {
		this.useSeed = useSeed;
	}
	
	/**
	 * Initialise the contributors
	 */
	private void preload() {
		context.setModule(this);
//		if(context.getFrameStack().contains("seed")){
//			seed= (int) context.getFrameStack().get("seed").getValue();
//		}
//		else{
//			seed=(int) System.currentTimeMillis();
//		}
		random.setSeed(seed);
		context.getOperationContributorRegistry().add(new CollectionOperationContributor(random));
		//EmfModel model= getModel();
		context.getOperationContributorRegistry().add(new ObjectOperationContributor(random, getModel(), classGroup));
	}
	
//	/**
//	 * Instantiate the generated model
//	 * @throws URISyntaxException If the generated model URI is incorrect
//	 * @throws EolModelLoadingException If the model can not be instantiated
//	 */
//	private void instantiateModel() throws EolModelLoadingException, URISyntaxException {
//		
//		EmfModel model= getModel();
//		String newFile;
//		if(context.getFrameStack().contains("output")){
//			newFile = getNewFilePath(model.getModelFile(), (String) context.getFrameStack().get("output").getValue());
//		}
//		else {
//			newFile=getFilePath(model.getModelFile())+ "New.ecore";
//		}
//		output = newFile;
//		outputModel = createEmfModel(model.getName()+"New", newFile,model.getModelFile(), false, true);
//		context.getModelRepository().removeModel(model);		// Delete a spurious model loaded by the launch configuration? 
//		context.getModelRepository().addModel(outputModel);
//		context.getOperationContributorRegistry().add(new ObjectOperationContributor(random, model, classGroup));
//	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.epl.EplModule#getMainRule()
	 */
	@Override
	public String getMainRule() {
		return "eplModule";
		
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.epl.EplModule#getImportConfiguration()
	 */
	@Override
	public HashMap<String, Class<?>> getImportConfiguration() {
		HashMap<String, Class<?>> importConfiguration = super.getImportConfiguration();
		importConfiguration.put("emg", EmgModule.class);
		return importConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.epl.EplModule#execute()
	 */
	@Override
	public Object execute() throws EolRuntimeException {	
		//long time= System.currentTimeMillis();
		preload();
//		try {
//			instantiateModel();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			EolRuntimeException.propagate(e);
//		}
//		if (outputModel != null) {
			executeOperations();
			prepareContext(context);
			execute(getPre(), context);
		
			EmgPatternMatcher patternMatcher = new EmgPatternMatcher();
			PatternMatchModel matchModel = null;
			try {
				int loops = 1;
				matchModel = patternMatcher.match(this);
				if (repeatWhileMatchesFound) {
						
					while (!matchModel.allContents().isEmpty()) {
						if (maxLoops != INFINITE) {
							if (loops == maxLoops) break;
						}
						matchModel = patternMatcher.match(this);
						loops++;
					}
				}
			}
			catch (Exception ex) {
				EolRuntimeException.propagate(ex);
			}	
			execute(getPost(), context);	
			//System.out.println("total time is: "+ (System.currentTimeMillis()-time));
			//System.out.println("model generation successful, seed used is "+seed);
			context.getModelRepository().getModels().get(0).store();
			System.out.println(getModel().getModelFile());
			context.getModelRepository().dispose();
			return matchModel;
//		}
//		return null;
	}
	
	/**
	 * Execute operations.
	 *
	 * @param operationNames the operation names
	 * @param outputModel the model
	 * @param ne the ne
	 * @return the emf model
	 * @throws EolModelElementTypeNotFoundException the eol model element type not found exception
	 * @throws EolRuntimeException the eol runtime exception
	 */
	protected void executeOperations() throws EolModelElementTypeNotFoundException, EolRuntimeException{
		
		//long time=System.currentTimeMillis();
		AnnotationBlock annotationBlock;
		String annotationName,operationName, guard;
		EClass eclass;
		for (Operation operation: getOperations()){
			if(operation.getName().equals("create")) {
				//get the class context
				eclass = getModel().classForName(operation.getContextType(context).getName());
				if(eclass.isAbstract() || eclass.equals(null))	{
					continue;	// Can't instantiate null or abstract classes
				}
				int instances = 1;
				operationName="";
				guard="";
				//get the annotations
				annotationBlock = operation.getAnnotationBlock();
				if(!(annotationBlock==null)){
					List<Object> annotationValues;
					for(Annotation annotation:annotationBlock.getAnnotations()){
						if(!(annotation.hasValue()))
							continue;	
						annotationName = annotation.getName();
						annotationValues = operation.getAnnotationsValues(annotationName, context);
						//search for instances to be created
						if(annotationName.equals("instances")){
							if (!annotationValues.isEmpty()) {
								Object val=annotationValues.get(0);
								if(val instanceof List){
									List valC = (List)val;
									if(valC.size()>1)
										instances = random.generateInteger(getInt(valC.get(0)), getInt(valC.get(1)));
									else
										instances= getInt(valC.get(0));
								}
								else
									instances = getInt( annotationValues.get(0));
							}
							if(instances<1)
								instances=1;
						}
						else if(annotationName.equals("name")){
							if (!annotationValues.isEmpty()) {
								operationName = (String) annotationValues.get(0);
							}
						}
						else if(annotationName.equals("guard")){
							if (!annotationValues.isEmpty()) {
								guard = (String) annotationValues.get(0);
							}
						}
					}//end for loop annotations
				}
				// how many instances of the class to create
				if(operation.getName().equals("create")){
					ArrayList classes= new ArrayList();
					for(int i=0;i<instances;i++){		
						Object modelObject = getModel().createInstance(operation.getContextType(context).getName());
						operation.execute(modelObject, null, context);
						classes.add(modelObject);
						
						
					}
					if(!operationName.isEmpty()) {
						if(classGroup.containsKey(operationName)){
							classGroup.get(operationName).addAll(classes);
						}
						else
							classGroup.put(operationName, classes);
					}
					operationName="";			
				}
					
			}
			
		}//end for loop (operations)
		//System.out.println(ne);
		//model.store(ne.substring(1));
		//System.out.println("generation time is: "+(System.currentTimeMillis()-time));
	}
	
	/**
	 * Creates the emf model.
	 *
	 * @param name the name
	 * @param model the model
	 * @param metamodel the metamodel
	 * @param readOnLoad the read on load
	 * @param storeOnDisposal the store on disposal
	 * @return the emf model
	 * @throws EolModelLoadingException the eol model loading exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	protected static EmfModel createEmfModel(String name, String model, 
			String metamodel, boolean readOnLoad, boolean storeOnDisposal) 
					throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
				metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI, 
				model);
		properties.put(EmfModel.PROPERTY_READONLOAD, false + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);	
		return emfModel;
	}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	protected EmfModel getModel() {
		for(IModel mod:context.getModelRepository().getModels()){
			if (mod instanceof EmfModel){
				return (EmfModel) mod;
			}
		}
		try {
			throw new Exception();
		} catch (Exception e) {
			System.out.println("No EmfModel found");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the int.
	 *
	 * @param object the object
	 * @return the int
	 */
	protected int getInt(Object object){
		if(object instanceof Integer)
			return (int)object;
		else
			return Integer.parseInt((String) object);	
	}
	
	/**
	 * Gets the file path.
	 *
	 * @param file the file
	 * @return the file path
	 */
	protected String getFilePath(String file){
		URI ecoreUri= URI.createURI(file);
		URI filePath = ecoreUri.trimFileExtension();
		return filePath.toString();
	}
	
	/**
	 * Gets the new file path.
	 *
	 * @param file the file
	 * @param output the output
	 * @return the new file path
	 */
	protected String getNewFilePath(String file, String output){
		URI ecoreUri= URI.createURI(file);
		if(!output.contains(".")) output=output+ ecoreUri.fileExtension();
		URI filePath = ecoreUri.trimSegments(1);
		filePath= filePath.appendSegment(output);
		return filePath.toString();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String [] args){
		//System.out.println(getNewFilePath("C:/Users/Popoola/git/latex/test.pdf","me.ade"));
		
	}
}
