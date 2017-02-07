class EnsembleTableauEntiers extends EnsembleEntiers{
    
    int [] elements;
    int nbElt;

    EnsembleTableauEntiers() {
        elements = new int [1];
        nbElt = 0;
    }

    private void growTab(){
        int [] ntab = new int [elements.length * 2];
        for(int i = 0; i < elements.length; i++) {
            ntab[i] = elements[i];
        }

        elements = ntab;
    }

    public void ajoute(int e) {
        if (nbElt == elements.length) {
            growTab();
        }
        elements[nbElt] = e;
        nbElt++;
    }
    
    private void swap2Elts(int idx1, int idx2) {
        int tmp;
        tmp = elements[idx1];
        elements[idx1] = elements[idx2];
        elements[idx2] = tmp;
    }

    public void supprime(int e) throws Exception {
        boolean found = false;
        for (int i = 0; i < nbElt; i++) {
            if (elements[i] == e) {
                swap2Elts(i,nbElt-1);
                found = true;
                nbElt--;
            }
        }
        if (!found) {
            throw new Exception(e + "not found.");
        }
    }

    public String toString(){
        String res;

        res = "Tableau : [ ";
        for (int i = 0; i < nbElt; i++) {
            res+= elements[i];
            if (i != nbElt - 1) {
                res+=", ";
            }
        }
        res +=" ]";
        
        return res;
    }
}
