package Absyn;

import Semant.FunEntry;
import Symbol.Symbol;

public class FunctionDeclaration extends Decl {
   public Symbol name;
   public FieldList params;
   public NameTy result;
   public Exp body;
   public FunctionDeclaration next;
   public boolean leaf = true;
   public FunEntry entry;

   public FunctionDeclaration(int p, Symbol n, FieldList a, NameTy r, Exp b, FunctionDeclaration x) {
      super.pos = p;
      this.name = n;
      this.params = a;
      this.result = r;
      this.body = b;
      this.next = x;
   }
}