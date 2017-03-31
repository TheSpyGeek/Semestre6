/**
 *
 * Structure Partition non optimisée
 * pour gérer des partitions d'un ensemble à {@code n} éléments
 *
 * @author Nadia Brauner
 */
public class Partition {
    private int[] parent ;

    /**
     * Initialise une structure Partition avec {@code n} éléments
     * La partition est composée de singletons
     *
     * @param n nombre d'éléments dans la partition
     * @throws IllegalArgumentException si le paramètre {@code n} est négatif
     */
    public Partition(int n){
        
        verifieIndex(n);

        parent = new int[n];

        for(int i=0; i<n; i++){
            parent[i] = i;
        }

    }

    /**
     * @return la référence de la classe de l'élément {@code i}
     * @throws IllegalArgumentException si le paramètre {@code i} n'est pas valide
     */
    public int trouver(int i){

        verifieIndex(i);

        int it = i;

        while(it != parent[it]){
            it = parent[it];
        }

        return it;
    }


    /**
     * Reunit les classes d'equivalence de {@code i} et {@code j}
     *
     * @param i
     * @param j
     *
     * @throws IllegalArgumentException si l'un des paramètres {@code i} ou {@code j} n'est pas valide
     */
    public void unir(int i , int j){

        this.verifieIndex(i);
        this.verifieIndex(j);

        int k =this.trouver(j);
        int l = this.trouver(i);

        this.parent[k] = l;

    }

    /**
     * indique si {@code i} et {@code j} sont dans la même composante connexe
     *
     * @param i
     * @param j
     *
     * @return true si {@code i} et {@code j} sont dans la même composante connexe
     * @throws IllegalArgumentException si l'un des paramètres {@code i} ou {@code j} n'est pas valide
     */
    public boolean sontConnectes(int i, int j){ 
        
        this.verifieIndex(i);
        this.verifieIndex(j);

        return (this.trouver(i) == this.trouver(j));       
    }

    /**
     * vérifie que {@code i} est un paramètre valide
     *
     * @param i
     *
     * @throws IllegalArgumentException sauf si 0 <= {@code i} < n
     *
     */
    private void verifieIndex(int i) throws IllegalArgumentException {
        if(i < 0){
            throw new IllegalArgumentException("Index Error");
        }
    }


    public String toString(){
        String renvoi = "";
        for(int i=0; i<parent.length; i++){
            renvoi += "p["+i+"]  :"+parent[i]+"\n";
        }

        return renvoi;
    }
}
