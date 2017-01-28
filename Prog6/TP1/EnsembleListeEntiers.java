

class EnsembleListeEntiers implements EnsembleEntiers {

	class Noeud {
		int elt;
		Noeud suivant;

		Noeud(int c, Noeud n){
			this.elt = c;
			this.suivant = n;
		}
	}
	
	Noeud tete;

	EnsembleListeEntiers() {
		tete = null;
	}

	public void ajoute(int c){
		Noeud nouveau = new Noeud(c, tete);
		tete = nouveau;
	}

	public void supprime_bis(int c) throws Exception{
		Noeud ptr = tete;
		Noeud suiv = tete;

	
		if(ptr != null){

			suiv = suiv.suivant;

			if(ptr.elt != c){
				while(suiv != null && suiv.elt != c){
					ptr = suiv;
					suiv = suiv.suivant;
				}
			}


		} else {
			throw new Exception(c + " non trouvé");
		}

		if(suiv.elt == c){ // suppression

			ptr.suivant = suiv.suivant;
			suiv = null; // liberation du pointeur
		} else if(ptr.elt == c){
			this.tete = ptr.suivant;
		} else {
			throw new Exception(c + " non trouvé");
		}

	}

	public void supprime(int c) throws Exception{

		Noeud ptr = tete;
		Noeud suiv = tete;

		if(ptr != null){
			suiv = suiv.suivant;
			if(ptr.elt != c){

				while(suiv != null && suiv.elt != c){
					ptr = suiv;
					suiv = suiv.suivant;
				}

				if(suiv.elt == c){ // suppression

					ptr.suivant = suiv.suivant;
					suiv = null; // liberation du pointeur
				} else {
					throw new Exception(c + " non trouvé");
				}


			} else {
				this.tete = ptr.suivant;
			}


		} else {
			throw new Exception(c + " non trouvé");
		}


	}

	public String toString(){
		Noeud ptr = tete;
		String str;

		str = "[ ";

		while(ptr != null){
			str = str + ptr.elt+" ";
			ptr = ptr.suivant;
		}

		str = str + "]" ;

		return str;
	}


	
}