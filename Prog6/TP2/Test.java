
import java.util.*;
import java.io.*;

public class Test {


	public static void main(String [] args){

		EnsembleTableauEntiers e = new EnsembleTableauEntiers();
		Iterator it = e.iterateur();

		e.ajoute(1);
		e.ajoute(2);
		e.ajoute(3);
		e.ajoute(4);
		e.ajoute(5);
		e.ajoute(6);

		System.out.println(e);

	}




}