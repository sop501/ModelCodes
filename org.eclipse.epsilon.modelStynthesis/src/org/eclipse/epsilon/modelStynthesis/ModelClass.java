package org.eclipse.epsilon.modelStynthesis;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.emf.EmfMetaModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
//import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

public class ModelClass {
	public static void main(String [] args) throws Exception{
		EolContext context = new EolContext();
		EolModule module = new EolModule();
		File file= new File("operations.eol");
		boolean me = module.parse(file);
		//check for errors in parsing file
		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			return;
		}
		//org.eclipse.epsilon.emc.emf
		//List<IModel> models = new ArrayList<IModel>();
		//models.add(createEmfModel("Model", "student.model","student.ecore", true, true));
		
		//load a new ecore
		String metamodel =getFile("student.ecore").getAbsolutePath();
		List<EPackage> metaPackage= EmfUtil.register(org.eclipse.emf.common.util.URI.createFileURI(metamodel), EPackage.Registry.INSTANCE);
		
		//EmfMetaModel meta = new EmfMetaModel();
		//meta.load();
		
		//create a new model
		IModel model= createEmfModel("Model", "student.model","student.ecore", false, true);
		context.getModelRepository().addModel(model);
		module.setContext(context);
		context.getOperationContributorRegistry().add(new OperationContributor() {
			
			@Override
			public boolean contributesTo(Object target) {
				return target instanceof Collection<?>;
				//return true;
			}
			
			public void deterministicRandom() {
				//target <- Student.all
				context.getFrameStack().get("seed");
				Collection me = (Collection) target;
				System.out.println(me.toArray()[0]);
			}
			
			
		});
		
		//System.out.println(ast.hasChildren());
		
		context.getFrameStack().put(Variable.createReadOnlyVariable("size", 10));
		//context.getFrameStack().put(Variable.createReadOnlyVariable("seed", 10));
		
		//get the operations
		Operation op = module.getOperations().get(0);
		
		//ccheck for classes in the metamodel and execute the method
		for(EPackage po:metaPackage ){
			for(EClassifier clas:po.getEClassifiers() ){
				if(clas instanceof EClass){
					EClass eclass= (EClass) clas;
					if(!(eclass.isAbstract())){
						for (Operation operation: module.getOperations()){
							if(operation.getName().equals("create") && operation.getContextType(context).getName().equals(eclass.getName())){
								
								List<Object> annotationValues = operation.getAnnotationsValues("instances", context);
								int instances = 1;
								if (!annotationValues.isEmpty()) {
									instances = (Integer) annotationValues.get(0);
								}
								
								for (int i = 0; i<instances; i++) {
									//System.out.println("found create method for: " + eclass.getName());
									Object modelObject = model.createInstance(eclass.getName());
									operation.execute(modelObject, null, context);
								}
							}
						}
					}
				}
				//op.execute(clas,null,context,true);		
			}
			
		}
		module.getContext().getModelRepository().dispose();
		/*for(Object po: model.allContents()){
			//System.out.println(po);
			op.execute(po,null,context,true);
		}*/
		//module.getContext().getModelRepository().addModel(model);
		//System.out.println(model.);
		
		/*model.getElementById("");
		int i=0;
		for(Object po: model.getAliases()){
			System.out.println(i+1);
			
		}*/
		//System.out.println(module.getContext().getModelRepository().getModelByName("Model").);
		//System.out.print(me2);
		//module.execute();
		//AST ast = module.getAst();
		//System.out.println(ast);
		
		//System.out.println(op.getAnnotationsAst());
		//System.out.print(op);
		//op.setName("ade");
		
	}
	
	//method
	protected void executeMethod(File file, String EcorePath){
		
	}
	
	
	//methods from org.eclipse.epsilon.examples.standalone.epsilonstandalone
	protected static EmfModel createEmfModel(String name, String model, 
			String metamodel, boolean readOnLoad, boolean storeOnDisposal) 
					throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
				getFile(metamodel).toURI().toString());
		properties.put(EmfModel.PROPERTY_MODEL_URI, 
				getFile(model).toURI().toString());
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
	protected static File getFile(String fileName) throws URISyntaxException {
		
		URI binUri = ModelClass.class.
				getResource(fileName).toURI();
		URI uri = null;
		
		if (binUri.toString().indexOf("bin") > -1) {
			uri = new URI(binUri.toString().replaceAll("bin", "src"));
		}
		else {
			uri = binUri;
		}
		
		return new File(uri);
	}
	/*public static void writeme(){
		try{
			
		}
		catch(Exception e){
			System.out.print(""+ e.getMessage());
		}
	}
	*/
	//eo = op.execute(module, null, null);
	
	
	
		
}
