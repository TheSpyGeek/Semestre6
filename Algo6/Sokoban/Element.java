

class Element {
	private int i;
	private int j;
	private int prio;

	Element(int i, int j, int prio){
		this.i = i;
		this.j = j;
		this.prio = prio;
	}

	public int poids(){
		return this.prio;
	}

	public int i(){
		return this.i;
	}

	public int j(){
		return this.j;
	}

	public String toString(){
		String renvoi = "";
		renvoi += "("+this.i+", "+this.j+", "+this.prio+")";
		return renvoi;
	}

	public boolean equals(Element e){
		return (this.i == e.i && this.j == e.j);
	}

	
}
