package org.eclipse.epsilon.modelStynthesis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;

public class Annotation {
	String type;
	String source;
	 Map<?,?> details;
	 EAnnotation eAnnotation;
	 List<EClassifier> refclasses;
	public Annotation(){
		type="";
		source="";
		details= new HashMap<Object, Object>();
		eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
	}
	public void setType(String types){
		type=types;
	}
	public String getType(){
		return type;
	}
	public void setSource(String sourc){
		source=sourc;
		eAnnotation.setSource(sourc);
	}
	public String getSource(){
		return source;
	}
	public void setDetails(String types){
		type=types;
	}
	public String getDetails(){
		return type;
	}
	protected EClass annotate(EClass clas, String source){
		//EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		//eAnnotation.setSource(source);
		clas.getEAnnotations().add(getAnnotation(source));
		System.out.println("successfully annotated class: "+ clas.getName() );
		return clas;
		
	}
	protected EAnnotation getAnnotation(String text){
		EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		String source;
		text = text.substring(1).trim();
		if (text.indexOf(",") > -1) {
			String[] parts = text.split(" ");
			String[] part2;
			source = parts[0];
			eAnnotation.setSource(source);
			for(int i=1;i<parts.length;i++){
				part2= parts[i].split("=");
				eAnnotation.getDetails().put(part2[0] + "", part2[1] + "");
			}
		}
		else {
			eAnnotation.setSource(text);
		}
		return eAnnotation;
	}
	
	protected void annotateRefClass(EClass clas, String source){
		refclasses.clear();
		for(EReference refClass: clas.getEAllReferences()){
			//System.out.println(refClass.getEType());
			EClassifier refClas= refClass.getEType();
			if(refclasses.contains(refClas))
				continue;
			refclasses.add(refClas);
			//refClas.
			//EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			//eAnnotation.setSource(source);
			refClas.getEAnnotations().add(getAnnotation(source));
			System.out.println("successfully annotated reference class: "+ refClas.getName() );
			
		}
	}
}
