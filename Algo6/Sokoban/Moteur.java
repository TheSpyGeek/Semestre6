
import java.util.*;

class Moteur {
    Terrain t;
    int lignePousseur, colonnePousseur, nb_actions = 0;


    Moteur(Terrain t) {
        this.t = t;
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++)
                if (t.consulter(i,j).contient(Case.POUSSEUR)) {
                    lignePousseur = i;
                    colonnePousseur = j;
                    return;
                }
    }

    private boolean sac_adjacent(int i, int j){
        if(est_possible(i+1, j) && t.consulter(i+1,j).contient(Case.SAC)){
            return true;
        }
        if(est_possible(i-1, j) && t.consulter(i-1,j).contient(Case.SAC)){
            return true;
        }
        if(est_possible(i,j+1) && t.consulter(i,j+1).contient(Case.SAC)){
            return true;
        }
        if(est_possible(i, j-1) && t.consulter(i,j-1).contient(Case.SAC)){
            return true;
        } 
        return false;
    }

    public boolean action(int i, int j) {
        
        if (t.consulter(i,j).estLibre()) {

            // int y = lignePousseur;
            // int x = colonnePousseur;

            if(t.consulter(i,j).contient(Case.SAC)){

                return false;

            } else if(existe_chemin(lignePousseur, colonnePousseur, i, j)){

                System.out.println("Chemin existant");

                Case courante = t.consulter(lignePousseur, colonnePousseur);
                courante = courante.retrait(Case.POUSSEUR);
                t.assigner(courante, lignePousseur, colonnePousseur);

                courante = t.consulter(i, j);
                courante = courante.ajout(Case.POUSSEUR);
                t.assigner(courante, i, j);

                lignePousseur = i;
                colonnePousseur = j;
                nb_actions ++;

                return true;
                
            } else {
                System.out.println("chemin impossible");
                return false;
            }




            
        } else {
            return false;
        }
    }
    

    private boolean est_possible(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0 && t.consulter(i,j).estLibre());
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
