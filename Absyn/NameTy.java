package Absyn;

import Symbol.Symbol;

public class NameTy extends Ty {
   public Symbol name;

   public NameTy(int p, Symbol n) {
      super.pos = p;
      this.name = n;
   }
}