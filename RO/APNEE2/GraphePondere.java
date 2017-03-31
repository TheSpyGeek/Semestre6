import java.io.*;
import java.util.*;

/**
 * Classe qui implémente des graphes non orientés pondérés
 *
 * @author Nadia Brauner
 */
public class GraphePondere {

    private final int n; // nombre de sommets. Ne peut etre assigné qu'une fois
    private int m ; // nombre d'arêtes du graphe

    private ArrayList<LinkedList<Arete>> adjacence; // listes d'arêtes incidentes à chaque sommet

    /**
     * Initialise l'instance de graphe avec {@code n} sommets
     *
     * @param n le nombre de sommets du graphe. {@code n} doit etre strictement positif.
     * @throws IllegalArgumentException si {@code n} est négatif ou nul
     */
    public GraphePondere(int n) {
        if (n < 1)
            throw new IllegalArgumentException("le nombre de sommets doit etre strictement positif");
        this.n = n;
        this.m = 0 ;
        this.adjacence = new ArrayList<LinkedList<Arete>>(n);
        for (int i = 0; i < n; i++)
            adjacence.add(new LinkedList<Arete>());
    }

    /**
     * Construit un graphe à partir d'un flux de données (InputStream)
     *
     * @param input données du graphe à construire
     * @return un graphe
     * @throws GrapheReaderException en cas d'erreur de lecture de {@code input}
     */
    public static GraphePondere read(final InputStream input) throws GrapheReaderException {
        return new GPReader().read(input);
    }

    /**
     * renvoie le nombre de sommets du graphe
     *
     * @return le nombre de sommets (toujours positif)
     */
    public int getN() {
        return this.n;
    }

    /**
     * renvoie le nombre d'aretes du graphe
     * @return le nombre d'aretes
     */
    public int getM() {
        return this.m;
    }

    /**
     * ajoute l'arête {@code e} au graphe <BR>
     * On peut rajouter une arête déjà existante ou faire des boucles !
     *
     * @param e arête à rajouter au graphe
     * @throws IllegalArgumentException si {@code e} n'est pas une arête valide
     */
    public void ajouteArete(Arete e){

        verifieSommet(e.unSommet());
        verifieSommet(e.autreSommet(e.unSommet()));

        this.adjacence.get(e.unSommet()).add(e);
        this.adjacence.get(e.autreSommet(e.unSommet())).add(e);
        m++ ;
    }

    /**
     * vérifie que le sommet {@code v} est un sommet valide du graphe
     *
     * @param v sommet du graphe à vérifier
     * @throws IllegalArgumentException si le sommet n'est pas valide
     */
    private void verifieSommet(int v) {
        if (v >= getN() || v < 0)
            throw new IllegalArgumentException(String.format("Le sommet %d n'est pas valide", v));
    }

    /**
     * Renvoie la liste des voisins du sommet {@code v}
     * Cette liste ne peut pas être modifiée
     *
     * @param v sommet du graphe
     * @return la liste des voisins du sommet {@code v}
     * @throws IllegalArgumentException si le sommet n'est pas valide
     */
    public List<Arete> voisins(int v) {
        verifieSommet(v);

        return Collections.unmodifiableList(adjacence.get(v));
    }

    /**
     *
     * @return un arbre couvrant de poids min avec l'algorithme de Kruskal
     */
    public GraphePondere kruskal(){

        Arete a;
        int poids = 0;

        GraphePondere G = new GraphePondere(this.n);

        Partition P =  new Partition(this.getN());

        ArrayList<LinkedList<Arete>> listeArete = this.adjacence;

        while(!G.sommet_couvert()){

            System.out.println("Passe");

            a = this.arete_poids_min(listeArete);

            if(!P.sontConnectes(a.unSommet(), a.autreSommet(a.unSommet()))){
                G.ajouteArete(a);
                P.unir(a.unSommet(), a.autreSommet(a.unSommet()));
                listeArete = suppression_arete(listeArete, a);
                poids += a.getPoids();
                System.out.println(a);
            }


        }

        System.out.println("Poids : "+poids);

        return G;
    } 

    /// renvoie l'arete de poids minimal
    public Arete arete_poids_min(ArrayList<LinkedList<Arete>> list){

        Arete min = new Arete(0, 0, 1000000000);

        for(int i=0; i<list.size(); i++){

            for(int j=0; j<list.get(i).size(); j++){

                if(list.get(i).get(j).getPoids() < min.getPoids()){
                    min = list.get(i).get(j);
                }
            }
        }

        return min;
    }

    public ArrayList<LinkedList<Arete>> suppression_arete(ArrayList<LinkedList<Arete>> list, Arete a){

        ArrayList<LinkedList<Arete>> renvoi = list;


        // LinkedList<Arete> renvoi.get(a.unSommet()) = renvoi.get(a.unSommet());
        // LinkedList<Arete> renvoi.get(a.autreSommet(a.unSommet())) = renvoi.get(a.autreSommet(a.unSommet()));         

        int i=0;
        while(i<renvoi.get(a.unSommet()).size() && !a.equals(renvoi.get(a.unSommet()).get(i))){
            i++;
        }
        if(i<renvoi.get(a.unSommet()).size()){
            renvoi.get(a.unSommet()).remove(i);
        }

        i=0;
        while(i<renvoi.get(a.autreSommet(a.unSommet())).size() && !a.equals(renvoi.get(a.autreSommet(a.unSommet())).get(i))){
            i++;
        }
        if(i<renvoi.get(a.autreSommet(a.unSommet())).size()){
            renvoi.get(a.autreSommet(a.unSommet())).remove(i);
        }

        return renvoi;
    }


    /// renvoi si le graphe est couvrant
    private boolean sommet_couvert(){

        boolean [] tab = new boolean[this.n];
        for(int i=0; i<this.n; i++){
            tab[i] = false;
        }

        for(int i=0; i<this.n; i++){
            for(int j=0; j<this.adjacence.get(i).size(); j++){
                Arete a = this.adjacence.get(i).get(j);
                tab[a.unSommet()] = true;
                tab[a.autreSommet(a.unSommet())] = true;
            }
        }

        for(int i=0; i<this.n; i++){
            if(!tab[i]){
                return false;
            }
        }
        return true;
    }

    private  static final class GPReader {

        public GraphePondere read(final InputStream input) throws GrapheReaderException {
            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(input)); {

                final GraphePondere g;
                String line;

                String[] items = readNextLine(reader);

                if ((items == null) || (items.length < 2) || !"w".equals(items[0]))
                    throw new GrapheReaderException("il manque la ligne w",null);

                g = new GraphePondere(toInt(items[2]));


                while (items != null) {
                    items = readNextLine(reader);
                    if ((items != null) && (items.length > 0) && "e".equals(items[0])) {
                        g.ajouteArete(new Arete(toInt(items[1]) , toInt(items[2]) , Double.parseDouble(items[3])));
                    }
                }
                return g;
            }
            } catch (IOException e) {
                throw new GrapheReaderException(e.getMessage(),e);
            }
        }

        private String[] readNextLine(BufferedReader reader) throws IOException {
            String line = reader.readLine();
            while ((line != null) && line.startsWith("c")) {
                line = reader.readLine();
            }
            return (line == null) ? null : line.split("\\s+");
        }


        private static int toInt(String s) throws GrapheReaderException {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                throw new GrapheReaderException(String.format("'%1$s' n'est pas un entier", s),e) ;
            }
        }
    }

    public static class GrapheReaderException extends Exception {

        public GrapheReaderException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
