
import java.util.*;
import java.io.*;


class ChargeurNiveaux {

	static int niveau = 1;
	
	static void init(){
		// ntm
	}


	public static void main(String [] args){

		double stockage;
		Scanner myscan = null;

		File f = new File("testdouble.txt");
		if(f.exists()){


			try {
				myscan = new Scanner(f);
				//System.out.println(myscan.delimiter());
			
			} catch(FileNotFoundException e){
				System.out.println("Erreur fichier non trouvé");
			}

			catch(Exception e){
				System.err.println("Erreur");
			} 

			while(myscan.hasNext()){

				stockage = myscan.nextDouble();
				// ou pour toute la ligne
				//stockage = myscan.nextLine();
				System.out.println(stockage);
			}

		}

		










	}





}