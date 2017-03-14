(* Question 1 *)

(* Type de l’identite polymorphe *)
Definition tid : Set := forall T: Set, T -> T.
Definition id : tid := fun T:Set => fun x:T => x.

(* Question 2 *)

Definition test := id nat 3.

Compute test.

Section type_booleen.

Definition test2 := id bool true.

Compute test2.


(* Question 3 *)

Definition nbtrue1 := fun b =>
match b with
| true => 1
| false => 0
end.


(* Question  4 *)

Definition test3 := id tid id.

Theorem th_id : forall T:Set, forall x:T, id T x = x.
Proof.
reflexivity.
Qed.

(*EXERCICE 2*)
(* QUestion 1*)

Definition pbool : Set := forall T: Set, T -> T ->T.

Definition ptr : pbool := fun T:Set => fun x:T => fun y:T => y .
Definition pfa : pbool := fun T:Set => fun (x:T) (y:T) => x.

(*question 2 *)
Definition negptr : pbool -> pbool := fun b => fun T:Set => fun x => fun y => b T x y.


Definition neg2 : pbool -> pbool:= fun b => b pbool ptr pfa.

(*question 3 *)

(*conjontion et*)


 

