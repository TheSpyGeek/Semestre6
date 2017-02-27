(* Michaël PÉRIN, Verimag / Université Grenoble-Alpes, Février 2017 
 *
 * Operation on bit vectors  parmaterized by the representation of bits (using functor)
 * 
 * - Requires an instranciation of a Bit module defining Two_Values zero and unit
 *
 * - How to use it ? Have a look at the DEMO
 *
 *)
  
(* USAGE:
 - Required:
 - Compile : ocamlc Bit_Vector.ml
*)


(* REPRESENTATION OF BIT *)
  
module type Two_Values_MT =
  sig
    type t 
    val zero:t
    val unit:t
    val pretty: t -> string
  end

module Bit_as_Int : Two_Values_MT =
  struct
    type t = int
    let (zero:t) = 0
    let (unit:t) = 1
    let (pretty: t -> string) = string_of_int 
  end

module Bit_as_Boolean : Two_Values_MT =
  struct
    type t = bool
    let (zero:t) = false
    let (unit:t) = true
    let (pretty: t -> string) = fun bool -> string_of_int (if bool=zero then 0 else 1)
  end


    
(* BIT VECTOR *)    
    
module Made_Of = functor (Bit : Two_Values_MT) ->
  struct

    type bit = Bit.t
    type t = bit list

    let (pretty: t -> string) = fun bits ->
	  String.concat "" (List.map Bit.pretty bits)

    let (print: t -> unit) = fun bits ->
	  print_string (pretty bits)
	
   (* CONVERSION int <-> bit vector (Little endian encoding: the less significant bit is on the left) *) 
      
    let rec (int_to_bits: int -> t) = 
	  function
	    | 0 -> [ Bit.zero ]
	    | 1 -> [ Bit.unit ]
	    | i ->
		    let r = i mod 2
		    in (if r=0 then Bit.zero else Bit.unit) :: (int_to_bits ((i-r)/2))

								
    let (bits_to_int: t -> int option) = fun bits ->
	  let rec
	      (horner: int -> t -> int) = fun int ->
		    function
		      | [] -> int
		      | b::bits ->
			      if b = Bit.zero
			      then horner  (2 * int)    bits
			      else horner ((2 * int)+1) bits
	  in
	    match bits with
	    | [] -> None
	    | _  -> Some (horner 0 (List.rev bits))
		      
    let (unsafe_bits_to_int: t -> int) = fun bits ->
	  match (bits_to_int bits) with
	  | Some int -> int 
	  | None -> assert (false)

		      
   (* OPERATIONS on bit vector *)

   (* The operation +1 *)
		  
    let rec (inc: t -> t) = fun bits ->
	  match bits with
	  | [b] -> if b=Bit.zero then [Bit.unit] else [Bit.unit ; Bit.zero]
	  | b::bits ->
		  if b = Bit.zero
		  then Bit.unit :: bits
		  else Bit.zero :: (inc bits)
	  | [] -> assert (false)
				    
   (* The operation *2 *)
				
    let (double: t -> t) = fun bits ->
	  Bit.zero :: bits

   (* The operation /2 *)
		 
    let (half: t -> t) = fun bits->
	  List.tl bits


   (* demo *)
  
    let (>>) x f = f x
	    
    let (demo: unit -> unit) = fun () ->
	  [0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16]  
	    >> (List.map
		  (fun int ->
			[ int_to_bits (unsafe_bits_to_int (int_to_bits int)) ;
			  inc (int_to_bits int) ;
			  double (inc (int_to_bits int)) ;
			  half(double (inc (int_to_bits int))) 
			]
			  >> (List.map pretty)
			  >> (String.concat ";")
		  ))
	    >> (String.concat "\n")
	    >> print_string
  end

  
(* DEMO 

module BV = Made_Of(Bit_as_Boolean)
	
let _ = BV.demo() ;;
	
*)
      
