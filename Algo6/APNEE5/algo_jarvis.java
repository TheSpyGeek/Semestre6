
class algo_jarvis {

public static void main(Fenetre f, String [] args) {
        Random r = new Random();

         int nb_point = 30;

         ArrayList<Point> enveloppe;
         Point [] nuage;

        // /* Point pour l'enveloppe RANDOM */
        nuage = new Point[nb_point];

        for (int i=0; i<nb_point; i++) {
            int x = r.nextInt(f.largeur()-20)+10;
            int y = r.nextInt(f.hauteur()-20)+10;
            Point p = new Point(x, y);
            nuage[i] = p;
            f.tracerSansDelai(p);
        }



	      enveloppe = algo_jarvis(f, nuage);

        System.out.println("Enveloppe : "+enveloppe);
        
    }

    static ArrayList<Point> algo_jarvis(Fenetre f, Point [] nuage){

        Point P0 = nuage[0];

        Point Pcourant;

        ArrayList<Point> enveloppe = new ArrayList<Point>();

        P0 = nuage[0];

        // calcul du plus bas
        for(int i=0; i<nuage.length; i++){
            if(nuage[i].y < P0.y){
                P0 = nuage[i];
            } else if(nuage[i].y == P0.y && nuage[i].x < P0.x){
                P0 = nuage[i];
            }
        }
        System.out.println("Le point avec la plus petite ordonnée est : "+P0);

        /// CALCUL DE l'enveloppe

        Pcourant = P0;
        Point P, Pprime;

        do {
            enveloppe.add(Pcourant);

            P = choisir(nuage, enveloppe);

            for(int i=0; i<nuage.length; i++){
                Pprime = nuage[i];

                // on trace pour montrer ce qu'on teste
                f.tracer(new Segment(P.x, P.y, Pprime.x, Pprime.y));
                f.effacer(new Segment(P.x, P.y, Pprime.x, Pprime.y));

                if(EstAGauche(P, Pcourant, Pprime)){
                    P = Pprime;
                } else if(P.x == Pcourant.x && Pprime.x == Pcourant.x || P.y == Pcourant.y && Pprime.y == Pcourant.y){
                    System.out.println("Points alignés");
                    P = Pprime;
                }
            }
            f.tracer(new Segment(Pcourant.x, Pcourant.y, P.x,P.y));
            Pcourant = P;


        } while(Pcourant != P0);

        return enveloppe;
    }

    static boolean EstAGauche(Point A, Point B, Point C){

        int XAB, YAC, XAC, YAB;

        XAB = B.x - A.x;
        YAC = C.y - A.y;
        XAC = C.x - A.x;
        YAB = B.y - A.y;

        return (XAB*YAC - XAC*YAB) < 0;
    }


    /* La fonction choisir renvoie un point au hasard présent dans le nuage de points
        mais non présent dans l'enveloppe */

    static Point choisir(Point [] nuage, ArrayList<Point> enveloppe){

        Random r = new Random();
        Point p;

        do {
            p = nuage[r.nextInt(nuage.length)];
        } while(enveloppe.contains(p));

        return p;
    }
}