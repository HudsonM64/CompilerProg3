package Absyn;

public class AssignmentExpression extends Exp {
   public Var var;
   public Exp exp;

   public AssignmentExpression(int p, Var v, Exp e) {
      super.pos = p;
      this.var = v;
      this.exp = e;
   }
}