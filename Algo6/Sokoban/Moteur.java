
import java.util.*;

class Moteur {
    Terrain t;
    int lignePousseur, colonnePousseur, nb_actions = 0;

    static final int NORD = 1;
    static final int OUEST = 2;
    static final int SUD = 3;
    static final int EST = 4;

    int nb_mouvement;


    Moteur(Terrain t) {
        this.t = t;
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++)
                if (t.consulter(i,j).contient(Case.POUSSEUR)) {
                    lignePousseur = i;
                    colonnePousseur = j;
                    return;
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
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0 && t.consulter(i,j).estLibre());
    }

    private boolean est_possible_sans_libre(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0);
    }


    public int nombre_actions(){
        return nb_actions;
    }




    public boolean existe_chemin(int i, int j, int buty, int butx){

        boolean found = false;

        class Couple {
            public int i;
            public int j;
            Couple(int i, int j){
                this.i = i;
                this.j = j;
            }

            public boolean same(Couple a, Couple b){
                return (a.i == b.i && a.j == b.j);
            }
        }





        Iterator<Couple> it;

        ArrayList<Couple> ens = new ArrayList<Couple>();
        Couple c, but;

        but = new Couple(buty, butx);

        boolean [][] marqued = new boolean[t.hauteur()][t.largeur()];
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
