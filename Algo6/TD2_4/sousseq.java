
import java.util.*;
import java.io.*;


class sousseq {

	static String S1;
	static String S2;

	static public void main(String [] args){

		S1 = "AABAAACCABBA";
		S2 = "ABBBAACDABCDA";

		//System.out.println(S2.charAt(S2.length()-1));

		System.out.println("Plus grande sequence commune "+plssc(0,0));
	} 

	static public int max(int a, int b){
		if(a > b){
			return a;
		} else {
			return b;
		}
	}


	static public int plssc(int i, int j){

		if(i==S1.length() || j==S2.length()){
			return 0;
		} else if(S1.charAt(i) == S2.charAt(j)){
			return 1 + plssc(i+1, j+1);
		} else if(S1.charAt(i) != S2.charAt(j)){
			return max(plssc(i, j+1), plssc(i+1, j));
		} else {
			return -1;
		}

	}


}