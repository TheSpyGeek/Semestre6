

public class test {
	public static void main(String [] args){
		EnsembleListeEntiers e = new EnsembleListeEntiers();

		e.ajoute(1);
		e.ajoute(2);
		e.ajoute(3);
		e.ajoute(4);
		e.ajoute(5);

		try {
			e.supprime(1);
			e.supprime(2);
		} catch(Exception ex){
			System.err.println(ex);
		}


		System.out.println(e);
	}
}