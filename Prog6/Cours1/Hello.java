class Hero{


	Hero(String name){
		this.Health = 100;
		this.name = name;
	}

	public void Heal(int n){
		this.Health = this.Health + n;
	}

	private int Health;
	private	String name;

	public void aff(){
		System.out.println("Hero "+name+" avec "+Health+" HP\n");
	}




}

class Hello {


	public static void main(String [] args){

		Hero player1 = new Hero("Maxime");

		player1.aff();

		//System.out.println("Hello INF362");
	}



}



