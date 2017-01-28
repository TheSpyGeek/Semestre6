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
import java.io.InputStream;
import java.util.Scanner;

class ChargeurNiveaux {
    static int courant;
    static boolean initialise = false;
    static ComposantGraphique [] modeles;
    static String [] couleurs;
    static int nb_couleurs;
    static Scanner s;
    static String ligne;
    static int indentation;
    static String nom, valeur;
    
    static void init() {
        if (!initialise) {
            courant = 1;
            initialise = true;
        }
    }
 
    private static void lisLigne() {
        indentation = 0;
        if (s.hasNextLine()) {
            ligne = s.nextLine();
            while (ligne.charAt(indentation) == ' ')
                indentation++;
        } else
            ligne = null;
    }
    
    private static void trouveCouple() {
        String [] mots = ligne.split(":");
        assert(mots.length <= 2);
        nom = mots[0].trim();
        if (mots.length > 1)
            valeur = mots[1].trim();
        else
            valeur = null;
        lisLigne();
    }
    
    private static void trouveNom() {
        trouveCouple();
        assert(valeur == null);
    }

    private static char trouveCar(String val) {
        trouveCouple();
        if (nom.length() == 1) {
            if (valeur != val)
                assert(valeur.equals(val));
            char c = nom.charAt(0);
            return c;
        } else {
            assert(false);
            return '0';
        }
    }
    
    private static ComposantGraphique lisComposant() {
        ComposantGraphique c;
        int indentationCourante = indentation;
        trouveCouple();
        assert(nom.equals("type"));
        switch (valeur) {
            case "brique":
                int resistance = 1;
                couleurs[nb_couleurs] = "rgba(50%,50%,50%,1.0)";
                while (indentation >= indentationCourante) {
                    trouveCouple();
                    switch (nom) {
                        case "resistance":
                            resistance = Integer.parseInt(valeur);
                            break;
                        case "couleur":
                            couleurs[nb_couleurs] = valeur.trim();
                            break;
                        default:
                            assert(false);
                            break;
                    }
                }
                c = new Brique(resistance, nb_couleurs++);
                return c;
            case "bonus":
                int nature = -1;
                while (indentation >= indentationCourante) {
                    trouveCouple();
                    switch (nom) {
                        case "nature":
                            nature = Bonus.findType(valeur.trim());
                            if (nature == -1)
                                throw new RuntimeException("Bonus invalide : "+valeur);
                            break;
                        default:
                            assert(false);
                            break;
                    }
                }
                assert(nature != -1);
                c = new Bonus(nature);
                return c;
        }
        assert(false);
        return null;
    }
    
    private static void lisCouche(Niveau n, int couche) {
        int indentationCourante = indentation;
        boolean [] bordsVerticaux;
        
        int i = 0;
        int j = 0;
        bordsVerticaux = new boolean[(ligne.length()-indentationCourante)*2];
        while ((indentation >= indentationCourante) && (ligne != null)) {
            ligne = ligne.substring(indentationCourante);
            if (ligne.matches("^#*$")) {
                n.ajouteComposant(couche, new BordHorizontal(i, i==0?-1:1));
            } else {
                int indice = 0;
                j = 0;
                while (indice < ligne.length()) {
                    char car = ligne.charAt(indice);
                    switch (car) {
                        case '#':
                            if (!bordsVerticaux[j]) {                                                 
                            n.ajouteComposant(couche, new BordVertical(j, j==0?-1:1));
                            bordsVerticaux[j] = true;
                            }
                            break;
                        case '.':
                            break;
                        default:
                            if (car != ' ') {
                                ComposantGraphique nouveau;
                                nouveau = modeles[ligne.charAt(indice)].copieVers(j, i);
                                n.ajouteComposant(couche, nouveau);
                            }
                            j+=1;
                            break;
                    }
                    indice++;
                }
            }
            lisLigne();
            i++;
        }
        n.fixerDimensionsMax(j,i);
    }
    
    static Niveau prochainNiveau() {
        // Permet de trouver les fichier aussi bien dans le systÃ¨me de fichiers
        // que dans une archive jar
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("Niveaux/Niveau-"+courant+".txt");
        if (in == null)
            return null;
        else {
            courant++;
            s = new Scanner(in);
            return lisNiveau();
        }
    }
    
    static Niveau lisNiveau() {
        Niveau n = new Niveau("Niveau "+(courant-1),3);
        modeles = new ComposantGraphique[256];
        couleurs = new String[128];
        nb_couleurs = 0;

        lisLigne();
        while (ligne != null) {
            trouveNom();
            switch (nom) {
                case "Composants" :
                    int indentationCourante = indentation;
                    while (indentation >= indentationCourante) {
                        char c = trouveCar(null);
                        modeles[c] = lisComposant();
                    }
                    break;
                case "Couches" :
                    indentationCourante = indentation;
                    while (indentation >= indentationCourante) {
                        char c = trouveCar("|");
                        assert((c >= '0') && (c <= '9'));
                        int hauteur = Character.digit(c, 10);
                        lisCouche(n, hauteur);
                    }
                    break;
                default :
                    assert(false);
                    break;
            }
        }
        modeles = null;
        n.fixeCouleurs(couleurs, nb_couleurs);
        n.nouvelleBalle();
        return n;
    }
}