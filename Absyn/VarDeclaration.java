package Absyn;

import Semant.VarEntry;
import Symbol.Symbol;

public class VarDeclaration extends Decl {
   public Symbol name;
   public boolean escape = false;
   public NameTy typ;
   public Exp init;
   public VarEntry entry;

   public VarDeclaration(int p, Symbol n, NameTy t, Exp i) {
      super.pos = p;
      this.name = n;
      this.typ = t;
      this.init = i;
   }
}