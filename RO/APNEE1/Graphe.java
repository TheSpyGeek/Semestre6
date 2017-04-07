import java.io.*;
import java.util.*;

/**
 * Classe qui implémente des graphes non orientés éventuellement avec boucles et arêtes multiples <BR>
 * implémentation sous forme de ArrayList avec dans chaque case la listes des voisins
 *
 * @author Nadia Brauner
 *
 */
public class Graphe {

    private final int n; // nombre de sommets. Ne peut être assigné qu'une fois

    private ArrayList<LinkedList<Integer>> adjacence; // les arêtes en listes de voisins

    /**
     * Initialise l'instance de graphe avec {@code n} sommets
     *
     * @param n le nombre de sommets du graphe. {@code n} doit être strictement positif.
     * @throws IllegalArgumentException si {@code n} est négatif ou nul
     */
    public Graphe(int n) {
        if (n < 1)
            throw new IllegalArgumentException("le nombre de sommets doit etre strictement positif");
        this.n = n;
        this.adjacence = new ArrayList<LinkedList<Integer>>(n);
        for (int i = 0; i < n; i++)
            adjacence.add(new LinkedList<Integer>());
        // ajoute la LinkedList a la liste adjacence
    }

    /**
     * Construit un graphe à partir d'un flux de données (InputStream)
     *
     * @param input données du graphe à construire
     * @return un graphe
     * @throws GrapheReaderException en cas d'erreur de lecture de {@code input}
     */
    public static Graphe read(final InputStream input) throws GrapheReaderException {
        return new Reader().read(input);
    }

    /**
     * Ajoute l'arete {@code i}{@code j} au graphe.<BR>
     * On peut rajouter une arête déjà existante ou faire des boucles !
     *
     * @param i sommet extremité de l'arête
     * @param j sommet extremité de l'arête
     * @throws IllegalArgumentException si {@code i} ou {@code j} n'est pas un sommet valide
     */
    public void ajouteArete(int i, int j) {
        // graphe non orienté. Si on a l'arête ij alors, on a aussi ji
        verifieSommet(i);
        verifieSommet(j);
        this.adjacence.get(i).add(j);
        this.adjacence.get(j).add(i);
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
     * vérifie que le sommet {@code v} est un sommet valide du graphe
     *
     * @param v sommet du graphe à verifier
     * @throws IllegalArgumentException si le sommet n'est pas valide
     */
    private void verifieSommet(int v) {
        if (v >= getN() || v < 0)
            throw new IllegalArgumentException(String.format("Le sommet %d n'est pas valide", v));
    }


    /**
     * Renvoie la liste des voisins du sommet {@code v}.<BR>
     * Cette liste ne peut pas etre modifiée
     *
     * @param v sommet du graphe
     * @return la liste des voisins du sommet {@code v}
     * @throws IllegalArgumentException si le sommet n'est pas valide
     */
    public List<Integer> voisins(int v) {
        verifieSommet(v);

        return Collections.unmodifiableList(adjacence.get(v));
    }
    
     /**
     * Renvoie vrai si et seulement si {@code u} et {@code v} sont voisins
     *
     * @param u un sommet du graphe
     * @param v un sommet du graphe
     * @return true si {@code u} et {@code v} sont voisins, false sinon
     */
    public boolean sontVoisins(int u, int v) {
        return (adjacence.get(u).contains(v) || adjacence.get(v).contains(u));
    }
    
    /**
     * Calcule le degré du sommet {@code v}
     *
     * @param v sommet du graphe
     * @return le degré de {@code v}
     * @throws IllegalArgumentException si {@code v} n'est pas un sommet valide
     */
    public int degre(int v) {
        
        return this.adjacence.get(v).size();
    }

    /**
     * Calcule le  degré maximum des sommets du graphe
     *
     * @return le degré maximum des sommets du graphe
     */
    public int maxDegre() {
        // TODO
        int max = 0;
        int deg;

        for(int i=0; i<n;i++){
            deg = this.degre(i);
            if(deg > max){
                max = deg;
            }
        }

        return max;
    }


    /**
     * Calcule le nombre d'arêtes à partir de la somme des degrés
     *
     * @return la somme des degrés divisée par 2
     */
    public int nbAretes() {
        // TODO
        int sum = 0;
        for(int i=0; i<n; i++){
            sum += this.degre(i);
        }

        return sum/2;
    }
    
    /**
     * calcule la matrice d'adjacence du graphe
     *
     * @return la matrice d'adjacence du graphe
     */
    public int[][] matriceAdj() {
        // TODO
        int mat[][] = new int[n][n];
        ListIterator<Integer> it;
        int sommet;

        // initialisation de la matrice
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                mat[i][j] = 0;
            }
        }


