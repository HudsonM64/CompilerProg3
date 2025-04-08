package Absyn;

public class LetExp extends Exp {
   public DeclarationList decs;
   public Exp body;

   public LetExp(int p, DeclarationList d, Exp b) {
      super.pos = p;
      this.decs = d;
      this.body = b;
   }
}