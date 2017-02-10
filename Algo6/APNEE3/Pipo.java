import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.Hashtable;
import java.util.Enumeration;


class Pipo {
    String f1; // the learning file
    Scanner in1; // the scanner associated to the file
    Hashtable<String,Hashtable<String,Integer>> LangModel; // the language model
    Hashtable<String,Integer> Frequencies_learn;
    Hashtable<String,Integer> Frequencies_gen;
    Random generator;
    
    Pipo(String f1) {
        this.f1 = f1;
        try {in1 = new Scanner(new FileInputStream(f1)); }
        catch (Exception e) {System.out.println("Erreur ouverture fichier : "+e);}
        LangModel = new  Hashtable<String,Hashtable<String,Integer>>();
        Frequencies_learn = new Hashtable<String,Integer>();
        Frequencies_gen = new Hashtable<String,Integer>();
        generator=new Random(); // Seed to be given... Eventually
    }
    
    public void newWorsSeq(String w1, String w2) {
        //System.out.println(" "+w1+"  "+w2+" ");
        // This is were you need to update the language model (hash of hashes)
        int occ;



        try {
            if(LangModel.containsKey(w1)){ // le mot w1 est déjà présent dans la table de hachage de table de hachage

                if(LangModel.get(w1).containsKey(w2)){ // le mot w2 est deja present 
                    occ = LangModel.get(w1).get(w2);
                    LangModel.get(w1).put(w2, occ+1);
                } else {
                    LangModel.get(w1).put(w2, 1);
                }

            } else {
                LangModel.put(w1, new Hashtable<String,Integer>());
                LangModel.get(w1).put(w2, 1);
            }
            
        } catch(Exception e){
            System.out.println("Erreur dans newWorsSeq : "+ e +" avec les mots "+w1+" et "+w2);
        }
    }
    
    public void Learn() {
        String word1;
        int occ;
        word1="."; // A ghost word beeing before the first word of the text
        try {
            while (in1.hasNext()) {


                ///// CALCUL DES FREQUENCES 
                if(Frequencies_learn.containsKey(word1)){ 
                    occ = Frequencies_learn.get(word1);
                    Frequencies_learn.put(word1, occ+1);    
                } else {
                    Frequencies_learn.put(word1, 1); 
                }




                String word2 = in1.next();
                if (word2.matches("(.*)[.,!?<>=+-/]")) {
                    // word2 is glued with a punctuation mark
                    String[] splitedWord= word2.split("(?=[.,!?<>=+-/])|(?<=])");
                    for (String s : splitedWord) {
                        newWorsSeq(word1,s); // update de language model
                        word1=s;
                    }

                } else { // word2 is a single word
                    newWorsSeq(word1,word2); // update de language model
                    word1=word2;
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur dans learn : "+e);
        }
    }

    int sum(Hashtable<String,Integer> H){
        int s = 0;
        int occ;
        for (Enumeration e = H.keys(); e.hasMoreElements();){
            occ = H.get(e.nextElement());
            s += occ;
        }

        return s;
    }

    public void max(){ // affichage des termes les plus fréquents

        String word, word_max = "";
        int max_occ = 0;


        for(int i=1; i<=10; i++){
            
            Enumeration<String> e = Frequencies_learn.keys();
            max_occ = 0;
            while(e.hasMoreElements()){

                word = e.nextElement();

                if(Frequencies_learn.get(word) > max_occ){
                    max_occ = Frequencies_learn.get(word);
                    word_max = word;
                }


            }
            System.out.println("Position "+i+": "+word_max);
            Frequencies_learn.put(word_max, -1); // pour eviter de le retraiter
        }

    }

    
    public void Talk(int nbWord) {
        // Taking advantage of the generative skills of the language model

        //System.out.println("Compte rendu de l'apnée 3 algo 6 :");
        //System.out.println();


        String w1, w2 = "";
        Random rand = new Random(); // ajouter un seed pour tester
        int nbmot = 0;
        int sum_keys;
        int occ;
        double tirage_rand, current_proba;

        w1 = ".";

        while(nbmot<nbWord){

            tirage_rand = rand.nextDouble();

            sum_keys = sum(LangModel.get(w1)); // calcul du nombre de mot possible après

            current_proba = 0.0;


            Enumeration<String> e = LangModel.get(w1).keys();
            while(current_proba < tirage_rand){ // tant qu'on a pas la bonne probabilité
                w2 = e.nextElement();
                current_proba += LangModel.get(w1).get(w2)/((double)sum_keys);
            }

            ///// CALCUL DES FREQUENCES 
            if(Frequencies_gen.containsKey(w2)){ 
                occ = Frequencies_gen.get(w2);
                Frequencies_gen.put(w2, occ+1);    
            } else {
                Frequencies_gen.put(w2, 1); 
            }

            System.out.print(w2+" ");

            w1 = w2;
            nbmot++;   
        }

        System.out.println();
        System.out.println();


        //System.out.println(Frequencies_learn);
        //System.out.println(Frequencies_gen);



    }
}