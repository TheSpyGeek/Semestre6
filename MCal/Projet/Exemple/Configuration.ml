(* Michaël PÉRIN, Verimag / Université Grenoble-Alpes, Février 2017 
 *
 * A run of a Turing Machine is a sequence of configurations
 * The configuration contains all the information needed to perform the next step.
 *)

open Tricks

open Tracing
open Band
open State
open Action
open Transition
open Turing_Machine
  

type status = Running | Final
  
type configuration = { tm: Turing_Machine.t ;
		       bands: Band.t list ;
		       state: State.t ;
		       status: status ; 
		       logger: Tracing.logger ;
		     }
      
type configurations = configuration list

      
module Configuration =
  (struct

    type t = configuration

    let (name: Turing_Machine.t -> Band.t list -> string) = fun tm bands ->
	  let nb_bands = List.length bands in
	    String.concat "" [ tm.name ; string_of_int nb_bands ; "B" ; Pretty.get_format_ext() ]

    let (make: Turing_Machine.t -> Band.t list -> configuration) = fun tm bands ->
	  { tm = tm ;
	    bands = bands ;
	    state = tm.initial ;
	    status = Running ;
	    logger = new Tracing.logger (Some (name tm bands))
	  }

    let (is_transition_enabled_on: configuration -> Transition.t -> bool) = fun cfg (source,instruction,_) ->
	  (cfg.state = source)
	    &&
	  (match instruction with
	  | Action action -> Action.is_enabled_on_bands action cfg.bands
	  | _ -> true
	  )
	    
	    
    (* PRETTY PRINTING *)
	  
    (* ascii *)
		    
    let (to_ascii: configuration -> string) = fun cfg ->
	  cfg.bands >> (List.map Band.to_ascii) >> (String.concat "\n")

    (* html *)

    let (to_html: Html.options -> configuration -> Html.table) = fun options cfg ->
	  let state =
	    State.to_html [] cfg.state

	  and bands =
	    let rows = List.map (Band.to_html []) cfg.bands in
	      Html.cell []
		(Html.table
		   (options @ [ ("bordercolor", Html.Color Color.white) ; ("cellpadding",Html.Int 1) ; ("cellspacing",Html.Int 0) ; ("border",Html.Int 1) ])
		   rows
		)
	  in
	    Html.table
	      (options @ [ ("bordercolor", Html.Color Color.gray) ; ("cellpadding",Html.Int 0) ; ("cellspacing",Html.Int 0) ; ("border",Html.Int 1) ])
	      [ state ; bands ]
	      
    (* user *)

     let (pretty: t -> string) =
      match Pretty.get_format() with
      | Pretty.Html  -> (to_html [])
      | Pretty.Ascii -> to_ascii


    let (print: t -> unit) = fun cfg ->
	  cfg.logger#print (pretty cfg) 
	  
  end)      


(* DEMO *)		

open Alphabet
open Symbol
  
let (demo: unit -> unit) = fun () ->
      let alphabet = Alphabet.make [B;Z;U;D] in
	let band1 = Band.make alphabet [B;D;Z;U;U;Z;B]
	and band2 = Band.make alphabet [B;B;B;L;B;B;B] in
	  let cfg = Configuration.make Turing_Machine.nop [ band1 ; band2 ] in
	    begin
	      print_string (Configuration.to_ascii cfg) ;
	      cfg.logger#print (Configuration.to_html [] cfg) ;
	      cfg.logger#close 
	    end
	      
    

