/*
 * Armoroides - casse briques Ã  but pÃ©dagogique
 * Copyright (C) 2016 Guillaume Huard

 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique GÃ©nÃ©rale GNU publiÃ©e par la
 * Free Software Foundation (version 2 ou bien toute autre version ultÃ©rieure
 * choisie par vous).

 * Ce programme est distribuÃ© car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spÃ©cifique. Reportez-vous Ã  la
 * Licence Publique GÃ©nÃ©rale GNU pour plus de dÃ©tails.

 * Vous devez avoir reÃ§u une copie de la Licence Publique GÃ©nÃ©rale
 * GNU en mÃªme temps que ce programme ; si ce n'est pas le cas, Ã©crivez Ã  la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * Ã‰tats-Unis.

 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'HÃ¨res
 */
package Ensembles;

import java.util.NoSuchElementException;

class EnsembleTableauEntiers<T> implements Ensemble<T> {

    Object[] contenu;
    int taille;

    EnsembleTableauEntiers() {
        this(16);
    }

    EnsembleTableauEntiers(int t) {
        taille = 0;
        contenu = new Object[t];
    }

    @Override
    public void ajoute(T c) {
        if (taille >= contenu.length) {
            Object[] nouveau = new Object[contenu.length * 2];
            System.arraycopy(contenu, 0, nouveau, 0, contenu.length);
            contenu = nouveau;
        }
        contenu[taille++] = c;
    }

    @Override
    public Iterateur<T> iterateur() {
        return new IterateurEnsembleTableauEntiers<T>(this);
    }

    @Override
    public void supprime(T c) {
        for (int i = 0; i < taille; i++) {
            if (contenu[i] == c) {
                taille--;
                contenu[i] = contenu[taille];
                contenu[taille] = null;
                return;
            }
        }
        throw new NoSuchElementException(c + " not found");
    }

    @Override
    public String toString() {
        String resultat = "{";
        int i = 0;

        if (i < taille) {
            resultat += contenu[i++];
        }
        while (i < taille) {
            resultat += ", " + contenu[i++];
        }
        return resultat + "}";
    }

}