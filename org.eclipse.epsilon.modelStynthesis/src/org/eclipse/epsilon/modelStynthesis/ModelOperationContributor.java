package org.eclipse.epsilon.modelStynthesis;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;
import org.eclipse.epsilon.eol.models.IModel;

public class ModelOperationContributor extends OperationContributor {
	IModel model;
	public ModelOperationContributor(IModel mod){
		model=mod;
	}
	
	@Override
	public boolean contributesTo(Object target) {
		// TODO Auto-generated method stub
		return true;
	}
	public Object createInstance() throws EolModelElementTypeNotFoundException, EolNotInstantiableModelElementTypeException{
		return model.createInstance(target.toString());
		//System.out.println(target.toString());
		
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
	public void link(String name,Object value){
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

	

}
