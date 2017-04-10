
import java.util.LinkedList;

class file_a_priorite {


	LinkedList<Sac_Perso> file;


	file_a_priorite(){
		file = new LinkedList<Sac_Perso>();
	}

	public boolean Est_Vide(){
		return file.size() == 0;
	}

	private int Contient(Sac_Perso s){
		int trouve = -1;

		for(int i=0; i<file.size(); i++){
			if(s.perso.same(s.perso, file.get(i).perso) && s.sac.same(s.sac, file.get(i).sac)){
				trouve = i;
			}
		}
		return trouve;
	}

	public void Inserer(Sac_Perso e){
		int index = this.Contient(e);
		if(index != -1){
			System.out.println("MAJ");
			file.get(index).poids = e.poids;
		} else {
			file.add(e);
		}
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