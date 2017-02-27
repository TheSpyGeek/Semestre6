(* Michaël PÉRIN, Verimag / Université Grenoble-Alpes, Février 2017 
 *
 * Transition of a Turing Machine
 *
 *)

open State  
open Pattern
open Action
open Band
  
  
type transition = State.t * instruction * State.t

and instruction =
  | Action of Action.t
  | Call of string (* the name of an existing turing machine *)
  | Run  of turing_machine
  | Seq  of instruction list (* a sequence of instructions *)
  | Parallel of instruction list (* mulitple instructions in paralell on multiple bands *)
      
and transitions = transition list
  
and turing_machine = { name: string ;
		       nb_bands: int ;
		       initial: State.t ;
		       transitions: transitions ;
		     }



module Instruction =
  (struct
    type t = instruction

    let (nop: instruction) = Seq []
	
    let (zip: instruction list -> Band.t list -> (instruction * Band.t) list) =  Band.zip_complete_with nop ;;

    (* PRETTY PRINTING *)
	
    let rec (to_ascii: t -> string) = fun instruction ->
	  match instruction with
	  | Action action -> Action.to_ascii action
	  | Call tm_name -> tm_name
	  | Run tm -> tm.name
	  | Seq instructions -> Pretty.parentheses (String.concat " ; " (List.map to_ascii instructions))
	  | Parallel instructions -> Pretty.bracket (String.concat " || " (List.map to_ascii instructions))
		  
  end)

    

    
module Transition =
  (struct

    type t = transition
	  
    let (nop: State.t -> State.t -> transition) = fun source target ->  (source, Action(Nop), target)


    (* INSTANCIATON of generic transitions (PROJECT 2015) *)
		    
    let (foreach_symbol_of: 'a list -> 'a Pattern.t -> ('a -> transitions) -> transitions) = fun alphabet pattern create_transitions_for ->
	  let rec
	      (instantiate_transitions_foreach_symbol_in: 'a list -> transitions) = fun symbols  ->
		    match symbols with
		    | [] -> []
		    | s::ymbols ->
			    List.append
			      (create_transitions_for s)
			      (instantiate_transitions_foreach_symbol_in ymbols)
	  in
	    instantiate_transitions_foreach_symbol_in (Pattern.enumerate_symbols_of alphabet pattern)

	      
   (* PRETTY PRINTING *)

    let (to_ascii: t -> string) = fun (source,instruction,target) ->
	  String.concat " " [ State.to_ascii source ; "--" ; Instruction.to_ascii instruction ; "->" ; State.to_ascii target ]

    (* user *)

    let (pretty: t -> string) =
      match Pretty.get_format() with
      | Pretty.Html  
      | Pretty.Ascii -> to_ascii

  end)
    
