/*
 * Armoroides - casse briques à but pédagogique
 * Copyright (C) 2016 Guillaume Huard

 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).

 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.

 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.

 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */
public class Bonus implements ComposantGraphique {    
    public static String [] natures = {
        "Elargit",
        "Retrecit",
        "Multiballes",
        "Laser"
    };
    int nature;
    float x, y;
    
    static int findType(String name) {
        for (int i=0; i<natures.length; i++)
            if (name.equals(natures[i]))
                return i;
        return -1;
    }
    
    Bonus(int t, float x, float y) {
        nature = t;
        this.x = x;
        this.y = y;
    }

    Bonus(int t) {
        this(t, 0, 0);
    }
    
    public ComposantGraphique copieVers(float x, float y) {
        return new Bonus(nature, x, y);
    }
    
    public String toString() {
        return "Bonus en ("+x+", "+y+"), nature "+natures[nature];
    }
}
