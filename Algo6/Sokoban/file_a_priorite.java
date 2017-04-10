
import java.util.LinkedList;

class file_a_priorite {


	LinkedList<Sac_Perso> file;


	file_a_priorite(){
		file = new LinkedList<Sac_Perso>();
	}

	public boolean Est_Vide(){
		return file.size() == 0;
	}

	public void Inserer(Sac_Perso e){
		file.add(e);
	}

	public Sac_Perso Extraire(){

		int index = 0;
		int min = 10000000;


		for(int i=0; i<file.size(); i++){
			if(file.get(i).poids < min){
				min = file.get(i).poids;
				index = i;
			}
		}

		return file.remove(index);
	}

	public String toString(){
		String renvoi = "File : ";
		for (int i=0; i<file.size(); i++){
			renvoi += file.get(i).toString()+" -> ";
		}
		return renvoi;
	}


}