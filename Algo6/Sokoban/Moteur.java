
import java.util.*;
import java.awt.Color;
import Dessin.Fenetre;

//  DES COMMENTAIRES PLS

class Moteur {
    Fenetre f;
    Terrain t;
    Terrain t_chemin;
    TerrainGraphique tg;
    TerrainGraphique tg_chemin;
    int lignePousseur, colonnePousseur, nb_actions = 0;

    static final int NORD = 1;  
    static final int OUEST = 2;
    static final int SUD = 3;
    static final int EST = 4;

    int buti, butj;
    int saci, sacj;

    int nb_mouvement;


    Moteur(Terrain t, Fenetre f) {
        this.f = f;
        this.t = t;
        this.t_chemin = t;
        this.tg = new TerrainGraphique(f, t);
        this.tg_chemin = new TerrainGraphique(f, t_chemin);
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++)
                if (t.consulter(i,j).contient(Case.POUSSEUR)) {
                    lignePousseur = i;
                    colonnePousseur = j;
                } else if(t.consulter(i, j).contient(Case.BUT)){
                    this.buti = i;
                    this.butj = j;
                } else if(t.consulter(i, j).contient(Case.SAC)){
                    this.saci = i;
                    this.sacj = j;
                }
        this.nb_mouvement = 0;
    }

    private int sac_adjacent(int i, int j){

        if(est_possible_sans_libre(i+1, j) && lignePousseur == i+1 && colonnePousseur == j){
            return NORD;
        } else if(est_possible_sans_libre(i-1, j) && lignePousseur == i-1 && colonnePousseur == j){
            return SUD;
        } else if(est_possible_sans_libre(i,j+1) && lignePousseur == i && colonnePousseur == j+1){
            return OUEST;
        } else if(est_possible_sans_libre(i, j-1) && lignePousseur == i && colonnePousseur == j-1){
            return EST;
        } else {
            return -1;
        }
    }
    private int val_absolue(int i){
        if(i<0){
            return (-1)*i;
        } else {
            return i;
        }
    }

    private int deplacement(int i, int j, int k, int l){
        return val_absolue(i-k) + val_absolue(j-l);
    }

    public boolean action(int i, int j) {

        int direction;
        
        if (t.consulter(i,j).estLibre()) {

            if(existe_chemin(lignePousseur, colonnePousseur, i, j)){

                Case courante = t.consulter(lignePousseur, colonnePousseur);
                courante = courante.retrait(Case.POUSSEUR);
                t.assigner(courante, lignePousseur, colonnePousseur);

                courante = t.consulter(i, j);
                courante = courante.ajout(Case.POUSSEUR);
                t.assigner(courante, i, j);

                this.nb_mouvement += deplacement(lignePousseur, colonnePousseur, i, j);

                lignePousseur = i;
                colonnePousseur = j;
                nb_actions ++;


                return true;
                
            } else {
                return false;
            }

        } else if(t.consulter(i,j).contient(Case.SAC)){

            direction = sac_adjacent(i, j);

            if(direction != -1){

                Case pousseur = t.consulter(lignePousseur, colonnePousseur);
                Case sac = t.consulter(i, j);
                Case c = t.consulter(lignePousseur, colonnePousseur);


                if(direction == NORD && est_possible(i-1, j)){
                    if(t.consulter(i-1, j).contient(Case.BUT)){
                        sac = sac.retrait(Case.SAC);
                        t.but_atteint();
                    }
                    t.assigner(sac, i-1, j); 
                    t.assigner(pousseur, i, j);
                    c = c.retrait(Case.POUSSEUR);
                    t.assigner(c, lignePousseur, colonnePousseur);
                    lignePousseur--;
                    nb_mouvement++;
                    return true;

                } else if(direction == SUD && est_possible(i+1, j)){
                    if(t.consulter(i+1, j).contient(Case.BUT)){
                        sac = sac.retrait(Case.SAC);
                        t.but_atteint();
                    }
                    t.assigner(sac, i+1, j); 
                    t.assigner(pousseur, i, j);
                    c = c.retrait(Case.POUSSEUR);
                    t.assigner(c, lignePousseur, colonnePousseur);
                    lignePousseur++;
                    nb_mouvement++;
                    return true;

                } else if(direction == EST && est_possible(i, j+1)){
                    if(t.consulter(i, j+1).contient(Case.BUT)){
                        sac = sac.retrait(Case.SAC);
                        t.but_atteint();
                    }
                    t.assigner(sac, i, j+1); 
                    t.assigner(pousseur, i, j);
                    c = c.retrait(Case.POUSSEUR);
                    t.assigner(c, lignePousseur, colonnePousseur);
                    colonnePousseur++;
                    nb_mouvement++;
                    return true;

                } else if(direction == OUEST && est_possible(i, j-1)){
                    if(t.consulter(i, j-1).contient(Case.BUT)){
                        sac = sac.retrait(Case.SAC);
                        t.but_atteint();
                    }
                    t.assigner(sac, i, j-1); 
                    t.assigner(pousseur, i, j);
                    c = c.retrait(Case.POUSSEUR);
                    t.assigner(c, lignePousseur, colonnePousseur);
                    colonnePousseur--;
                    nb_mouvement++;
                    return true;

                } else {
                    return false;
                }

            } else {
                return false;
            }


            
        } else {
            return false;
        }
    }


   
    private boolean est_possible(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0 && t_chemin.consulter(i,j).estLibre());
    }

    private boolean est_possible_sans_libre(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0);
    }

    private boolean est_possible_sans_pousseur(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0 && !t_chemin.consulter(i, j).contient(Case.SAC) && !t_chemin.consulter(i, j).contient(Case.SAC_SUR_BUT)
            && !t_chemin.consulter(i, j).contient(Case.OBSTACLE) && !t_chemin.consulter(i, j).contient(Case.INVALIDE));
    }


    public int nombre_actions(){
        return nb_actions;
    }


    private int heuristique(int x, int y){

        return val_absolue(x-buti) + val_absolue(y-butj);

    }

    // private int nb_deplacement(int x, int y){
    //     return val_absolue(saci-x) + val_absolue(sacj-y);        
    // }

    // private int fx(int x, int y){
    //     return heuristique(x,y) + nb_deplacement(x,y);
    // }


    private ArrayList<Sac_Perso> successeurs(Sac_Perso combi, int nb_deplacement) {

        Couple perso = new Couple(combi.perso.i, combi.perso.j);
        Couple perso_deplace = new Couple(combi.sac.i, combi.sac.j);

        ArrayList<Sac_Perso> C = new ArrayList<Sac_Perso>();
        Couple nord, sud, ouest, est;
        nord = new Couple(combi.sac.i-1, combi.sac.j);
        sud = new Couple(combi.sac.i+1, combi.sac.j);
        ouest = new Couple(combi.sac.i, combi.sac.j-1);
        est = new Couple(combi.sac.i, combi.sac.j+1);

        if(est_possible_sans_pousseur(nord.i, nord.j) && existe_chemin(combi.perso.i, combi.perso.j, nord.i, nord.j) && est_possible_sans_pousseur(sud.i, sud.j)){
            C.add(new Sac_Perso(sud, perso_deplace, nb_deplacement + heuristique(sud.i, sud.j)));
        }
        if(est_possible_sans_pousseur(sud.i, sud.j) && existe_chemin(combi.perso.i, combi.perso.j, sud.i, sud.j) && est_possible_sans_pousseur(nord.i, nord.j)){
            C.add(new Sac_Perso(nord, perso_deplace, nb_deplacement + heuristique(nord.i, nord.j)));
        }
        if(est_possible_sans_pousseur(est.i, est.j) && existe_chemin(combi.perso.i, combi.perso.j, est.i, est.j) && est_possible_sans_pousseur(ouest.i, ouest.j)){
            C.add(new Sac_Perso(ouest, perso_deplace, nb_deplacement + heuristique(ouest.i, ouest.j)));
        }
        if(est_possible_sans_pousseur(ouest.i, ouest.j) && existe_chemin(perso.i, perso.j, ouest.i, ouest.j) && est_possible_sans_pousseur(est.i, est.j)){
            C.add(new Sac_Perso(est, perso_deplace, nb_deplacement + heuristique(est.i, est.j)));
        }

        return C;
    }

    
    static public int max(int a, int b){
        if(a > b){
            return a;
        } else {
            return b;
        }
    }
		

    public void Explorer(){


        file_a_priorite fap = new file_a_priorite();

        Sac_Perso combi = new Sac_Perso(saci, sacj, lignePousseur, colonnePousseur, heuristique(saci, sacj));
        Sac_Perso current;
        // int [] poids = new int[max(t.hauteur(),t.largeur())+1];
        // poids[0] = 0;

        int poids;
        Couple perso = new Couple(lignePousseur, colonnePousseur);
        Couple sac = new Couple(saci, sacj);

        int nb_deplacement = 0;



        fap.Inserer(combi);

        ArrayList<Sac_Perso> succ = new ArrayList<Sac_Perso>();

        do {
            t_chemin.assigner(Case.LIBRE, perso.i, perso.j);
            t_chemin.assigner(Case.LIBRE, sac.i, sac.j);

            current = fap.Extraire();
            perso = current.perso;
            sac = current.sac;
            t_chemin.assigner(Case.POUSSEUR, perso.i, perso.j);
            t_chemin.assigner(Case.SAC, sac.i, sac.j);

            System.out.println("Current = "+current+" Nombre deplacement sac = "+nb_deplacement);
            nb_deplacement++;
            succ = successeurs(current, nb_deplacement);

            for(int i=0; i<succ.size(); i++){

                Sac_Perso z = succ.get(i);
                // System.out.println("Z = "+z);

                // poids = current.poids+1;

                // if(poids < z.poids){
                //     z.poids = poids;
                    // pred[z] = current; pour faire le chemin j'imagine
                    fap.Inserer(z);
                    tg_chemin.setStatut(Color.green, z.sac.i, z.sac.j);


                // }
            }

            f.tracer(tg_chemin);

        } while(!fap.Est_Vide() && (current.sac.i != buti || current.sac.j != butj));

        System.out.println("Fap vide "+fap.Est_Vide());

        if(current.sac.i == buti && current.sac.j == butj){
            System.out.println("Solution trouvé !!!");
        } else {
            System.out.println("Solution non trouvé");
        }



    }






    public boolean existe_chemin(int i, int j, int buty, int butx){

        boolean found = false;

        if(i == buty && j == butx){
            return true;
        }


        Iterator<Couple> it;


        ArrayList<Couple> ens = new ArrayList<Couple>();
        Couple c, but;

        but = new Couple(buty, butx);

        boolean [][] marqued = new boolean[t_chemin.hauteur()][t_chemin.largeur()];
        for(int k=0; k<t.hauteur();k++){
            for(int l=0; l<t.largeur();l++){
                marqued[k][l] = false;
            }
        }

        ens.add(new Couple(i, j));

        do {

            it = ens.iterator();
            
            c = it.next();
            it.remove();
            marqued[c.i][c.j] = true;

            if(est_possible(c.i+1, c.j)){
                if(!marqued[c.i+1][c.j]){
                    ens.add(new Couple(c.i+1, c.j));
                }

                if(but.same(new Couple(c.i+1, c.j), but)){
                    found = true;
                }
            }

            if(est_possible(c.i-1, c.j)){
                if(!marqued[c.i-1][c.j]){
                    ens.add(new Couple(c.i-1, c.j));
                }

                if(but.same(new Couple(c.i-1, c.j), but)){
                    found = true;
                }
            }

            if(est_possible(c.i, c.j+1)){
                if(!marqued[c.i][c.j+1]){
                    ens.add(new Couple(c.i, c.j+1));
                }

                if(but.same(new Couple(c.i, c.j+1), but)){
                    found = true;
                }
            }

            if(est_possible(c.i, c.j-1)){
                if(!marqued[c.i][c.j-1]){
                    ens.add(new Couple(c.i, c.j-1));
                }

                if(but.same(new Couple(c.i, c.j-1), but)){
                    found = true;
                }
            }

        } while(!ens.isEmpty() && !found);

        return found;

      
    }

}
