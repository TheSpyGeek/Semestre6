import java.util.Properties;

public abstract class EnsembleEntiers {

	abstract void ajoute(int c);

	abstract void supprime(int c) throws Exception;

	static EnsembleEntiers petit = null, grand = null;






	public static void init(Properties prop){

		if(prop.getProperty("GrandEnsemble").equals("Liste")){
			grand = new SousEnsemble_abstract();
		} else if(prop.getProperty("GrandEnsemble").equals("Tableau")){
			grand = new EnsembleTableauEntiers();
		} else {
			System.out.println("Erreur grand");
		}

		if(prop.getProperty("PetitEnsemble").equals("Liste")){
			petit = new SousEnsemble_abstract();
		} else if(prop.getProperty("PetitEnsemble").equals("Tableau")){
			petit = new EnsembleTableauEntiers();
		} else {
			System.out.println("Erreur petit");
		}
	}
	

	public static EnsembleEntiers Petit(){
		return petit;
	}

	public static EnsembleEntiers Grand(){
		return grand;
	}



}