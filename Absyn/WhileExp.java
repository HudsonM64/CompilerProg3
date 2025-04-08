package Absyn;

public class WhileExp extends Exp {
   public Exp test;
   public Exp body;

   public WhileExp(int p, Exp t, Exp b) {
      super.pos = p;
      this.test = t;
      this.body = b;
   }
}