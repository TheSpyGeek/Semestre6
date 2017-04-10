
import java.util.LinkedList;

class file_a_priorite {


	LinkedList<Element> file;


	file_a_priorite(){
		file = new LinkedList<Element>();
	}

	public boolean Est_Vide(){
		return file.size() == 0;
	}

	public void Inserer(Element e){
		file.add(e);
	}

	public void Inserer(Couple i, int j, int prio){
		Element e = new Element(i, j, prio);
		file.add(e);
	}

	public Element Extraire(){

		int index = 0;
		int min = 10000000;


		for(int i=0; i<file.size(); i++){
			if(file.get(i).poids() < min){
				min = file.get(i).poids();
				index = i;
			}
		}

		return file.remove(index);
	}


}