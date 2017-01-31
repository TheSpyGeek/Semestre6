
import java.util.*;




public class Hashtable {
	class Element {
		Object value;
		Element next;

		Element(Object val, Element suiv){
			this.value = val;
			this.next = suiv;
		}
	}

	int taille = 11;

	Element [] table = new Element[taille];

	Hashtable(){
		for(int i=0; i<taille; i++){
			table[i] = null;
		}
	}

	void inserer(Object input){

		int index = input.hashCode() % this.taille;

		Element elt = new Element(input, table[index]);

		table[index] = elt;
	}


	// renvoie vrai si l'objet est trouvé dans la table
	boolean rechercher(Object input){

		int i = input.hashCode() % this.taille;
		Element maillon = this.table[i];

		while(maillon != null && !input.equals(maillon.value)){
			maillon = maillon.next;
		}

		return maillon != null;

	}


	void supprimer(Object input){

		int index = input.hashCode() % this.taille;
		Element maillon = this.table[index];

		if(maillon != null){

			if(input.equals(maillon.value)){
				this.table[index] = maillon.next;
			} else {

				while(maillon.next != null && !input.equals(maillon.value)){
					maillon = maillon.next;
				}

				if(maillon.next != null){
					maillon.next = maillon.next.next;
				} else {
					System.out.println("Element non trouvé");
				}
			}
		} else {
			System.out.println("Element non trouvé");
		}

	}

	public String aff(){
		Element maillon;
		String renvoi = "\n";

		for(int i=0; i<this.taille; i++){
			renvoi += "["+i+"] -> ";

			maillon = this.table[i];

			while(maillon != null){
				renvoi += maillon.value+" -> ";
				maillon = maillon.next;
			}

			renvoi += "[]\n";
		}


		return renvoi;
	}



}
