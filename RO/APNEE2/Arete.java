/**
 * Classe qui modélise une arête entre les sommet {@code v} et {@code w} avec un {@code poids}
 *
 * @author Nadia Brauner
 *
 */

public class Arete implements Comparable<Arete> {

    private final int v ;
    private final int w ;
    private final double poids ;

    /**
     * Initialise une instance d'arête pondérée entre les sommets en paramètre
     *
     * @param v indice d'un des sommets de l'arête
     *           doit être un nombre positif
     * @param w indice de l'autre sommet de l'arête
     *          doit être un nombre positif
     * @param poids poids de l'arête
     *
     * @exception IndexOutOfBoundsException si les indices de sommet {@code v} et {@code w} sont négatifs
     * @exception IllegalArgumentException si le poids n'est pas un nombre
     *
     */
    public Arete(int v, int w, double poids) {
        if (v < 0) throw new IndexOutOfBoundsException("Le nom d'un sommet doit etre un entier non negatif");
        if (w < 0) throw new IndexOutOfBoundsException("Le nom d'un sommet doit etre un entier non negatif");
        if (Double.isNaN(poids)) throw new IllegalArgumentException("le poids n'est pas un nombre");
        this.v = v;
        this.w = w;
        this.poids = poids;
    }

    /**
     * Renvoie le poids de l'arête
     *
     * @return le poids de l'arête
     */
    public double getPoids() {
        return poids;
    }

    /**
     * Renvoie l'une des extrémités de l'arête
     *
     * @return l'un des extrémités de l'arête
     */
    public int unSommet(){
        return v ;
    }

    /**
     * Renvoie l'extremité de l'arête qui n'est pas {@code i}
     *
     * @return  l'extremité de l'arête qui n'est pas {@code i}
     * @exception IllegalArgumentException si {@code i} n'est pas l'une des extremités de l'arête
     */
    public int autreSommet(int i){
        if (v==i) return w;
        else if (w==i) return v ;
        else throw new IllegalArgumentException(i+ "n'est pas une extremité de l'arête");
    }

    @Override
    public int compareTo(Arete that) {
        if      (this.getPoids() < that.getPoids()) return -1;
        else if (this.getPoids() > that.getPoids()) return +1;
        else                                        return  0;
    }

    public String toString(){
        return v+" "+ w +": " +poids ;
    }
}
