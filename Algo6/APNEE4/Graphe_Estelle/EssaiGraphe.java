import java.io.*;

class EssaiGraphe {
    public static void main(String [] args) {
        FileInputStream f;
        Graphe g;
		Graphe m = null;

        try {
            f = new FileInputStream(args[0]);
            g = new Graphe(f);
			m = couplage(g);
            System.out.println("Le graphe original est :");
            System.out.println(g);
            System.out.println("Son degre est : " + g.degre());
            if (g.arcsIndependants())
                System.out.println("Ses arcs sont independants");
            else
                System.out.println("Ses arcs ne sont pas independants");
            if (g.estCouplage())
                System.out.println("C'est un couplage");
            else
                System.out.println("Ce n'est pas un couplage");

			if(m!=null){
				System.out.println("Le graphe couplage trouvé est :");
		        System.out.println(m);
			}
			else
				System.out.println("aucun couplage parfait n'a pu être trouvé");
        } catch (Exception e) {
            System.out.println(e);
        }
           
   }
}
