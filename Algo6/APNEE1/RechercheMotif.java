import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class RechercheMotif {

    // Recherche du motif {motif} dans la chaÃ®ne {texte}
    static void recherche(String texte, String motif) {
		// indice de parcours du texte
		int i;
		// indice de parcours du motif
		int j;

		i = 0;
		j = 0;
		while ((i < texte.length() - motif.length() + 1)) {
		    
		    // recherche du motif Ã  l'indice i
		    j = 0;
		    while ((j < motif.length()) && (texte.charAt(i+j) == motif.charAt(j))) {
			// invariant : texte[i..i+j-1] = motif[0..j-1]
				j = j + 1;
		    }

		    if (j == motif.length()) {
				System.out.println("Motif trouvé à l'indice " + i);
		    }
		    
		    i = i + 1;
		}
    }



    
    // Renvoie le hashcode de la sous-chaine s[debut..fin]
    static int hash(String s, int debut, int fin) {
		int h = 0;
		for(int i = debut; i < fin; i++) {
		    h += (int)s.charAt(i);
		}
		return h;
    }

    // Renvoie le hashcode de la sous-chaine s[debut..fin], Ã  l'aide
    // de la valeur hash = hashcode(s,debut-1,fin-1)
    static int updateHash(String s, int hash, int debut, int fin) {
		// A COMPLETER

    	int h = hash;
    	
    	h = h - (int)s.charAt(debut-1) + (int)s.charAt(fin-1);

		return h;
    }

    static void rechercheKR(String texte, String motif) {
		// indice de parcours du texte
		int i;
		// indice de parcours du motif
		int j;
		// valeurs de hachage du motif et des sous-chaÃ®nes
		int hmotif, hcourant = 0;

		hmotif = hash(motif,0,motif.length());


		i = 0;
		j = 0;
		while ((i < texte.length() - motif.length() + 1)) {
		    // hachage de la sous-chaÃ®ne texte[i..i+m-1]

			if(i == 0){
				hcourant = hash(texte, i, i+motif.length());	
			} else {
		    	hcourant = updateHash(texte, hcourant, i, i + motif.length());
			}

		    if (hcourant == hmotif) {
				// recherche du motif Ã  l'indice i
				j = 0;
				while ((j < motif.length()) && (texte.charAt(i+j) == motif.charAt(j))) {
				    // invariant : texte[i..i+j-1] = motif[0..j-1]
				    j = j + 1;
				}

				if (j == motif.length()) {
				    System.out.println("Motif trouvé à l'indice " + i);
				}
		    }
		    i = i + 1;
		}
    }


    public static void main(String args[]) {
	
		String texte;
		String motif;

		FileInputStream input;
		BufferedReader reader;

		for (int i = 0; i < args.length; i++) {
		    try {
				// Ouverture du fichier passÃ© en argument
				input = new FileInputStream(args[i]);
				reader = new BufferedReader(new InputStreamReader(input));

				// Lecture de la chaÃ®ne
				texte = reader.readLine();
				// Lecture du motif
				motif = reader.readLine();
				
				// date de dÃ©but
				long startTime = System.nanoTime();
				
				rechercheKR(texte,motif);
				//recherche(texte, motif);
				
				// date de fin pour le calcul du temps Ã©coulÃ©
				long endTime = System.nanoTime();

				// Impression de la longueur du texte et du temps d'exÃ©cution
				System.out.println(texte.length() + "\t" + ((endTime - startTime)/1.0E9));

		    } catch (FileNotFoundException e) {
				System.err.println("Erreur lors de l'ouverture du fichier " + args[i]);
		    } catch (IOException e) {
				System.err.println("Erreur de lecture dans le fichier");
		    }
		}	
	}
}

    
    
    