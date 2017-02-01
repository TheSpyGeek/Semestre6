
import java.util.*;
import java.io.*;


class main_game {

	public static void main(String [] args){


		InputStream f = null;
		Properties prop = new Properties();
		String s;
		File fi;


		Map<String,String> env = System.getenv();


		try{

			fi = new File(env.get("HOME")+"/.armoroides");
			if(fi.exists() && !fi.isDirectory()) { 
				f = new FileInputStream(env.get("HOME")+"/.armoroides");
			} else {
				f = new FileInputStream("default.cfg");
			}

         	prop.load(f);

         	EnsembleEntiers.init(prop);

         	EnsembleEntiers.Petit().ajoute(1);
         	EnsembleEntiers.Grand().ajoute(2);

         	System.out.println(EnsembleEntiers.Petit());
         	System.out.println(EnsembleEntiers.Grand());









		} catch(Exception e){
			System.err.println("Exeception "+e);

		}finally{
         	System.out.println("Lecture faite");

      	}


	}




}