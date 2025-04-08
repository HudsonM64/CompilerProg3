package Absyn;

import Symbol.Symbol;

public class SimpleVar extends Var {
   public Symbol name;

   public SimpleVar(int p, Symbol n) {
      super.pos = p;
      this.name = n;
   }
}