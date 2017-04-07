
import java.awt.event.*;
import Dessin.Fenetre;

class EcouteurDeClavier implements KeyListener {

    Fenetre f;
    Terrain t;
    TerrainGraphique tg;
    Moteur m;
    int lignePousseur, colonnePousseur;

    EcouteurDeClavier(Fenetre f, Terrain t, TerrainGraphique tg, Moteur m) {
        this.f = f;
        this.m = m;
        this.tg = tg;
        this.t = t;
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++)
                if (t.consulter(i,j).contient(Case.POUSSEUR)) {
                    lignePousseur = i;
                    colonnePousseur = j;
                    return;
                }
    }

    private boolean est_possible(int i, int j){
        return (j < t.largeur() && j >= 0 && i < t.hauteur() && i >= 0 && t.consulter(i,j).estLibre());
    }


    public void keyPressed(KeyEvent e) {
        int x = colonnePousseur;
        int y = lignePousseur;
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            System.out.println("Up");
            if (est_possible(y-1, x) && m.action(y-1, x)) {
                f.tracerSansDelai(tg);
                lignePousseur--;
            }
            break;
        case KeyEvent.VK_RIGHT:
            System.out.println("Right");
            if (est_possible(y, x+1) && m.action(y, x+1)) {
                f.tracerSansDelai(tg);
                colonnePousseur++;
            }
            break;
        case KeyEvent.VK_DOWN:
            System.out.println("Down");
            if (est_possible(y+1, x) && m.action(y+1, x)) {
                f.tracerSansDelai(tg);
                lignePousseur++;
            }
            break;
        case KeyEvent.VK_LEFT:
            System.out.println("Left");
            if (est_possible(y, x-1) && m.action(y, x-1)) {
                f.tracerSansDelai(tg);
                colonnePousseur--;
            }
            break;
        default:
            System.out.println(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
