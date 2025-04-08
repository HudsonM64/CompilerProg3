package Absyn;

import Symbol.Symbol;

public class FieldExpList extends Absyn {
   public Symbol name;
   public Exp init;
   public FieldExpList tail;

   public FieldExpList(int p, Symbol n, Exp i, FieldExpList t) {
      super.pos = p;
      this.name = n;
      this.init = i;
      this.tail = t;
   }
}