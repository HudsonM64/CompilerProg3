package Absyn;

public class IfExp extends Exp {
   public Exp test;
   public Exp thenclause;
   public Exp elseclause;

   public IfExp(int p, Exp x, Exp y) {
      super.pos = p;
      this.test = x;
      this.thenclause = y;
      this.elseclause = null;
   }

   public IfExp(int p, Exp x, Exp y, Exp z) {
      super.pos = p;
      this.test = x;
      this.thenclause = y;
      this.elseclause = z;
   }
}