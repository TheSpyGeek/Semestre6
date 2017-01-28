
import java.util.*;
import java.io.*;


class ChargeurNiveaux {

	static int niveau = 1;
	
	static void init(){
		// ntm
	}


	static void prochainNiveau(){

		String stockage;
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

				stockage = myscan.nextLine();
				// ou pour toute la ligne
				//stockage = myscan.nextLine();
				System.out.println(stockage);
			}

		}

		










	}





}