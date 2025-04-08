package Absyn;

import Symbol.Symbol;

public class CallExp extends Exp {
   public Symbol func;
   public ExpList args;

   public CallExp(int p, Symbol f, ExpList a) {
      super.pos = p;
      this.func = f;
      this.args = a;
   }
}