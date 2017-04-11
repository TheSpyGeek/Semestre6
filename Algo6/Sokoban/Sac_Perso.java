class Sac_Perso {
    public Couple sac;
    public Couple perso;
    public int poids;
    public int distance;

    public Sac_Perso(Couple s, Couple p, int poi, int d){
        this.sac = s;
        this.perso = p;
        this.poids = poi;
        this.distance = d;
    }

    public Sac_Perso(int si, int sj, int pi, int pj, int poi, int d){
        this.sac = new Couple(si, sj);
        this.perso = new Couple(pi, pj);
        this.poids = poi;
        this.distance = d;
    }

    public String toString(){
        String renvoi = "";

        renvoi += "Sac ("+sac.i+", "+sac.j+") Perso ("+perso.i+", "+perso.j+") Poids : "+poids+" Dist : "+distance;
        return renvoi;
    }
}
