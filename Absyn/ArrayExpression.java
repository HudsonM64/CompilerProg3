package Absyn;

import Symbol.Symbol;

public class ArrayExpression extends Exp {
   public Symbol typ;
   public Exp size;
   public Exp init;

   public ArrayExpression(int p, Symbol t, Exp s, Exp i) {
      super.pos = p;
      this.typ = t;
      this.size = s;
      this.init = i;
   }
}