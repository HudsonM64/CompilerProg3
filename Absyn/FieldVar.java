package Absyn;

import Symbol.Symbol;

public class FieldVar extends Var {
   public Var var;
   public Symbol field;

   public FieldVar(int p, Var v, Symbol f) {
      super.pos = p;
      this.var = v;
      this.field = f;
   }
}