import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by braunern on 16/12/2015.
 *
 * Classe qui contient le main pour faire les tests
 */
public class Executable {
    public static void main(String[] args)  {

        int s = 0;

        try{
            InputStream input = new FileInputStream("maison.txt");

            Graphe g = Graphe.read(input);
            
            // test toString()
            System.out.println(g);

            // test nbEdges
            System.out.printf("le nombre d'arêtes est %d\n" , g.nbAretes());
            System.out.println("Le degre de "+s+" est "+g.degre(s));
            System.out.println("Le degre max est "+g.maxDegre());
            System.out.println("Matrice Adjacence : ");
            aff_tab(g.matriceAdj());

            System.out.println("Matrice incidence : ");
            aff_tab(g.matriceInc());

            System.out.println(g);
            System.out.println(g.complementaire());

            System.out.println("K5 : "+Graphe.graphe_complet(5));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    // pour afficher les matrices
    static void aff_tab(int tab[][]){
        for(int i=0; i<tab.length; i++){
            for(int j=0; j<tab[i].length; j++){
                System.out.print(tab[i][j]+ "");
            }
            System.out.println();
        }
    }

}
