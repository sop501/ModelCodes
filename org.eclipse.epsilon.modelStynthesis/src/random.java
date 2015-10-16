import java.util.Random;


public class random {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random x = new Random();
		x.setSeed(3);
		for(int i=0;i<4;i++){
		System.out.println(x.nextInt(20));
		}

	}

}
