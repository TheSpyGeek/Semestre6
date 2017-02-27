(* Michaël PÉRIN, Verimag / Université Grenoble-Alpes, Février 2017 
 *
 * Execution of a Turing Machine (single band or multi-bands TM) 
 *
 *)
  
open Tricks
  
open State  
open Band    
open Action
open Transition
open Turing_Machine
open Configuration  


(* PROJECT 2015 *) 
 
module Execution =
  (struct

    let (select_enabled_transition: Turing_Machine.t -> Configuration.t -> Transition.t option) = fun tm cfg ->
	  let
	      enabled_transitions = List.filter (Configuration.is_transition_enabled_on cfg) tm.transitions
	  in
	    match enabled_transitions with
	    | []           -> None
	    | [transition] -> Some transition
	    | transitions  ->
		    let error = "Execution.select_transitions: non deterministic TM" in
		      let msg = String.concat " "
			  [ error ; cfg.tm.name ; String.concat "\n - " (List.map Transition.pretty transitions) ]
		      in begin print_string msg ; failwith error  end

	  
   (* mutually recursives functions to deal with the instruction Machine(TM name)  *)

    let rec (perform_on_one_band: instruction -> Band.t -> Band.t) = fun instruction band ->
	  List.hd (perform instruction [band])
	    
    and (perform: instruction -> Band.t list -> Band.t list) = fun instruction bands ->
	  match instruction with
	  | Action action -> Action.perform action bands
         
	  | Run tm -> (* The reasons of mutual recursivity: "run" calls "perform" which can call "run" *)
		  let final_cfg = run (Configuration.make tm bands) in final_cfg.bands

	  | Call tm_name -> (* The reasons of mutual recursivity: "run" calls "perform" which can call "run" *)
		  let tm = Turing_Machine.i_find_TM_named tm_name in
		    let final_cfg = run (Configuration.make tm bands) in final_cfg.bands

	  | Seq [] -> bands
	  | Seq (inst::instructions) -> bands >> (perform inst) >> (perform (Seq instructions))

	  | Parallel instructions ->
		  List.map
		    (fun (inst,band) -> perform_on_one_band inst band)
		    (Instruction.zip instructions bands)
		    
    and (execute: Transition.t -> Configuration.t -> Configuration.t) = fun (_,instruction,target) cfg ->
	  { cfg with bands = perform instruction cfg.bands ; state = target } 

    and (one_step: Configuration.t -> Configuration.t) = fun cfg ->
	  match select_enabled_transition cfg.tm cfg with
	  | None            -> { cfg with status = Final }
	  | Some transition -> execute transition cfg

    and (run: Configuration.t -> Configuration.t) = fun cfg ->
	  begin
	    cfg.logger#print (Configuration.to_html [] cfg) ;
    	    if (cfg.status = Final)
	    then cfg
	    else run (one_step cfg)
	  end

    and (run_tm_named: string -> Band.t list -> Band.t list) = fun name bands ->
	  let tm = Turing_Machine.i_find_TM_named name in
	    let final_cfg = run (Configuration.make tm bands) 
	    in  final_cfg.bands


  end)
    
(* DEMO *)

open Alphabet
open Symbol
  
let (demo_inc: unit -> unit) = fun () ->
      let alphabet = Alphabet.make [B;Z;U;D] in
	let band1 = Band.make alphabet [U;U;Z;U] in
	  let cfg = Configuration.make Turing_Machine.inc [ band1 ] in
	    let _final_cfg = Execution.run cfg in
	      cfg.logger#close 


let (demo_dup: unit -> unit) = fun () ->
      let alphabet = Alphabet.make [B;Z;U;D] in
	let dup = Turing_Machine.generic_dup alphabet.symbols in
	  let band = Band.make alphabet [U;Z;Z;U] in
	    let cfg = Configuration.make dup [ band ] in
	      let _final_cfg = Execution.run cfg in
		cfg.logger#close 
		
      
