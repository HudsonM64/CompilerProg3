package Absyn;

public class ForExp extends Exp {
   public VarDeclaration var;
   public Exp hi;
   public Exp body;

   public ForExp(int p, VarDeclaration v, Exp h, Exp b) {
      super.pos = p;
      this.var = v;
      this.hi = h;
      this.body = b;
   }
}