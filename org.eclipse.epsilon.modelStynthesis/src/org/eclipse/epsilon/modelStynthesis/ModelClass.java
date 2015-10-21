package org.eclipse.epsilon.modelStynthesis;
import java.io.File;
//import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

public class ModelClass {
	RandomGenerator random;
	protected final long seed;
	IEolContext context;
	CustomEplModule module;
	EmfModel model;
	Map<String,Collection> classGroup= new HashMap<String,Collection>();//maps create names to collection of models
	public ModelClass(){
		seed = System.currentTimeMillis();
		module = new CustomEplModule();
		random= new RandomGenerator(seed);
	}
	public ModelClass(long seed){
		this.seed=seed;
		module = new CustomEplModule();
		random= new RandomGenerator(seed);
	}
	
	protected void executeModule(File ecoreFile,File eolFile) throws Exception{
		java.net.URI temp= ecoreFile.toURI();
		URI ecoreUri= URI.createURI(temp.getPath());
		URI ecoreNew = ecoreUri.trimFileExtension();
		String ecoreU= ecoreUri.toString();
		String ecoreN= ecoreNew.toString()+"epl.ecore";
		
		File file= eolFile;
		boolean me = module.parse(file);
		//check for errors in parsing file
		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			return;
		}
		
		//create a new model
		model= createEmfModel("Model", ecoreU,ecoreU, true, false);
		EmfModel model1= createEmfModel("Model2", ecoreN,ecoreU, false, true);//empty model
		module.getContext().getModelRepository().addModel(model1);
		context=module.getContext();
		context.setModule(module);
		
		ArrayList create= new ArrayList<Operation>();
		context.getOperationContributorRegistry().add(new CollectionOperationContributor(random));
		context.getOperationContributorRegistry().add(new ObjectOperationContributor(random,model1,classGroup));
		for (Operation operation: module.getOperations()){
			String name = operation.getName();
			if(name.equals("create"))
				create.add(operation);
		}
		EmfModel model2= executeOperations(create,model1,ecoreN);
		module.execute();
		System.out.println("Model Generation Successful\nThe seed used for this generation is "+seed);
		module.getContext().getModelRepository().dispose();	
	}//end mainClass
	protected EmfModel executeOperations(ArrayList<Operation> operationNames,EmfModel model2, String ne) throws EolModelElementTypeNotFoundException, EolRuntimeException{
		AnnotationBlock annotationBlock;
		String name,operationName,guard;
		EClass eclass;
		//module.getOperations()
		for (Operation operation: operationNames){
			//get the class context
			eclass = model2.classForName(operation.getContextType(context).getName());
			if(eclass.isAbstract() || eclass.equals(null))
				continue;	
			int instances = 1;
			operationName="";guard="";
			//get the annotations
			annotationBlock = operation.getAnnotationBlock();

			if(!(annotationBlock==null)){
				List<Object> annotationValues;
				for(Annotation annotation:annotationBlock.getAnnotations()){
					if(!(annotation.hasValue()))
						continue;	
					name= annotation.getName();
					annotationValues = operation.getAnnotationsValues(name, context);
					//search for instances to be created
					if(name.equals("instances")){
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
					}
					else if(name.equals("name")){
						if (!annotationValues.isEmpty()) {
							operationName = (String) annotationValues.get(0);
						}
					}
					else if(name.equals("guard")){
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
					Object modelObject = model2.createInstance(operation.getContextType(context).getName());
					operation.execute(modelObject, null, context);
					classes.add(modelObject);
					
				}
				if(!operationName.isEmpty()){
					if(classGroup.containsKey(operationName)){
						classGroup.get(operationName).addAll(classes);
					}
					else
						classGroup.put(operationName, classes);
				}
					
			}
				
		}//end for loop (operations)
		model2.store(ne.substring(1));
		return model2;
		
	}
	
	
	//methods from org.eclipse.epsilon.examples.standalone.epsilonstandalone
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
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
	private int getInt(Object object){
		if(object instanceof Integer)
			return (int)object;
		else
			return Integer.parseInt((String) object);	
	}
	public static void main(String[] args){
		ModelClass model= new ModelClass(35467839);
		File ecoreFile = new File("src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore");
		File eolFile= new File("src/org/eclipse/epsilon/modelStynthesis/operations.epl");
		try {
			model.executeModule(ecoreFile, eolFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
