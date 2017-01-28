/*
 * Armoroides - casse briques Ã  but pÃ©dagogique
 * Copyright (C) 2016 Guillaume Huard
 * 
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique GÃ©nÃ©rale GNU publiÃ©e par la
 * Free Software Foundation (version 2 ou bien toute autre version ultÃ©rieure
 * choisie par vous).
 * 
 * Ce programme est distribuÃ© car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spÃ©cifique. Reportez-vous Ã  la
 * Licence Publique GÃ©nÃ©rale GNU pour plus de dÃ©tails.
 * 
 * Vous devez avoir reÃ§u une copie de la Licence Publique GÃ©nÃ©rale
 * GNU en mÃªme temps que ce programme ; si ce n'est pas le cas, Ã©crivez Ã  la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * Ã‰tats-Unis.
 * 
 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'HÃ¨res
 */
public class Niveau {
    int coucheEnCours;
    Ensemble<ComposantGraphique> liste;
    Ensemble<ComposantGraphique> tableau;    
    
    Niveau(String n, int nbCouches) {
        System.out.println("Niveau : "+n+", composÃ© de "+nbCouches+" couches");
        coucheEnCours = -1;
        liste = new EnsembleListe<>();
        tableau = new EnsembleTableau<>();
    }
    
    void ajouteComposant(int couche, ComposantGraphique composant) {
        if (couche != coucheEnCours) {
            System.out.println("Je remplis la couche "+couche+"...");
            coucheEnCours = couche;
        }
        liste.ajoute(composant);
        tableau.ajoute(composant);
    }
    
    void fixerDimensionsMax(int i, int j) {
        System.out.println("Le niveau a une largeur de "+i+" et une hauteur de "+j);
    }
    
    void fixeCouleurs(String [] t, int nb) {
        System.out.println("Les couleurs utilisÃ©es dans ce niveau sont :");
        for (int i=0; i<nb; i++) {
            System.out.println(i+" - "+t[i]);
        }
    }
    
    void nouvelleBalle() {
        System.out.println("TerminÃ©, voici les composants que j'ai stockÃ© :");
        System.out.println("- dans l'ensemble implÃ©mentÃ© dans un tableau");
        System.out.println(tableau.toString());
        System.out.println("- dans l'ensemble implÃ©mentÃ© dans une liste");
        System.out.println(liste.toString());
        System.out.println("PrÃªt Ã  jouer, en attente de balle !");
    }
}