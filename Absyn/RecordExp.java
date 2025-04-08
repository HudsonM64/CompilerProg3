package Absyn;

import Symbol.Symbol;

public class RecordExp extends Exp {
   public Symbol typ;
   public FieldExpList fields;

   public RecordExp(int p, Symbol t, FieldExpList f) {
      super.pos = p;
      this.typ = t;
      this.fields = f;
   }
}