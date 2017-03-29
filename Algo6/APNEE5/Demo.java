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
import java.util.ArrayList;

import java.util.*;

public class Demo {

    public static void main(Fenetre f, String [] args) {
        Random r = new Random();

         int nb_point = 30;

         ArrayList<Point> enveloppe;
         Point [] nuage;

        // /* Point pour l'enveloppe RANDOM */
        nuage = new Point[nb_point];

        for (int i=0; i<nb_point; i++) {
            int x = r.nextInt(f.largeur()-20)+10;
            int y = r.nextInt(f.hauteur()-20)+10;
            Point p = new Point(x, y);
            nuage[i] = p;
            f.tracerSansDelai(p);
        }


        /* Point pour tester l'alignement */

        // nb_point = 9;

        // nuage = new Point[nb_point];

        // nuage[0] = new Point(200, 200);
        // nuage[1] = new Point(150, 200);
        // nuage[2] = new Point(350, 350);
        // nuage[3] = new Point(50, 200);
        // nuage[4] = new Point(280, 280);
        // nuage[5] = new Point(300, 300);
        // nuage[6] = new Point(200, 290);
        // nuage[7] = new Point(200, 250);
        // nuage[8] = new Point(150, 210);

        // for(int i=0; i<nuage.length; i++){
        //     f.tracerSansDelai(nuage[i]);
        // }

        /* Points pour tester segment 3 points */

        // nb_point = 3;
        // nuage = new Point[nb_point];

        // nuage[0] = new Point(50, 200);
        // nuage[1] = new Point(100, 200);
        // nuage[2] = new Point(150, 200);
        // //nuage[3] = new Point(100, 250);

        // for(int i=0; i<nuage.length; i++){
        //     f.tracerSansDelai(nuage[i]);
        // }

        /* algorithme de Jarvis */

        enveloppe = algo_jarvis(f, nuage);

        System.out.println("Enveloppe : "+enveloppe);
        


    }

    static ArrayList<Point> algo_jarvis(Fenetre f, Point [] nuage){

        Point P0 = nuage[0];

        Point Pcourant;

        ArrayList<Point> enveloppe = new ArrayList<Point>();

        P0 = nuage[0];

        // calcul du plus bas
        for(int i=0; i<nuage.length; i++){
            if(nuage[i].y < P0.y){
                P0 = nuage[i];
            } else if(nuage[i].y == P0.y && nuage[i].x < P0.x){
                P0 = nuage[i];
            }
        }
        System.out.println("Le point avec la plus petite ordonnée est : "+P0);

        /// CALCUL DE l'enveloppe

        Pcourant = P0;
        Point P, Pprime;

        do {
            enveloppe.add(Pcourant);

            P = choisir(nuage, enveloppe);

            for(int i=0; i<nuage.length; i++){
                Pprime = nuage[i];

                // on trace pour montrer ce qu'on teste
                f.tracer(new Segment(P.x, P.y, Pprime.x, Pprime.y));
                f.effacer(new Segment(P.x, P.y, Pprime.x, Pprime.y));

                if(EstAGauche(P, Pcourant, Pprime)){
                    P = Pprime;
                } else if(P.x == Pcourant.x && Pprime.x == Pcourant.x || P.y == Pcourant.y && Pprime.y == Pcourant.y){
                    System.out.println("Points alignés");
                    P = Pprime;
                }
            }
            f.tracer(new Segment(Pcourant.x, Pcourant.y, P.x,P.y));
            Pcourant = P;


        } while(Pcourant != P0);

        /// Traçage de l'enveloppe

        // int i;
        // for(i=1; i<enveloppe.size(); i++){
        //     f.tracer(new Segment(enveloppe.get(i-1).x, enveloppe.get(i-1).y, enveloppe.get(i).x, enveloppe.get(i).y));
        // }
        // f.tracer(new Segment(enveloppe.get(i-1).x, enveloppe.get(i-1).y, enveloppe.get(0).x, enveloppe.get(0).y));

        return enveloppe;
    }

    static boolean EstAGauche(Point A, Point B, Point C){

        int XAB, YAC, XAC, YAB;

        XAB = B.x - A.x;
        YAC = C.y - A.y;
        XAC = C.x - A.x;
        YAB = B.y - A.y;

        return (XAB*YAC - XAC*YAB) < 0;
    }


    /* La fonction choisir renvoie un point au hasard présent dans le nuage de points
        mais non présent dans l'enveloppe */

    static Point choisir(Point [] nuage, ArrayList<Point> enveloppe){

        Random r = new Random();
        Point p;

        do {
            p = nuage[r.nextInt(nuage.length)];
        } while(enveloppe.contains(p));

        return p;
    }

}