        for(int i=0; i<n; i++){
            it = this.adjacence.get(i).listIterator(0);

            while(it.hasNext()){
                sommet = it.next();
                mat[i][sommet] = 1;
                mat[sommet][i] = 1;
            }
        }

        return mat;

    }

    @Override
    public String toString() {
        
        String ret = "";

        ret += "Nombre de sommet : "+this.n+"\n";
        ret += "Nombre d\'arretes : "+this.nbAretes()+"\n";
        ret += "Degrée max : "+this.maxDegre()+"\n";
        ret += "Arretes : \n";

        int mat[][] = this.matriceAdj();

        for(int i=0; i<this.n; i++){
            for(int j=0; j<=i; j++){
                if(mat[i][j] == 1){
                    ret += "{"+i+", "+j+"} ";
                }
            }
        }

        return ret;
    }

    /**
     * Calcule le graphe complémentaire
     *
     * @return le graphe complémentaire
     */
    public Graphe complementaire(){
        
        int mat[][] = this.matriceAdj();
        Graphe compl = new Graphe(this.n);



        for(int i=0; i<this.n; i++){
            for(int j=0; j<=i; j++){
                if(mat[i][j] == 0){
                    compl.ajouteArete(i, j);
                }
            }
        }

        return compl;
    }
    
    /**
     * Construit le graphe complet à {@code k} sommets
     *
     * @param k le nombre de sommets du graphe complet
     */
    public static Graphe graphe_complet(int k){
        
        Graphe complet = new Graphe(k);

        for(int i=0; i<k; i++){
            for(int j=0; j<i; j++){
                if(i != j){ // si on veut éviter les boucles sur les sommets 
                    complet.ajouteArete(i,j);
                }
            }
        }

        return complet;
    }
    
    /**
     * calcule la matrice d'incidence du graphe
     *
     * @return la matrice d'incidence du graphe
     */
    public int[][] matriceInc() {
            
        int mat_inc[][] = new int[n][this.nbAretes()];
        int mat_adj[][] = this.matriceAdj();
        int num_arrete = 0;

        for(int i=0; i<n; i++){
            for(int j=0; j<this.nbAretes(); j++){
                mat_inc[i][j] = 0;
            }
        }

        for(int i=0; i<this.n; i++){
            for(int j=0; j<=i; j++){

                if(mat_adj[i][j] == 1){
                    mat_inc[i][num_arrete] = 1;
                    mat_inc[j][num_arrete] = 1;
                    num_arrete++;
                }
            }
        }


        return mat_inc;
    }

     public static final class Reader {

        public Graphe read(final InputStream input) throws GrapheReaderException {
            try{
                final BufferedReader reader = new BufferedReader(new InputStreamReader(input)) ;

                final Graphe g;
                String line;

                String[] items = readNextLine(reader);

                if ((items == null) || (items.length < 2) || !"p".equals(items[0]))
                    throw new GrapheReaderException("il manque la ligne p",null);

                g = new Graphe(toInt(items[2]));

                while (items != null) {
                    items = readNextLine(reader);
                    if ((items != null) && (items.length > 0) && "e".equals(items[0])) {
                        g.ajouteArete(toInt(items[1]), toInt(items[2]));
                    }
                }
                return g;
            }
            catch (IOException e) {
               throw new GrapheReaderException(e.getMessage(),e) ;
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
