

class Element {
	private int val;
	private int prio;

	Element(int val, int prio){
		this.val = val;
		this.prio = prio;
	}

	public int poids(){
		return this.prio;
	}

	public int valeur(){
		return this.val;
	}

	public String toString(){
		String renvoi = "";
		renvoi += "("+this.val+", "+this.prio+")";
		return renvoi;
	}

	
}
