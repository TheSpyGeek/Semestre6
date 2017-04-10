
import java.util.*;

//  DES COMMENTAIRES PLS

class Moteur {
    Terrain t;
    int lignePousseur, colonnePousseur, nb_actions = 0;

    static final int NORD = 1;  
    static final int OUEST = 2;
    static final int SUD = 3;
    static final int EST = 4;

    int buti, butj;
    int saci, sacj;

    int nb_mouvement;


    Moteur(Terrain t) {
        this.t = t;
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
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0 && t.consulter(i,j).estLibre());
    }

    private boolean est_possible_sans_libre(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0);
    }


    public int nombre_actions(){
        return nb_actions;
    }


    private int heuristique(int x, int y){

        return val_absolue(x-buti) + val_absolue(y-butj);

    }

    private int nb_deplacement(int x, int y){
        return val_absolue(saci-x) + val_absolue(sacj-y);        
    }

    private int fx(int x, int y){
        return heuristique(x,y) + nb_deplacement(x,y);
    }


    private ArrayList<Couple> successeurs_max(int x, int y) {

        ArrayList<Couple> C = new ArrayList<Couple>();
        Couple nord, sud, ouest, est;
        nord = new Couple(y-1, x);
        sud = new Couple(y+1, x);
        ouest = new Couple(y,x-1);
        est = new Couple(y,x+1);

        if(est_possible(y, x-1)){
            C.add(ouest);
        }        
        if(est_possible(y, x+1)){
            C.add(est);
        }
        if(est_possible(y+1, x)){
            C.add(sud);
        }
        if(est_possible(y-1, x)){
            C.add(nord);
        }

        return C;
    }


    private  ArrayList<Couple> successeurs(int x, int y) {
        ArrayList<Couple> C = new ArrayList<Couple>();
        Couple nord, sud, ouest, est;
        nord = new Couple(y, x-1);
        sud = new Couple(y, x+1);
        ouest = new Couple(y-1,x);
        est = new Couple(y+,x);
        int fx_sud, fx_nord,fx_est, fx_ouest;

        //  refaire coin meme cas file vide
        //les opposés des mur ne sont pas des solutions

        //cas du coin supérieur gauche
        if(x == 0 && y ==0 || x == largeur && y ==0 || x == 0 && y ==t.hauteur || x == t.largeur && y ==t.hauteur){
           //remains the list empty
             
            
        

        //cas  bordure supérieur
        } else if(y == 0){

            fx_est = fx(est.i,est.j))
            fx_ouest = fx(ouest.i, ouest.j);

            if(fx_ouest = fx_est){

            }

            if (fx_sud < fx_ouest) {
                C.add(sud);
            } else if(fx_ouest< fx_est){

                C.add(ouest);
            } else {
                C.add(est)
            }
        
    //cas bordure inférieur
        } else if(y == t.hauteur){
    
            fx_est = fx(est.i,est.j))
            fx_ouest = fx(ouest.i, ouest.j);

            if(fx_est = fx_ouest){
                C.add(est);
                C.add(ouest);
            } else if(fx_est < fx_ouest){
                C.add(est)
            } else{
                C.add(ouest);
            }

            

    

        //cas bordure la plus à gauche
        } else if(x ==0){
            fx_nord =fx(nord.i, nord.j);
            
            fx_sud = fx(sud.i, sud.j);


            if (fx_sud < fx_nord) {
                C.add(sud);
            } else if(fx_sud = fx_nord){
                C.add(nord);
                C.add(sud);
            }
			else {
				C.add(nord);
            }
                



            

        //cas bordure la plus à droite
        } else if(x=t.largeur){
            fx_nord =fx(nord.i, nord.j);
            fx_sud = fx(sud.i, sud.j);

            fx_nord =fx(nord.i, nord.j);
            
            fx_sud = fx(sud.i, sud.j);


            if {fx_sud < fx_nord) {
                C.add(sud);
            }
            else if(fx_sud = fx_nord){
           
                C.add(nord);
                C.add(sud);
            } else {
    			C.add(nord);
            }

        }

		//cas général
        else {
            fx_nord =fx(nord.i, nord.j);
            fx_ouest = fx(ouest.i,ouest.j))
            fx_sud = fx(sud.i, sud.j);
            fx_est = fx(est.i,est.j);
		
	    //cas où sud et sud sont à la même distances du point
            if(fx_sud = fx_est){

                if {fx_nord < fx_sud) {
                	if(fx_ouest < fx_nord){
                    		C.add(ouest);
                    }
			}
			else if(fx_ouest = fx_nord){
				C.add(ouest)
				C.add(nord);
			}
			else
				C.add(nord);
		}
		else if (fx_nord = f_sud) {
			if(fx_ouest < fx_nord){
                C.add(ouest);
			}
			else if(fx_ouest = fx_nord){
				C.add(ouest)
				C.add(nord);
				C.add(sud);
				C.add(est)
			}
			else {
				C.add(nord);
				C.add(sud);
				C.add(est);
                	}
		}
		else{
			if(fx_ouest<fx_sud)
				C.add(ouest);
			else if(fx_ouest > fx_sud){
				C.add(sud);
				C.add(est);
			}
			else {
				C.add(sud);
				C.add(est);
				C.add(ouest);
			}
		}
	}
        //cas où sud > est
	else if(fx_est < fx_sud){
		 if {fx_nord < fx_est) {
                	if(fx_ouest < fx_nord){
                    		C.add(ouest);
			}
			else if(fx_ouest = fx_nord){
				C.add(ouest)
				C.add(nord);
			}
			else
				C.add(nord);
		}
		else if (fx_nord = f_est) {
			if(fx_ouest < fx_nord){
                    		C.add(ouest);
			}
			else if(fx_ouest = fx_nord){
				C.add(ouest)
				C.add(nord);
				C.add(est)
			}
			else {
				C.add(nord);
				C.add(est);
                	}
		}
		else{
			if(fx_ouest<fx_est)
				C.add(ouest);
			else if(fx_ouest > fx_est){
				C.add(est);
			}
			else {
				C.add(est);
				C.add(ouest);
			}
		}
        }
        //cas où fx_sud < fx_est
	else if (fx_est > fx_sud){
                if {fx_nord < fx_sud) {
                	if(fx_ouest < fx_nord){
                    		C.add(ouest);
			}
			else if(fx_ouest = fx_nord){
				C.add(ouest)
				C.add(nord);
			}
			else
				C.add(nord);
		}
		else if (fx_nord = f_sud) {
			if(fx_ouest < fx_nord){
                    		C.add(ouest);
			}
			else if(fx_ouest = fx_nord){
				C.add(ouest)
				C.add(nord);
				C.add(sud);
			}
			else {
				C.add(nord);
				C.add(sud);
                	}
		}
		else{
			if(fx_ouest<fx_sud)
				C.add(ouest);
			else if(fx_ouest > fx_sud){
				C.add(sud);
			}
			else {
				C.add(sud);
				C.add(ouest);
			}
		}
	}

            
    }
}
}
}
}

    

		
		

    public Dijkstra (){
        int p = 99999;
	int pz;
        file_a_priorite Fap = new file_a_priorite();
        int [] P = new int[max(t.hauteur,t.largeur)+1];
        Fap.inserer(saci,sacj,0);
        Couple c, suc;
	 //insérer init de P(x)
	ArrayList<Couple> succ= new ArrayList<Couple>;
        do {
        	c = Fap.extraire;
		succ= successeurs(c.j,c.i);
		for(int i = 0; i < succ.size(); i++){
			suc=remove(0);
			pz = fx(c.i,c.j) + 1;
			if pz < P(z) {
				P(z) =pz;
				Fap.inserer(Z.i,Z.j,pz);
			}
		}
        }while(y = (buti, butj) );



    }






    public boolean existe_chemin(int i, int j, int buty, int butx){

        boolean found = false;


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
