import java.util.*;
import java.io.*;

class EnumP {

	//Graphe_dep = new Graphe(InputStream in);


	ArrayList<Integer> destination = new ArrayList<Integer>();
	ArrayList<Integer> source = new ArrayList<Integer>();

/*    Graphe Supprimer(int source, int destination, Graphe G){

    	if( existe(source, destination)){
    		Maillon courant  = G.sommet[source];
    		Maillon temp;
    		if(courant.suivant == null) {
    			Maillon nouveau[] = new Maillon[G.nombreSommets() - 1];
    			for(int i = 0; i < G.nombreSommets(); i++){
    				if(i != source){
    					nouveau[i]=
    				}
    			}
    		}
    		while(courant != null){
    			if(courant.arc.destination == destination) {
    				int nb_arc = courant.arc.numero; 
    				temp = courant.suivant;
    				courant.suivant = temp.suivant;
    			}

    		}

    	G.nb_arc_dispo[nb_arc-1] = 1;
    	}
    	return G;
    }*/

    Graphe ajouter(int source, int destination, int numero, Graphe M){
    		if(!M.existe(source, destination) ) {
    			Arc nouveau = new Arc(numero, source, destination);
				
				M.sommets[source].arc = nouveau;
				M.sommets[source].suivant = null;
				
  
    		}
    	return M;
    	
    }


    void partition(Graphe G) {

    	Maillon courant= new Maillon();

    	for(int i=0; i<G.nombreSommets(); i++){
    		courant=G.sommets[i];
    		while(courant!= null) {
				if(!source.contains(courant.arc.source) ) 
    				source.add(courant.arc.source);
				if(!destination.contains(courant.arc.destination) ) 
    				destination.add(courant.arc.destination);
    			courant = courant.suivant;
    		}
    	}

    }

	ArrayList<Arc> supprOcc(ArrayList<Arc> A, int source, int destination) {

		ArrayList<Integer>indice = new ArrayList<Integer>();
		ArrayList<Arc> A_prime = new ArrayList<Arc>();
		A_prime=A;

		for(int i=0; i<A.size(); i++) {
			if(A.get(i).source == source) {
				indice.add(i);
			}
			if(A.get(i).destination == destination) {
				indice.add(i);
			}
		}
		while(!indice.isEmpty()){
			A_prime.remove(indice.get(0) );
			indice.remove(0);
		}
		return A_prime;

	}



	Graphe enum_graphe(ArrayList<Integer> L, ArrayList<Integer> R, ArrayList<Arc> A, Graphe M ){

		Graphe P1;
		Graphe M_prime; //couplage final
		ArrayList<Arc> A_prime = new ArrayList<Arc>() ; //ensemble d'arrête de G
		A_prime = A;
		M_prime = M;
		
		int i;

		boolean test;
		
		Arc x_y = new Arc(A.get(0).numero, A.get(0).source, A.get(0).destination);


		

		ArrayList<Integer> L_prime = new ArrayList<Integer>();
		ArrayList<Integer> R_prime = new ArrayList<Integer>();

		L_prime = L;
		R_prime = R;

		if(L.isEmpty() && R.isEmpty() ) {
			return M;
		}

		else if (A.isEmpty() || L.isEmpty() || R.isEmpty()  ) {
			return null;

		}

		else {
			A_prime.remove(x_y);
			P1 = enum_graphe(L,R,A_prime,M);
			if (P1 != null) {
				return P1;
			}

			else {
				
				i=L.indexOf(x_y.source);
				L_prime.remove(i);
				i=R.indexOf(x_y.destination);
				R_prime.remove(i);
				A_prime=supprOcc(A_prime, x_y.source, x_y.destination);
				M_prime = ajouter(x_y.source, x_y.destination, x_y.numero, M);

				return enum_graphe(L_prime, R_prime,A_prime, M_prime);
			}

		
		}





	}




	ArrayList<Arc> recupArc(Graphe G) {


		ArrayList<Arc> A = new ArrayList<Arc>();
		Maillon courant = new Maillon();
		for(int i=0; i< G.nombreSommets(); i++) {
			courant=G.sommets[i];
			while(courant!=null){
				A.add(courant.arc);
				courant=courant.suivant;
			}
		}
		return A;
			
	}

		


	 Graphe couplage(Graphe G){
		
		Graphe M; //couplage final
		ArrayList<Arc> A = new ArrayList<Arc>() ; //ensemble d'arrête de G
		M =null;
		M.sommets = new Maillon[G.nombreSommets()];
		
		A = recupArc(G);

		partition(G);

		ArrayList<Integer> L = new ArrayList<Integer>();
		ArrayList<Integer> R = new ArrayList<Integer>();
		
		L = source;
		R = destination;
		if (G.estCouplage() ) 
		M = enum_graphe(L,R, A, M);

		else 
		M = null;

		return M;

	}

}
