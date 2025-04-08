package Absyn;

public class VarExp extends Exp {
   public Var var;

   public VarExp(int p, Var v) {
      super.pos = p;
      this.var = v;
   }
}