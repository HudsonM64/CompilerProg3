package Absyn;

public class SeqExp extends Exp {
   public ExpList list;

   public SeqExp(int p, ExpList l) {
      super.pos = p;
      this.list = l;
   }
}