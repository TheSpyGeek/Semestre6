import java.io.*;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.*; // pour projection


class Joiner {
    String f1,f2;
    PrintWriter out;

    Joiner(String f1, String f2, String res) {
        this.f1 = f1;
        this.f2 = f2;
        try {
        out = new PrintWriter(new FileOutputStream(res));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void ChangeOutput(String res) {
        try {
        out = new PrintWriter(new FileOutputStream(res));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void NestedJoin() {
        Scanner in1, in2;
        String t1,t2 ;
        String [] t2_splited;
        String [] t1_splited;
        
        long startTime = System.currentTimeMillis();
        try {
          in1 = new Scanner(new FileInputStream(f1)) ;
          in2 = new Scanner(new FileInputStream(f2));

          while (in1.hasNext()) {
              t1 = in1.nextLine();
              t1_splited = t1.split("\t");
              while (in2.hasNext()) {
                  t2 = in2.nextLine();
                  t2_splited = t2.split("\t");
                  if (t1_splited[1].equals(t2_splited[0])) {
                      out.print(t1+"\t");
                      out.println(t2);
                  }
              }
              in2.reset();
              in2= new Scanner(new FileInputStream(f2));
          }
          in1.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        out.close();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Computation time, nested loop join: "+totalTime+" ms");
    }
    
    public void HashJoin() {
        Scanner in1, in2;
        String t1,t2 ;
        String [] t2_splited;
        String [] t1_splited;

        
        Hashtable<String, String> hashed_t2 = new Hashtable<String, String>();

        long startTime = System.currentTimeMillis();
        

        ///  REMPLISSAGE DE LA TABLE DE HASH

        try {

          in2 = new Scanner(new FileInputStream(f2));

          while(in2.hasNext()){
            t2 = in2.nextLine();
            t2_splited = t2.split("\t");

            hashed_t2.put(t2_splited[0], t2_splited[0]+"\t"+t2_splited[1]+"\t"+t2_splited[2]);
          }

          in2.close();


        } catch(Exception e){
          System.err.println("Fichier1 : Erreur Exception "+e);
        }


        ///// JOINTURE
        try {

          in1 = new Scanner(new FileInputStream(f1));

          while(in1.hasNext()){

            t1 = in1.nextLine();

            t1_splited = t1.split("\t");

            if(hashed_t2.containsKey(t1_splited[1])){ // clé trouvé faire la jointure

                out.println(t1+"\t"+hashed_t2.get(t1_splited[1])); 
            }

          }


          in1.close();



        } catch(Exception e2){
          System.err.println("Fichier 2  Erreur Exception "+e2);
        }



        
        
        out.close();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Computation time, hash join: "+totalTime+" ms");
    }


    public void projection_naif(){

      Scanner in1;
        String t1;
        String [] t1_splited;
        int i;

        ArrayList<String> nom = new ArrayList<String>(); // pour stocker les noms deja lu
        
        long startTime = System.currentTimeMillis();
        try {
          in1 = new Scanner(new FileInputStream(f1));

          while (in1.hasNext()) {
              t1 = in1.nextLine();
              t1_splited = t1.split("\t");

              i=0;

              while(i<nom.size() && !nom.get(i).equals(t1_splited[1])){ // rechercher du nom 
                i++;
              }

              if(i>=nom.size()){
                nom.add(t1_splited[1]);
                out.println(t1_splited[1]);
              }           
            
          }



          in1.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        out.close();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Computation time, projection naif : "+totalTime+" ms");
    }


    public void projection_hash(){

      Scanner in1;
      String t1;
      String [] t1_splited;

      Hashtable<String, String> hashed = new Hashtable<String, String>();

      long startTime = System.currentTimeMillis();

      try {

        in1 = new Scanner(new FileInputStream(f1));

        while(in1.hasNext()){
          t1 = in1.nextLine();
          t1_splited = t1.split("\t");

          if(!hashed.containsKey(t1_splited[1])){
            hashed.put(t1_splited[1], t1_splited[1]);
            out.println(t1_splited[1]);
          }



        }
        in1.close();

      } catch (Exception e) {
            System.out.println(e);
      }

      



      out.close();
      long endTime   = System.currentTimeMillis();
      long totalTime = endTime - startTime;
      System.out.println("Computation time, hash projectio: "+totalTime+" ms");

    }
    ///// f1 minus f2
    public void soustraire_naif(){

     Scanner in1, in2;
     boolean trouve;
        String t1,t2 ;
        String [] t2_splited = null;
        String [] t1_splited;
        
        long startTime = System.currentTimeMillis();
        try {
          in1 = new Scanner(new FileInputStream(f1)) ;
          in2 = new Scanner(new FileInputStream(f2));

          while (in1.hasNext()) {
              t1 = in1.nextLine();
              t1_splited = t1.split("\t");

              trouve = false;

              while (in2.hasNext() && !trouve) {
                  t2 = in2.nextLine();
                  t2_splited = t2.split("\t");
                  if (t1_splited[0].equals(t2_splited[1])) {
                      trouve = true;
                  }
              }

              if(!trouve){
                out.println(t1_splited[0]);                
              }

              in2.reset();
              in2= new Scanner(new FileInputStream(f2));
          }
          in1.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        out.close();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Computation time, soustraction naive: "+totalTime+" ms");

    }


    ///// f1 minus f2

    public void soustraire_hash(){


      Scanner in1, in2;
      String t1,t2 ;
      String [] t2_splited;
      String [] t1_splited;
 
      Hashtable<String, String> hashed_t2 = new Hashtable<String, String>();

      long startTime = System.currentTimeMillis();

      try {
        in2 = new Scanner(new FileInputStream(f2));


        ///remplissage de la table de hachage avec le fichier f2

        while(in2.hasNext()){
          t2 = in2.nextLine();
          t2_splited = t2.split("\t");

          hashed_t2.put(t2_splited[1], t2_splited[1]);
        }
        in2.close();

        /// ecriture dans le fichier en verifiant s'il est présent dans la table (dans f2)
        in1 = new Scanner(new FileInputStream(f1));

        while(in1.hasNext()){
          t1 = in1.nextLine();
          t1_splited = t1.split("\t");

          if(!hashed_t2.containsKey(t1_splited[0])){
            out.println(t1_splited[0]);
          }
        }
        in2.close();




      } catch(Exception e){
        System.err.println("Erreur soustraction hash "+e);
      }




      out.close();
      long endTime   = System.currentTimeMillis();
      long totalTime = endTime - startTime;
      System.out.println("Computation time, hash soustraction: "+totalTime+" ms");

    }


        
    
}