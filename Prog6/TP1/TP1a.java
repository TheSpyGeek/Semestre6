/*
 * Armoroides - casse briques Ã  but pÃ©dagogique
 * Copyright (C) 2016 Guillaume Huard
 * 
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique GÃ©nÃ©rale GNU publiÃ©e par la
 * Free Software Foundation (version 2 ou bien toute autre version ultÃ©rieure
 * choisie par vous).
 * 
 * Ce programme est distribuÃ© car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spÃ©cifique. Reportez-vous Ã  la
 * Licence Publique GÃ©nÃ©rale GNU pour plus de dÃ©tails.
 * 
 * Vous devez avoir reÃ§u une copie de la Licence Publique GÃ©nÃ©rale
 * GNU en mÃªme temps que ce programme ; si ce n'est pas le cas, Ã©crivez Ã  la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * Ã‰tats-Unis.
 * 
 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'HÃ¨res
 */

import java.util.Random;

public class TP1a {
    static Random r;

    static void testEnsemble(String [] args, EnsembleEntiers e) {
        if (args.length > 0) 
            r = new Random(Integer.parseInt(args[0]));
        else
            if (r == null)
                r = new Random();
        
        System.out.println("Entiers :");
        for (int i=0; i<100; i++) {
            int entier = r.nextInt(100);
            if (entier == 42)
                System.out.print("*** " + entier + " *** ");
            else
                System.out.print(entier + " ");
            e.ajoute(entier);
        }
        System.out.println();
        try {
            while (true) {
                e.supprime(42);
                System.out.println("42 trouvÃ©, on l'enlÃ¨ve de l'ensemble");
            }
        } catch (Exception ex) {
            System.out.println("Plus de 42, on affiche");
        }
        System.out.println("Ensemble : "+e);
    }

    public static void main(String [] args) throws Exception {
        EnsembleEntiers e;
        
        e = new EnsembleListeEntiers();
        testEnsemble(args, e);
    }
}