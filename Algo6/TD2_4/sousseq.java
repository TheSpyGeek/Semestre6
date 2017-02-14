
import java.util.*;
import java.io.*;


class sousseq {

	static String S1;
	static String S2;

	static public void main(String [] args){

		 S1 = "AABAAACCABBA";
		 S2 = "ABBBAACDABCDA";

		// S1 = "ABBC";
		// S2 = "ABBAC";

		//System.out.println(S2.charAt(S2.length()-1));
		System.out.println("Longeur plus grande "+matrice());
		// matrice();
		//System.out.println("Plus grande sequence commune "+plssc(0,0));
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

	static public int matrice(){

		int i = 0, j=0;

		int imax = S1.length()+1;
		int jmax = S2.length()+1;

		int mat[][] = new int[imax][jmax];

		for(i=0;i<imax;i++){
			for(j=0;j<jmax; j++){
				mat[i][j] = 0;
			}
		}

		for(i=1;i<imax;i++){
			for(j=1;j<jmax; j++){
				if(S1.charAt(i-1) == S2.charAt(j-1)){
					mat[i][j] = mat[i-1][j-1] +1;
				} else {
					mat[i][j] = max(mat[i][j-1], mat[i-1][j]);
				}

			}
		}

		for(i=0;i<imax;i++){
			for(j=0;j<jmax; j++){
				System.out.print(mat[i][j]+" ");
			}
			System.out.println();
		}


		return mat[i-1][j-1];
	}


}