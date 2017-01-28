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
class Brique implements ComposantGraphique {
    int resistance;
    int couleur;
    float x, y;
    
    Brique(int r, int c) {
        this(r, c, 0, 0);
    }
    
    Brique(int r, int c, float x, float y) {
        resistance = r;
        couleur = c;
        this.x = x;
        this.y = y;
    }
    
    public ComposantGraphique copieVers(float x, float y) {
        return new Brique(resistance, couleur, x, y);
    }    

    public String toString() {
        return "Brique en ("+x+", "+y+"), resistance "+resistance+", couleur "+couleur;
    }
}