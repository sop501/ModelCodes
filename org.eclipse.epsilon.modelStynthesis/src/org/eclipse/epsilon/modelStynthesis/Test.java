package org.eclipse.epsilon.modelStynthesis;

/*import java.io.File;
//import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;*/

public class Test {

	public static void main(String[] args) {
		String temp="ade,df";
		if(temp.contains("ad")){
			System.out.println(temp);
		}
		/**
		if (temp.contains(",")){
			//temp.s
		String[]temp1= temp.split("\\,", 2);
		System.out.println(temp1.toString());
		}
		
		URI te = URI.createPlatformResourceURI("org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/std.ecore",true);
		
		String te3= te.toString()+"2.ecore";
		
		//String te3= te1.appendFileExtension("2.ecore").toString();
		//URI te= URI.create("student.ecore");
		File file = new File("src/org/eclipse/epsilon/modelStynthesis/std.ecore");
		java.net.URI te4= file.toURI();
		URI te1= URI.createURI(te4.getPath()).trimFileExtension();
		//te=file.toURI();
		//String te3=te.toString();
		//te.appendFragment(fragment)
		System.out.println(te.toString());
		//URI modelsBaseUri = qvtcURI.trimFileExtension();

		// TODO Auto-generated method stub
		String name;
		Map<String,String> map = new HashMap<String,String>();
		ArrayList test= new ArrayList();
		for(int i=0;i<10;i++){
			name=""+i;
			map.put(name, name+"New");
			test.add(name);
		}
		System.out.println(map.toString());
**/
	}

}
