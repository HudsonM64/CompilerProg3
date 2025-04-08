package Absyn;

public class OpExp extends Exp {
   public Exp left;
   public Exp right;
   public int oper;
   public static final int PLUS = 0;
   public static final int MINUS = 1;
   public static final int MUL = 2;
   public static final int DIV = 3;
   public static final int EQ = 4;
   public static final int NE = 5;
   public static final int LT = 6;
   public static final int LE = 7;
   public static final int GT = 8;
   public static final int GE = 9;

   public OpExp(int p, Exp l, int o, Exp r) {
      super.pos = p;
      this.left = l;
      this.oper = o;
      this.right = r;
   }
}