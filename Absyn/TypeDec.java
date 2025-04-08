package Absyn;

import Symbol.Symbol;
import Types.NAME;

public class TypeDec extends Decl {
   public Symbol name;
   public Ty ty;
   public TypeDec next;
   public NAME entry;

   public TypeDec(int p, Symbol n, Ty t, TypeDec x) {
      super.pos = p;
      this.name = n;
      this.ty = t;
      this.next = x;
   }
}