
import java.util.*;
import java.io.*;


class ChargeurNiveaux {

	static int niveau = 1;
	
	static void init(){
		// ntm mdr
	}

//charAt

	static void prochainNiveau(){
		int etat = 0, sep, index_tab=0;
		String stockage, post, aft;
		Brique tab_br [100];
		Bonus tab_bo [100];
		Scanner myscan = null;

		File f = new File("Niveau-"+niveau+".txt");
		if(f.exists()){


			try {
				myscan = new Scanner(f);
				System.out.println(myscan.delimiter());
			
			} catch(FileNotFoundException e){
				System.out.println("Erreur fichier non trouvé");
			}

			catch(Exception e){
				System.err.println("Erreur");
			} 

			while(myscan.hasNext()){

				//On va stocker dans nos différents structures les données sur les composants pour ensuite les afficher avec leur coordonnées

				//stockage = myscan.next();


				switch (etat) : // 0 init et transitions, 1 composants 2 couches

				case 0 : 			
					stockage = myscan.nextLine();
					stockage = stockage.trim();	 //Enlève les espaces inutiles
					if(stockage.equals(New string("composants:") ){
							etat = 1;
					}
					else if (stockage.equals(New string("couches:") ){
							etat = 2;
					}
				case 1 : 
					stockage = myscan.nextLine(); //numéro du composant : indice -1
					stockage = myscan.nextLine();
					stockage = stockage.trim(); // on lit soit le type soit la résistance
					sep = stockage.indexOf(":"); //renvoie l'index de du ':'
					post = stockage.substring(0,sep); //donne la sous chaine de caract entre l'indice a et b
					aft = stockage.substring(sep, stockage.lentgh()-1); //lent donne la longueur (on sait jamais)
					if (post.equals("type") {
							if( past.equals("Brique"){
								//alors on rentre le type dans  le tableau brique un seul 
							}
							else {
								//alors on rentre le type dans tableau bonus
							}
					}
				//alors après, soit tu traite les différentes attributs dans les précédents if/else, soit tu fais à part, genre dans un autre état de l'automate.

				index_tab ++;
				break;
				case 2 :
						// on s'amuse à faire un for sur la lenght de la ligne 
						//si on est sur la première ligne on augment un compeur largeur jusquà' la fion de la boucle
						//sinon on lit et des qu'on rencontre un numéro, on stock les coordonées plus l'indice de la brique/bonus dans le tableau (si couche 0 alors tab_bo, sinon tab_br)
					
					
				break;	
					
					case default :
						break;
				// et amuse toi pour l'affichae;
				
				//System.out.println(stockage);
			}

		}

		



// On sait que la couche zéro n'a que des bonus et la couche 1 des briques (pour l'instant il n'y a pas de balles)






	}





}
