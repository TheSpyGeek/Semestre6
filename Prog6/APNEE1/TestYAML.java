import java.io.*;
import java.util.*;

import org.yaml.snakeyaml.*;


public class TestYAML {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String test;
		Yaml yaml = new Yaml();

		
		try {
			Object obj = yaml.load(new FileInputStream(new File("Niveau-1.txt")));

			System.out.println(obj.getClass());

			// c'est cancer

			//List obje = (List) obj;

			System.out.println(obj);
			System.out.println( ( (Map)   ( (Map) obj).get("Composants") ).size());

			//System.out.println("Casted = "+obje);
		}

		catch (FileNotFoundException e){
			System.out.println("Fichier non trouvé");
		}




		//System.out.println(obj.dump("Composants"));



	}
}
