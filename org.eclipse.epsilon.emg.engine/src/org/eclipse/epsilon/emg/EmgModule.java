package org.eclipse.epsilon.emg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emg.operationContributors.CollectionOperationContributor;
import org.eclipse.epsilon.emg.operationContributors.ObjectOperationContributor;
import org.eclipse.epsilon.eol.EolLibraryModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.IEolLibraryModule;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.epl.EplModule;
import org.eclipse.epsilon.epl.execute.PatternMatchModel;

public class EmgModule extends EplModule implements IModule, IEolExecutableModule {
	ModelGenerator mod;
	RandomGenerator random;
	EmfModel model;
	protected long seed;
	Map<String,Collection> classGroup= new HashMap<String,Collection>();//maps create names to collection of models
	int i=0;
	public EmgModule(){
		reset();
		random= new RandomGenerator();
		
	}
	protected void preload(){
		context.setModule(this);
		model= getModel();
		ArrayList operation= new ArrayList<Operation>();
		context.getOperationContributorRegistry().add(new CollectionOperationContributor(random));
		context.getOperationContributorRegistry().add(new ObjectOperationContributor(random,model,classGroup));
		for (Operation op: getOperations()){
			String name = op.getName();
			if(name.equals("create"))
				operation.add(op);
		}
		try {
			executeOperations(operation,getFilePath(model.getModelFile())+"test.ecore");
		} catch (EolModelElementTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EolRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String getMainRule() {
		return "eplModule";
		
	}
		
	@Override
	public HashMap<String, Class<?>> getImportConfiguration() {
		HashMap<String, Class<?>> importConfiguration = super.getImportConfiguration();
		importConfiguration.put("emg", EmgModule.class);
		return importConfiguration;
	}
	@Override
	public Object execute() throws EolRuntimeException {	
		preload();
		
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
		return matchModel;
		
	}
	protected EmfModel executeOperations(ArrayList<Operation> operationNames, String ne) throws EolModelElementTypeNotFoundException, EolRuntimeException{
		long time=System.currentTimeMillis();
		AnnotationBlock annotationBlock;
		String name,operationName,guard;
		EClass eclass;
		for (Operation operation: operationNames){
			//get the class context
			eclass = model.classForName(operation.getContextType(context).getName());
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
					Object modelObject = model.createInstance(operation.getContextType(context).getName());
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
		model.store(ne);
		System.out.println("generation time is: "+(System.currentTimeMillis()-time));
		return model;
		
	}
	protected EmfModel getModel(){
		for(IModel mod:context.getModelRepository().getModels()){
			if (mod instanceof EmfModel){
				return (EmfModel) mod;
			}
		}	
		return null;
	}
	protected int getInt(Object object){
		if(object instanceof Integer)
			return (int)object;
		else
			return Integer.parseInt((String) object);	
	}
	protected String getFilePath(String file){
		URI ecoreUri= URI.createURI(file);
		URI filePath = ecoreUri.trimFileExtension();
		return filePath.toString();
	}
}
