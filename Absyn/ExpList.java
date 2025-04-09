package Absyn;

public class ExpList {
   public Exp head;
   public ExpList tail;

   public ExpList(Exp h, ExpList t) {
      this.head = h;
      this.tail = t;
   }
}