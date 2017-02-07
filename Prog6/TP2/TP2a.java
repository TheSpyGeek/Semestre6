/*
 * Armoroides - casse briques à but pédagogique
 * Copyright (C) 2016 Guillaume Huard
 * 
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 * 
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 * 
 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Ensembles.Ensemble;
import Ensembles.FabriqueEnsemble;
import Ensembles.Iterateur;
import java.util.Random;

public class TP2a {

    static Random r;

    static void testEnsemble(String[] args, FabriqueEnsemble f) {
        Ensemble<Integer> ens = f.nouveau();

        System.out.println("On remplit l'ensemble avec 10 entiers...");
        for (int i = 0; i < 10; i++) {
            ens.ajoute(i);
        }
        System.out.println("On le parcourt en y effectuant des opérations aléatoires :");

        Iterateur<Integer> it = ens.iterateur();
        try {
            it.supprime();
            assert (false); // bug
        } catch (Exception e) {
            System.out.println("Suppression impossible avant toute utilisation de l'itérateur");
        }
        while (it.aProchain()) {
            Integer i = it.prochain();
            if (r.nextBoolean()) {
                it.supprime();
                System.out.println(i + " supprimé");
                try {
                    it.supprime();
                    assert (false); // bug
                } catch (Exception e) {
                    System.out.println("Double suppression impossible");
                }
            } else {
                System.out.println(i + " passé");
            }
        }

        it = ens.iterateur();
        System.out.println("Parcours des paires d'éléments :");
        while (it.aProchain()) {
            Integer i = it.prochain();
            Iterateur<Integer> it2 = it.clone();

            while (it2.aProchain()) {
                Integer i2 = it2.prochain();
                System.out.println("Paire : {" + i + "," + i2 + "} examinée");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            r = new Random(Integer.parseInt(args[0]));
        } else if (r == null) {
            r = new Random();
        }

        FabriqueEnsemble.init(Configuration.proprietes());
        testEnsemble(args, FabriqueEnsemble.grand());
        testEnsemble(args, FabriqueEnsemble.petit());
    }
}
