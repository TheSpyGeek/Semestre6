import java.util.*;
import java.io.*;

class EnumP {

	Maillon [] sommets_graphe;


	EnumP (InputStream in) throws Exception {
			lire(in);
	}



	   ///enumeration par parties // couplage doit etre initialisé à null partout et de la bonne taille
    /// et num_sommet appelé avec 0
    Maillon [] Enum(int num_sommet, Maillon [] couplage, Maillon arc){

    	if(estCouplage(sommets_graphe)){ // si le graphe initial est deja un couplage
    		return sommets_graphe;
    	} else {

    		if(num_sommet >= sommets_graphe.length/2){

    			if(estCouplage(couplage)){
    				// System.out.println("Couple : "+aff_couplage(couplage));
	            	return couplage;    				
    			} else {
    				// System.out.println("Pas trouvé");
    				return couplage_vide(sommets_graphe.length);
    			}

	        } else {
	        	
	        	
        		if(sommets_graphe[num_sommet] == null){
        			if(sommets_graphe[num_sommet] != null && arc == null){
        				// System.out.println("Error");
        			}
        			// System.out.println("Sommet suivant");
        			return Enum(num_sommet+1, couplage, arc);

        		} else {
        			if(arc == null){
        				arc = sommets_graphe[num_sommet];
        			}

        			Maillon [] couplage_bis = couplage;
        			Maillon [] res;

        			ajoute_arc(num_sommet, arc.arc, couplage_bis);

        			// System.out.println("Couplage bis");
        			// aff_couplage(couplage_bis);
        			// System.out.println("Couplage");
        			// aff_couplage(couplage);

        			if(arc.suivant == null){

        				res = choix(Enum(num_sommet+1, couplage_bis, sommets_graphe[num_sommet+1]), Enum(num_sommet+1, couplage, sommets_graphe[num_sommet+1]));        				
        				System.out.println("sommet : " + num_sommet + " graphe "+ aff_couplage(res));
        				return res;
        			} else {
        				res = choix(Enum(num_sommet+1, couplage_bis, sommets_graphe[num_sommet+1]), Enum(num_sommet, couplage, arc.suivant));
        				System.out.println("sommet : " + num_sommet + " graphe "+ aff_couplage(res));
        				return res;
        			}

        		}

	        }
    	}
        
    }

    public void test(){
    	Maillon [] t = new Maillon[4];
    	t = couplage_vide(4);

    	System.out.println("Vide : "+ aff_couplage(t));

    	ajoute_arc(0, new Arc(1,1,2), t);
    	ajoute_arc(2, new Arc(2,2,4), t);

    	System.out.println("Apres : "+ aff_couplage(t));


    }



    Maillon [] choix(Maillon [] G1, Maillon [] G2){

    	if(degre(G1) == 0){ // si G1 est vide
    		return G2;
    	} else {
    		return G1;
    	}
    }

    void ajoute_arc(int num_sommet, Arc arc, Maillon [] graphe){

    	graphe[num_sommet] = new Maillon();
    	graphe[num_sommet].arc = arc;
    	graphe[num_sommet].suivant = null;
    }


    static public Maillon [] couplage_vide(int n){
    	Maillon [] vide = new Maillon[n];
    	for (int i=0; i<n; i++){
    		vide[i] = null;
    	}

    	return vide;
    }


	public int nombre_sommets(){
		return sommets_graphe.length;
	}	


	static public int nombreSommets(Maillon [] g) {
        return g.length;
    }

    int nombreArcs(Maillon [] g) {
        int resultat = 0;

        for (int i=0; i<g.length; i++) {
            Maillon courant;

            courant = g[i];
            while (courant != null) {
                resultat ++;
                courant = courant.suivant;
            }
        }
        return resultat;
    }

    boolean existe(int source, int destination, Maillon [] g) {
        Maillon courant;

        courant = g[source];
        while (courant != null) {
            if (courant.arc.destination == destination)
                return true;
            courant = courant.suivant;
        }
        return false;
    }

    int [] successeurs(int source, Maillon [] g) {
        Maillon courant;
        int nombre = 0, indice = 0;
        int [] resultat;

        courant = g[source];
        while (courant != null) {
            nombre++;
            courant = courant.suivant;
        }
        resultat = new int[nombre];
        courant = g[source];
        while (courant != null) {
            resultat[indice++] = courant.arc.destination;
            courant = courant.suivant;
        }
        return resultat;
    }

    int degre(Maillon [] g) {
        int resultat=0;
        int [] degre_sommet;

        degre_sommet = new int[nombreSommets(g)];
        for (int i=0; i<nombreSommets(g); i++)
            degre_sommet[i] = 0;

        for (int i=0; i<nombreSommets(g); i++) {
            int [] succ;

            succ = successeurs(i, g);
            degre_sommet[i] += succ.length;
            for (int j=0; j<succ.length; j++)
                degre_sommet[succ[j]] += 1;
        }

        for (int i=0; i<nombreSommets(g); i++)
            if (degre_sommet[i] > resultat)
                resultat = degre_sommet[i];

        return resultat;
    }

    boolean arcsIndependants(Maillon [] g) {
        return degre(g) <= 1;
    }

  	boolean estCouplage(Maillon [] g) {
        return arcsIndependants(g) && (nombreSommets(g) == 2*nombreArcs(g));
    }
      



	void lire(InputStream in) throws Exception {
        int nombre_sommets;
        String specification_arc;
        String[] parties;
        int numero, source, destination;

        Scanner s = new Scanner(in);

        //init nombre de sommets
        nombre_sommets = s.nextInt();
        sommets_graphe = new Maillon[nombre_sommets];


        while (s.hasNext()) {

            specification_arc = s.next();
            if (!specification_arc.matches("[0-9]+/[0-9]+\\+[0-9]+/"))
                throw new Exception("Arc mal forme : " + specification_arc);

            // on sépare
            parties = specification_arc.split("/");

            // init du numero de l'arc
            numero = Integer.valueOf(parties[0]);

            // sommet source
            parties = parties[1].split("\\+");
            source = Integer.valueOf(parties[0]) - 1;
            destination = Integer.valueOf(parties[1]) - 1;

            Maillon nouveau = new Maillon();
            nouveau.arc = new Arc(numero, source, destination);
            nouveau.suivant = null;

            if (sommets_graphe[source] == null) {
                sommets_graphe[source] = nouveau;
            } else {
                Maillon courant = sommets_graphe[source];
                while (courant.suivant != null)
                    courant = courant.suivant;
                courant.suivant = nouveau;
            }
        }
    }


    public String aff_graphe() {
        String resultat = sommets_graphe.length + "\n";

        for (int i=0; i<sommets_graphe.length; i++) {
            Maillon courant = sommets_graphe[i];
            while (courant != null) {
                resultat += courant.arc + "\n";
                courant = courant.suivant;
            }
        }

        return resultat;
    }

    static public String aff_couplage(Maillon [] couplage) {
        String resultat = couplage.length + "\n";

        for (int i=0; i<couplage.length; i++) {
            Maillon courant = couplage[i];
            while (courant != null) {
                resultat += courant.arc + "\n";
                courant = courant.suivant;
            }
        }

        return resultat;
    }







}