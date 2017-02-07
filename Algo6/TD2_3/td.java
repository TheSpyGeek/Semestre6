
import java.io.*;
import java.util.*;

class td {



	public static void main(String [] args){

		Random rand = new Random();


		/*System.out.println("Dé 6 faces non pipé  : "+face(rand));
		System.out.println("Lancer de piece pipée : "+pile_face_pipe(0.60, rand));
		System.out.println("Lettre choisie par la roue : "+roue(rand));*/

		apprentissage();


	}


	//// EXERCICE  1

	static int face(Random rand){
		return rand.nextInt(6)+1;
	}

	static boolean pile_face_pipe(double p, Random rand){
		double proba = rand.nextDouble();

		return proba <= p;

	}

	static char roue(Random rand){
		String roux = new String("ananas");

		return roux.charAt(rand.nextInt(roux.length()));
	}

	//// EXERCICE 3

	public static void apprentissage(){


		try {


			File f = new File("apprentissage.txt");
			Scanner scan  = new Scanner(f);
			String mot_avant, mot_apres;
			int nb;

			Hashtable<String, Hashtable<String, Integer>> apprend  = new Hashtable<String, Hashtable<String, Integer> >();

			mot_avant = scan.next();

			while(scan.hasNext()){

				mot_apres = scan.next();

				if(apprend.containsKey(mot_avant)){

					if(apprend.get(mot_avant).containsKey(mot_apres)){
						nb = apprend.get(mot_avant).get(mot_apres);
						apprend.get(mot_avant).put(mot_apres, nb+1);
					} else {
						apprend.get(mot_avant).put(mot_apres, 1);
					}
				} else {
					apprend.put(mot_avant, new Hashtable<String, Integer>());
				}

				mot_avant = mot_apres;

			}


			System.out.println(apprend);



		} catch(Exception e){
			System.err.println("Erreur Execption "+ e);
		}
	}

	//// EXERCICE 3

	public static void generation(){
		Random rand = new Random();

		String mot_avant, mot_apres;
		int i = 0;

		mot_avant = "We"; // a choisir

		while(i<40){

			/// a finir







			i++;
		}



	}




}