/*
Dessin - package de visualisation pas à pas d'algorithmes de dessin
Copyright (C) 2009 Guillaume Huard
Ce programme est libre, vous pouvez le redistribuer et/ou le modifier selon les
termes de la Licence Publique Générale GNU publiée par la Free Software
Foundation (version 2 ou bien toute autre version ultérieure choisie par vous).

Ce programme est distribué car potentiellement utile, mais SANS AUCUNE
GARANTIE, ni explicite ni implicite, y compris les garanties de
commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
Licence Publique Générale GNU pour plus de détails.

Vous devez avoir reçu une copie de la Licence Publique Générale GNU en même
temps que ce programme ; si ce n'est pas le cas, écrivez à la Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
États-Unis.

Contact: Guillaume.Huard@imag.fr
         ENSIMAG - Laboratoire LIG
         51 avenue Jean Kuntzmann
         38330 Montbonnot Saint-Martin
*/
import Dessin.*;
import java.awt.Color;
import java.util.*;

public class AlgoNaif {
    static final double PRECISION=1E-6;

    public static void main(Fenetre f, String [] args) {
        Random r = new Random();
        List<Point> l = new LinkedList<Point>();
        int nbPoints;

        // Reccuperation du nombre de points en argument (ou valeur par defaut)
        if (args.length > 0) {
            nbPoints = Integer.parseInt(args[0]);
        } else {
            nbPoints = 5;
        }

        // Generation du nuage avec une petite marge pour ne pas avoir de
        // points contre le bord de la fenetre
        for (int i=0; i<nbPoints; i++) {
            int x = r.nextInt(f.largeur()-20)+10;
            int y = r.nextInt(f.hauteur()-20)+10;
            Point p = new Point(x, y);
            l.add(p);
            f.tracerSansDelai(p);
        }

        // Algorithme naif : enumeration des segments
        for (int i=0; i<l.size(); i++)
            for (int j=i+1; j<l.size(); j++) {
                // Recuperation du segment et calcul de l'equation de droite
                Point a = (Point) l.get(i);
                Point b = (Point) l.get(j);
                double coef = 0;
                double cte = 0;
                boolean positifs = false, negatifs = false, vertical;
                Iterator it = l.listIterator();

                // Si la droite est verticale, on ne peut pas utiliser le
                // coefficient directeur (infini)
                if (a.x == b.x) {
                    vertical = true;
                } else {
                    vertical = false;
                    coef = (double) (a.y - b.y) / (a.x - b.x);
                    cte  = a.y - coef*a.x;
                }

                // On affiche le segment etudie
                f.tracer(new Segment(a.x, a.y, b.x, b.y, Color.red));
                // On consulte la position de tous les points
                while ((!positifs || !negatifs) && (it.hasNext())) {
                    double valeur;
                    Point p = (Point) it.next();
                    if (vertical) {
                        valeur = p.x - a.x;
                    } else {
                        valeur = p.x*coef + cte - p.y;
                    }

                    // Attention aux erreurs d'arrondi, on ignore les valeurs
                    // trop proches de 0, considerees comme etant des points
                    // sur la droite
                    if ((valeur < -PRECISION) || (valeur > PRECISION)) {
                        if (valeur < 0)
                            negatifs = true;
                        if (valeur > 0)
                            positifs = true;
                    }
                }
                if (positifs && negatifs) {
                    f.effacer(new Segment(a.x, a.y, b.x, b.y));
                } else {
                    f.tracer(new Segment(a.x, a.y, b.x, b.y));
                }
            }

        // Remarquons qu'a l'issue de l'algorithme, nous ne connaissons pas la
        // liste des points de l'enveloppe convexe, les segments qui en font
        // partie restent juste affiches a l'ecran. Cela ne correspond pas tout
        // a fait a la sortie attendue pour les algorithmes vus en TD.
    }
}