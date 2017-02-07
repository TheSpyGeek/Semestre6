
import java.util.*;




public class Hashtable {
	class Element {
		int present;
		int supprime;
		Object value;

		Element(){
			this.supprime = 0;
			this.present = 0;
		}

		Element(Object val){
			this.value = val;
			this.supprime = 0;
			this.present = 1;
		}
	}

	int taille = 11;

	Element [] table = new Element[taille];

	Hashtable(){
		for(int i=0; i<taille; i++){
			table[i] = new Element();
		}
	}



	void inserer(Object input){

		int index = input.hashCode() % this.taille;

		while(table[index].taken == 1){
			index++;
		}

		table[index].value = input;
	}


	// renvoie vrai si l'objet est trouvé dans la table
	boolean rechercher(Object input){

		int i; 
		int j=0;

		i = h(input, j);

		while((j<taille)&& table[i].present &&((table[i].supprime) || (!table[i].value.equals(input))){
			j++;
			i = h(input, j)
		}

		return // trouvé ou pas;

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

	/*public String aff(){
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
	}*/



}
