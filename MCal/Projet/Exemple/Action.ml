(* Michaël PÉRIN, Verimag / Université Grenoble-Alpes, Février 2017 
 *
 * Basic actions of a Turing Machine
 *
 *)

open Tricks
  
open Symbol
open Band
open Pattern
  
  
type reading = Match of Symbol.t pattern

type writing =
  | No_Write
  | Write of Symbol.t

type moving  = Left | Here | Right

type action =
  | RWM of (reading * writing * moving)
  | Simultaneous of action list (* simultaneous actions on multiple bands *)
  | Nop (* no operation, needed by zip *)


      
module Moving =
  (struct
    type t = moving
	  
    let (pretty: t -> string) = function
      | Left  -> "Left"
      | Here  -> "Here"
      | Right -> "Right"
		
  end)

    
	  
module Action =
  (struct

    type t = action

    let (zip: action list -> Band.t list -> (action * Band.t) list) =  Band.zip_complete_with Nop ;;

    (* ENABLED ACTION *)	
	
    let rec (is_enabled_on_one_band: action -> Band.t -> bool) = fun action band ->
	  match action with
	  | Nop -> true
	  | RWM (Match(pattern),_,_) -> Pattern.matches pattern band.head
	  | Simultaneous [action] -> is_enabled_on_one_band action band

    let (are_enabled_on_bands: action list -> Band.t list -> bool) = fun actions bands ->
	  List.for_all
	    (fun (action,band) -> is_enabled_on_one_band action band)
	    (zip actions bands)

    let rec (is_enabled_on_bands: action -> Band.t list -> bool) = fun action bands ->
	  (bands <> [])
	    &&
	  (match action with
	  | Nop -> true
	  | RWM _ -> is_enabled_on_one_band action (List.hd bands)
	  | Simultaneous actions -> are_enabled_on_bands actions bands
	  )

    (* PERFORMING AN ACTION *)
	    
    let (do_move: moving -> Band.t -> Band.t) = fun moving band ->
	  match moving with
	  | Left  -> Band.move_head_left  band
	  | Right -> Band.move_head_right band
	  | Here  -> band
		    
    let (do_write: writing -> Band.t -> Band.t) = fun writing band ->
	  match writing with
	  | Write s -> Band.write s band
	  | _       -> band

    let rec (perform_on_one_band: action -> Band.t -> Band.t) = fun action band ->
	  match action with
	  | Nop -> band
	  | RWM (_,writing,moving) -> band >> (do_write writing) >> (do_move moving)
 	  | Simultaneous [action]  -> perform_on_one_band action band

    let (perform: action -> Band.t list -> Band.t list) = fun action bands ->
	  match action with
	  | Nop -> bands
	  | Simultaneous actions ->
		  List.map
		    (fun (action,band) -> perform_on_one_band action band)
		    (zip actions bands)
	  | RWM _ ->
		  (match bands with
		  | band :: untouched_bands -> (perform_on_one_band action band) :: untouched_bands
		  | [] -> failwith "Action.perform: missing band"
		  )


    (* PRETTY PRINTING *)

    let rec (to_ascii: t -> string) = function
      | Nop -> "Nop"
      | RWM(reading,writing,moving) -> "RWM"
      | Simultaneous actions -> Pretty.brace (String.concat "," (List.map to_ascii actions))

    (* user *)

    let (pretty: t -> string) =
      match Pretty.get_format() with
      | Pretty.Html  
      | Pretty.Ascii -> to_ascii

end)


    
(*
    let (write_on_ith_band: symbol -> int -> bands -> bands) = fun symbol ith bands ->
	  let (before, band_i, after) = MyList.split_at (ith-1) bands
	  in before @ [ { band_i with head = symbol } ] @ after
 *)		
	    
    (* information 

    let (alphabets_of: action -> Alphabet.t list) = fun action ->
	  let (get_alphabet: action -> Alphabet.t) = fun action ->
		match action with
		| RWM (reading,writing,_) -> Alphabet.union (Reading.alphabet_of reading) (Writing.alphabet_of writing) 
		| Nop -> Alphabet.empty
	  in
	    match action with
	    | Simultaneous actions -> List.map get_alphabet actions
       |  _ -> [ get_alphabet action ]
   
     *)

    
