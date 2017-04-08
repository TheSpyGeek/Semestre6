


class test_fap {

	static public void main(String [] args){

		file_a_priorite file = new file_a_priorite();

		file.Inserer(10, 2);
		file.Inserer(11, 1);
		file.Inserer(20, 0);
		file.Inserer(15, 4);

		while(!file.Est_Vide()){
			System.out.println(file.Extraire());
		}


	}


}