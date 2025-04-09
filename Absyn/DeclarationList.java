package Absyn;

public class DeclarationList {
   public Decl head;
   public DeclarationList tail;

   public DeclarationList(Decl h, DeclarationList t) {
      this.head = h;
      this.tail = t;
   }
}