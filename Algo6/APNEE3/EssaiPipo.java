import java.io.*;

//
// args : CRexemple NbdeMots
//

class EssaiPipo {
    public static void main(String [] args) {
        Pipo p;
        if(args.length < 2){
            System.out.println("Veuillez entrer un nom de fichier d apprentissage et un nombre de mot pour la creation");
        } else {
            try {
                p = new Pipo(args[0]);
                p.Learn();
                //p.Talk(Integer.parseInt(args[1]));
                p.max();
            } catch (Exception e) {
                System.out.println(e);
            }
            
        }
   }
}