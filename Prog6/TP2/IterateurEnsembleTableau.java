
class IterateurEnsembleTableau<T> implements Iterator<T> {

	EnsembleTableau<T> e;
	int position;
	int dernier;


	IterateurEnsembleTableau(EnsembleTableauEntiers<T> ens){
		this.e = ens;
		this.position = 0;
		this.dernier = -1;
	}

	boolean aProchain(){
		return position < e.taille;
	}

    T prochain(){
    	if(aProchain()){

    		dernier = position;
    		return e.contenu[position++];

    	} else {
    		throw new Exception();
    	}
    }

    void supprime(){
    	if( dernier != -1 ){

    		e.taille--;
    		e.contenu[dernier] = e.contenu[e.taille];
    		e.contenu[e.taille] = null;
    		dernier = -1;

    	} else {
    		throw new Exception();
    	}
    }

    Iterateur<T> clone(){
    	IterateurEnsembleTableau<T> renvoi = new IterateurEnsembleTableau<T>(this.e);
    	renvoi.position = position;
    	renvoi.dernier = dernier;
    	return renvoi;
    }


}