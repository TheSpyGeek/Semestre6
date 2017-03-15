import java.io.*;

class EssaiGraphe {
    public static void main(String [] args) {
        FileInputStream f;
        EnumP g;
        Maillon [] couplage;
        Maillon [] res;

        try {
            f = new FileInputStream(args[0]);
            g = new EnumP(f);
            System.out.println("Le graphe est :");
            System.out.println(g.aff_graphe());

            couplage = new Maillon[g.nombre_sommets()];
            couplage = EnumP.couplage_vide(g.nombre_sommets());

            System.out.println("Calcul...");
            res = new Maillon[g.nombre_sommets()];
            res = g.Enum(0, couplage, null);

            System.out.println("Couplage parfait : ");

            EnumP.aff_couplage(res);

            //g.test();
            
        } catch (Exception e) {
            System.out.println(e);
        }
           
   }
}
