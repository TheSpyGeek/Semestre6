
//import java.util.Scanner; // pour les fonctions d'entree

import java.util.*;
// pour tout ajouter dans le util

class Essai_Scanner {

	//  première partie de l'apnee

    public static void main_first(String [] args) {
        Scanner my_scanner;
        String ligne;

        my_scanner = new Scanner(System.in);
        System.out.println("Saisissez une ligne");


        try {

        	ligne = my_scanner.nextLine();
        	System.out.println("Vous avez saisi la ligne : " + ligne);

        } catch(NoSuchElementException e){ // on catch l'erreur
        	System.err.println("Aucune ligne saisie");
        } catch(Exception e){
        	System.err.println("Autre execption");
        }

    }

    public static void main_int(String [] args){

    	Scanner my_scanner;
    	boolean isValid = false;
    	int input;


    	while(!isValid){
    		isValid = true;
	    	try {
    			my_scanner = new Scanner(System.in);
		    	System.out.println("Entree un entier");
		    	input = my_scanner.nextInt();
    			System.out.println("Votre input : "+input);
	    	} 
	    	catch (InputMismatchException e){ // l'entree n'est pas un entier
	    		System.err.println("Entrée invalide !!!!!! ");
	    		isValid = false;
	    	
		    } 
		    catch(NoSuchElementException e){ // l'entree est vide
	    		System.err.println("Veuillez entrer un element");
	    		isValid = false;
	    	} 


    	}
    }

    public static void main_brique(String [] args){

    	Brique maBrique;
    	float x, y;
    	int couleur, resistance;

    	Scanner my_scan = new Scanner(System.in);

    	System.out.println("Entrer la resistance");
    	resistance = my_scan.nextInt();

    	System.out.println("Entrer la couleur");
    	couleur = my_scan.nextInt();

    	System.out.println("Entrer la position x");
    	x = my_scan.nextFloat();

    	System.out.println("Entrer la position y");
    	y = my_scan.nextFloat();

    	maBrique = new Brique(resistance, couleur,x,y);


    	System.out.println("Affichage brique : "+maBrique);


    }

    public static void main(String [] args){
        ChargeurNiveaux.prochainNiveau();
    }

}