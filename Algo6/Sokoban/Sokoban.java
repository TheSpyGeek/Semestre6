


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
        int nb_but = 0;

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

        f.setDrawAreaSize(50*t.largeur(),50*t.hauteur());
        Moteur m = new Moteur(t);
        TerrainGraphique tg = new TerrainGraphique(f, t);

        e.addMouseListener(new EcouteurDeSouris(f, tg, m));
        e.addKeyListener(new EcouteurDeClavier(f, t, tg, m));

        f.tracerSansDelai(tg);


        for(int i=0; i<t.hauteur(); i++){
            for(int j=0; j<t.largeur(); j++){
                if(t.consulter(i, j).contient(Case.BUT)){
                    nb_but++;
                }
            }
        }

        System.out.println("Il y a "+ nb_but+" but(s)");
        
        while (nb_but != 0){
            e.waitForEvent();
        }
    }
}
