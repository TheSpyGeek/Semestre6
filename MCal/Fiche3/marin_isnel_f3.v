(* 
ISNEL Maxime
MARIN Estelle
Groupe 3
*)



Variable T: Set.
Definition cnat := (T->T) -> (T->T).

(* Exercice 1 *)
(* Question 1 *)

Variable a : T.

Definition C0: cnat := fun f x => x.
Definition C1: cnat := fun f x => f x.
Definition C2: cnat := fun f x => f (f x).



