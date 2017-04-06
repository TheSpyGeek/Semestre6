
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.net.URL;
import Dessin.ObjetGraphique;
import Dessin.Fenetre;

class TerrainGraphique implements ObjetGraphique {
    Fenetre f;
    Terrain t;
    BufferedImage pousseur, sac, but, pousseurSurBut, sacSurBut;

    private Color [][] statut;
    
    public Color getStatut(int I,int J) {
        return statut[I][J];
    }
    
    public void setStatut(Color i, int I, int J) {
        statut[I][J]=i;
    }

    
    private URL getImage(String nom) {
        ClassLoader cl = getClass().getClassLoader();
        return cl.getResource("Images/" + nom);
    }

    TerrainGraphique(Fenetre f, Terrain t) {
        this.f = f;
        this.t = t;
        statut = new Color [t.hauteur()][t.largeur()];
        
        for (int i=0; i<t.hauteur(); i++) {
            for (int j=0; j<t.largeur(); j++) {
                statut[i][j]=Color.white;
            }
        }

        try {
            pousseur = ImageIO.read(getImage("Pousseur.png"));
            sac = ImageIO.read(getImage("Sac.png"));
            but = ImageIO.read(getImage("But.png"));
            pousseurSurBut = ImageIO.read(getImage("PousseurSurBut.png"));
            sacSurBut = ImageIO.read(getImage("SacSurBut.png"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    // Clonage incomplet mais suffisant pour le paquetage de dessin
    public ObjetGraphique clone() {
        TerrainGraphique nt = new TerrainGraphique(f, t.clone());
        for (int i=0; i<t.hauteur(); i++) {
            for (int j=0; j<t.largeur(); j++) {
                nt.statut[i][j]=statut[i][j];
            }
        }
        
        return nt;

    }

    private int largeurCase() {
        return f.largeur() / t.largeur();
    }

    private int hauteurCase() {
        return f.hauteur() / t.hauteur();
    }

    public void draw(Graphics2D g) {
        // On efface tout
        g.setPaint(Color.white);
        g.fillRect(0, 0, f.largeur(), f.hauteur());
        g.setPaint(Color.black);

        int lC = largeurCase();
        int hC = hauteurCase();

        // On affiche les cases
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++) {
                int x,y;
                x = j*lC;
                y = i*hC;

                g.setPaint(statut[i][j]);
                g.fillRect(x, y, lC, hC);
                g.setPaint(Color.black);
                switch (t.consulter(i, j)) {
                case OBSTACLE:
                    g.fillRect(x, y, lC, hC);
                    break;
                case POUSSEUR:
                    g.drawImage(pousseur, x, y, lC, hC, null);
                    break;
                case POUSSEUR_SUR_BUT:
                    g.drawImage(pousseurSurBut, x, y, lC, hC, null);
                    break;
                case SAC:
                    g.drawImage(sac, x, y, lC, hC, null);
                    break;
                case SAC_SUR_BUT:
                    g.drawImage(sacSurBut, x, y, lC, hC, null);
                    break;
                case BUT:
                    g.drawImage(but, x, y, lC, hC, null);
                    break;
                }
            }
    }

    public int calculeLigne(int y) {
        return y / hauteurCase();
    }

    public int calculeColonne(int x) {
        return x / largeurCase();
    }

    public boolean equals(ObjetGraphique o) {
        TerrainGraphique autre = null;
        try {
            autre = (TerrainGraphique) o;
        } catch (ClassCastException e) {
            return false;
        }
        return t.equals(autre.t) && statut.equals(autre.statut);
    }

    public int key() {
        // Non utilise car un terrain n'est jamais efface (mais ecrase)
        return 0;
    }

    public String toString() {
        return t.toString();
    }
}
