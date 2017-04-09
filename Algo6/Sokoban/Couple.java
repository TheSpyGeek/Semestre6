class Couple {
    public int i;
    public int j;
    Couple(int i, int j){
        this.i = i;
        this.j = j;
    }

    public boolean same(Couple a, Couple b){
        return (a.i == b.i && a.j == b.j);
    }
}