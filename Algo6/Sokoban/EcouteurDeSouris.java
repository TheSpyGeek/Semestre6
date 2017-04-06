
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import Dessin.Fenetre;

class EcouteurDeSouris implements MouseListener {
    Fenetre f;
    TerrainGraphique tg;
    Moteur m;

    EcouteurDeSouris(Fenetre f, TerrainGraphique tg, Moteur m) {
        this.f = f;
        this.tg = tg;
        this.m = m;
    }

    public void mousePressed(MouseEvent e) {
        int x,y;

        x = tg.calculeColonne(e.getX());
        y = tg.calculeLigne(e.getY());
        if (m.action(y, x)) {
            // Exemple d'utilisation du statut d'une case : plus on passe par
            // une case, plus celle-ci est foncee.
            //tg.setStatut(tg.getStatut(y,x).darker(),y,x);
            f.tracerSansDelai(tg);
        }
    }

    // Il faut aussi une implementation pour les autres methodes de l'interface
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
