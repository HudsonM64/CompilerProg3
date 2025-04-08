package Absyn;

public class SubscriptVar extends Var {
   public Var var;
   public Exp index;

   public SubscriptVar(int p, Var v, Exp i) {
      super.pos = p;
      this.var = v;
      this.index = i;
   }
}