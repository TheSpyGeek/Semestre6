
enum Case {
    LIBRE(0), OBSTACLE(2), SAC(4), POUSSEUR(8), BUT(16), SAC_SUR_BUT(20),
    POUSSEUR_SUR_BUT(24), INVALIDE(32);

    int contenu;

    Case(int i) {
        contenu = i;
    }

    public boolean contient(Case c) {
        return (contenu & c.contenu) == c.contenu;
    }

    public boolean estLibre() {
        return (contenu == 0) || (contenu == 16);
    }

    public Case ajout(Case c) {
        switch (contenu | c.contenu) {
        case 4:
            return SAC;
        case 8:
            return POUSSEUR;
        case 20:
            return SAC_SUR_BUT;
        case 24:
            return POUSSEUR_SUR_BUT;
        default:
            throw new RuntimeException("Ajout de " + c + " sur " + this +
                      " impossible");
        }
    }

    public Case retrait(Case c) {
        switch (contenu & ~c.contenu) {
        case 0:
            return LIBRE;
        case 16:
            return BUT;
        default:
            throw new RuntimeException("Retrait de " + c + " de " + this +
                      " impossible");
        }
    }
}
