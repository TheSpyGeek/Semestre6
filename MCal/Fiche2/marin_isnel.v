(* ISNEL Maxime
MARIN Estelle 
Fiche 2

*)

(* Exercice 1 *)

Section type_booleen.
Variable T: Set.

(* les booleens sont des fonctions a deux parametres *)
Definition cbool := T -> T -> T.

(* codage de true *)
Definition ctr : cbool := fun x y => x.

(* codage de false *)
Definition cfa : cbool := fun x y => y.

(* codage du if *)

Definition cif : cbool->T->T->T := fun b m n => b m n.
(* cbool donne si c'est le premier argument ou le deuxième qu'on choisi
et on renvoi le booleen correspondant *)

Variable t : T.
Variable f : T.

Compute cif ctr t f.
Compute cif cfa t f.

(* Exercice 2 : Codage des opérateur Booleen *)

(* codage not *)

Definition cnot : cbool -> T -> T->T := fun b x y => b y x.

Compute cnot cfa t f.

(* codage and *)

Definition cand : cbool -> cbool -> cbool := fun a b x y => a (b x y) y.
Compute cand ctr ctr t f.

(* codage or *)

Definition cor : cbool -> cbool -> cbool := fun a b x y => a x (b x y).
Compute cand ctr ctr t f.

Theorem not_true : cnot ctr = cfa.
Proof. (* marque le debut de la preuve *)
cbv delta [cnot]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [cfa].
(* A ce stade on a une egalite entre deux termes syntaxiquement identiques *)
reflexivity.
Qed. (* marque la fin de la preuve *)

(* Question 4

delta reduction : on remplace la fonction par sa definition

Question 5

Beta reduction : on remplace variable par sa valeur

Question 6

*)

Theorem not_false : cnot cfa = ctr.
Proof. (* marque le debut de la preuve *)
cbv delta [cnot]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [ctr].
(* A ce stade on a une egalite entre deux termes syntaxiquement identiques *)
reflexivity.
Qed.

(* Question 7 *)

Theorem not_false2 : cnot cfa = ctr.
Proof. (* marque le debut de la preuve *)
reflexivity.
Qed.

(* pas mal !!*)

(* Question 9 *)

Theorem Th1 : cand cfa cfa = cfa.
Proof.
cbv delta [cand]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [cfa].
cbv beta.
reflexivity.
Qed. (* prouved *)

Theorem Th2 : cand cfa ctr = cfa.
Proof.
cbv delta [cand]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [cfa].
cbv beta.
reflexivity.
Qed.

Theorem Th3 : cand ctr cfa = cfa.
Proof.
cbv delta [cand]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [cfa].
cbv beta.
reflexivity.
Qed.

Theorem Th4 : cand ctr ctr = ctr.
Proof.
cbv delta [cand]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [ctr].
cbv beta.
reflexivity.
Qed.

(* Question 11 pourver or *)

Theorem Th_or1 : cor cfa cfa = cfa.
Proof.
cbv delta [cor]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [cfa].
cbv beta.
reflexivity.
Qed.

Theorem Th_or2 : cor ctr cfa = ctr.
Proof.
cbv delta [cor]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [ctr].
cbv beta.
reflexivity.
Qed.

Theorem Th_or3 : cor cfa ctr = ctr.
Proof.
cbv delta [cor]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [cfa].
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [ctr].
cbv beta.
reflexivity.
Qed.

Theorem Th_or4 : cand ctr ctr = ctr.
Proof.
cbv delta [cor]. (* les tactiques commencent par des minuscules *)
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [ctr].
cbv beta.
cbv delta [ctr].
cbv beta.
reflexivity.
Qed.

(* marche pas 
Theorem cand_com : forall a b : cbool, cand a b = cand b a.
Proof.
intros a b.
cbv delta[cand].
cbv beta.
cbv delta[cand].
cbv beta.
reflexivity.
Qed.
*)










