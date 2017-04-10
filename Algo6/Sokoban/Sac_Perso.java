class Sac_Perso {
    public Couple sac;
    public Couple perso;
    public int poids; 

    public Sac_Perso(Couple s, Couple p, int poi){
        this.sac = s;
        this.perso = p;
        this.poids = poi;
    }

    public Sac_Perso(int si, int sj, int pi, int pj, int poi){
        this.sac = new Couple(si, sj);
        this.perso = new Couple(pi, pj);
        this.poids = poi;
    }

    public String toString(){
        String renvoi = "";

        renvoi += "Sac ("+sac.i+", "+sac.j+") Perso ("+perso.i+", "+perso.j+") Poids : "+poids;
        return renvoi;
    }
}
