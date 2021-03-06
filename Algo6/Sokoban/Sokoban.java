


import Dessin.*;
import java.io.*;
import java.awt.*;
import java.util.*;

public class Sokoban {
    public static void main(Fenetre f, Evenements e, String [] args) {
        // Desactive les mecanismes mis en oeuvre (reexecution d'une partie des
        // commandes, cache, hashage des commandes) dans Dessin lorsque les
        // commandes successives ne modifient qu'une partie de l'affichage.
        // Ici, l'affichage d'un terrain ecrase l'integralite de l'affichage
        // precedent.
        Random rand;
        Dessin.Parameters.requiresOverdraw = false;

        Terrain t = null;
        if (args.length > 0) {
            try {
                t = new Terrain(new FileInputStream(args[0]));
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
                System.exit(1);
            }
        } else
            t = Terrain.defaut();
        TerrainGraphique tg = new TerrainGraphique(f, t);
        Moteur m = new Moteur(t, f);
        if(args.length == 2){
	        if(args[1].compareTo("-a")==0){

		        f.setDrawAreaSize(50*t.largeur(),50*t.hauteur());
		        f.tracerSansDelai(tg);
		        m.Explorer();
		         while (!t.fini()){
		            e.waitForEvent();
		        }
	        }
	        else if (args[1].compareTo("-m")==0){
		        

		        e.addMouseListener(new EcouteurDeSouris(f, tg, m));
		        e.addKeyListener(new EcouteurDeClavier(f, t, tg, m));

		        
		        while (!t.fini()){
		            e.waitForEvent();
		        }
	    	}
	    	else {
	    		System.err.println("ERREUR : mauvaise option :  syntaxe : Sokoban [terrain optionnel] [option]. \n option : -a automatique -m manuel");
	    		System.exit(1);
	    	}
	    }
	    else {

	    	e.addMouseListener(new EcouteurDeSouris(f, tg, m));
		    e.addKeyListener(new EcouteurDeClavier(f, t, tg, m));

		        
		    while (!t.fini()){
		        e.waitForEvent();
		        }
	    	
	    }

        System.out.println("Jeu fini");
        System.out.println("Score = "+m.nb_mouvement);
        System.exit(0);
    }
}
