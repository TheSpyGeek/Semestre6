

public class test {
	public static void main(String [] args){
		Hashtable table = new Hashtable();

		table.inserer(10);
		table.inserer(9);
		table.inserer(20);
		table.inserer(2);
		table.inserer(3);
		table.inserer(21);


		// table.supprimer(10);

		System.out.println(table.aff());
	}
}