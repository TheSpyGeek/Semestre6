
import java.awt.event.*;

class EcouteurDeClavier implements KeyListener {
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            System.out.println("Up");
            break;
        case KeyEvent.VK_RIGHT:
            System.out.println("Right");
            break;
        case KeyEvent.VK_DOWN:
            System.out.println("Down");
            break;
        case KeyEvent.VK_LEFT:
            System.out.println("Left");
            break;
        default:
            System.out.println(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
