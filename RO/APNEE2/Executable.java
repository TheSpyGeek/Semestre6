import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Executable {

    public static void main(String[] args) {
        try {
            InputStream input = new FileInputStream("./maisonUnitaire.txt");

            GraphePondere g1 = GraphePondere.read(input);

            GraphePondere couvrant_g1 = g1.kruskal();


        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
