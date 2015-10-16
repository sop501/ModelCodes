
public class T {

	public static void main(String[] args) {
		String string = "type=me,annotation=source(key=details),name=ade,annotation=khg,";
		String temp2,temp3;
		int index=0;
		String[] temp1;
		//temp.s
		String property="";
		
		if(string.contains("annotation")){
			temp1= string.split("annotation");
			property=temp1[0];
			for(int i=1;i<temp1.length;i++){
				if(temp1[i].contains("(")){
					if(temp1[i].contains(")")){
						string=temp1[i].trim();
						index=string.indexOf(")");
						temp2=string.substring(1,index+1);
						System.out.println(temp2);
						temp3=string.substring(index+1,string.length());
						System.out.println(temp3);
						if(!temp3.isEmpty()){
							System.out.println("true");
							property=property+temp3.substring(1);
						}
						
					}
					else{
						System.out.println("error");
					}
				
				}
				else{
					if(temp1[i].contains(",")){
						string=temp1[i].trim();
						index=string.indexOf(",");
						temp2=string.substring(1,index);
						System.out.println("temp2 "+temp2);
						temp3=string.substring(index+1,string.length()).trim();
						System.out.println(temp3);
						if(!temp3.isEmpty())
							property=property+temp3.substring(1);
						
					}
					else{
						string=temp1[i].trim();
						temp2=string.substring(1);
						System.out.println(temp2);
					}
				}
				
			}
		}
		if(property.endsWith(","))
			System.out.println(property.substring(0, property.length()-1));	
		
		// TODO Auto-generated method stub

	}

}
