package Absyn;

import java.io.PrintWriter;

public class Print {
   PrintWriter out;
   private Types.Print types;
   private Semant.Print semant;

   public Print(PrintWriter o) {
      this.out = o;
      this.types = new Types.Print(o);
      this.semant = new Semant.Print(o);
   }

   void indent(int d) {
      for(int i = 0; i < d; ++i) {
         this.out.print(' ');
      }

   }

   void prDec(Decl d, int i) {
      this.indent(i);
      if (d instanceof FunctionDeclaration) {
         this.prDec((FunctionDeclaration)d, i);
      } else if (d instanceof VarDeclaration) {
         this.prDec((VarDeclaration)d, i);
      } else {
         if (!(d instanceof TypeDec)) {
            throw new Error("Print.prDec");
         }

         this.prDec((TypeDec)d, i);
      }

   }

   void prDec(FunctionDeclaration d, int i) {
      this.say("FunctionDeclaration(");
      if (d != null) {
         this.sayln(d.name.toString());
         this.prFieldlist(d.params, i + 1);
         this.sayln(",");
         if (d.result != null) {
            this.indent(i + 1);
            this.sayln(d.result.name.toString());
         }

         this.prExp(d.body, i + 1);
         this.sayln(",");
         this.indent(i + 1);
         this.prDec(d.next, i + 1);
      }

      this.say(")");
      if (d != null && d.entry != null) {
         this.sayln("");
         this.indent(i);
         this.semant.prEntry(d.entry, i);
      }

   }

   void prDec(TypeDec d, int i) {
      if (d != null) {
         this.say("TypeDec(");
         this.say(d.name.toString());
         this.sayln(",");
         this.prTy(d.ty, i + 1);
         if (d.next != null) {
            this.sayln(",");
            this.indent(i + 1);
            this.prDec(d.next, i + 1);
         }

         this.say(")");
         if (d.entry != null) {
            this.sayln("");
            this.indent(i);
            this.say("=");
            this.types.prType(d.entry, i + 1);
         }
      }

   }

   void prDec(VarDeclaration d, int i) {
      this.say("VarDeclaration(");
      this.say(d.name.toString());
      this.sayln(",");
      if (d.typ != null) {
         this.indent(i + 1);
         this.say(d.typ.name.toString());
         this.sayln(",");
      }

      this.prExp(d.init, i + 1);
      this.sayln(",");
      this.indent(i + 1);
      this.say(d.escape);
      this.say(")");
      if (d.entry != null) {
         this.sayln("");
         this.indent(i);
         this.semant.prEntry(d.entry, i);
      }

   }

   void prDecList(DeclarationList v, int d) {
      this.indent(d);
      this.say("DeclarationList(");
      if (v != null) {
         this.sayln("");
         this.prDec(v.head, d + 1);
         this.sayln(",");
         this.prDecList(v.tail, d + 1);
      }

      this.say(")");
   }

   void prExp(ArrayExpression e, int d) {
      this.say("ArrayExpression(");
      this.say(e.typ.toString());
      this.sayln(",");
      this.prExp(e.size, d + 1);
      this.sayln(",");
      this.prExp(e.init, d + 1);
      this.say(")");
   }

   void prExp(AssignmentExpression e, int d) {
      this.sayln("AssignmentExpression(");
      this.prVar(e.var, d + 1);
      this.sayln(",");
      this.prExp(e.exp, d + 1);
      this.say(")");
   }

   void prExp(BreakExp e, int d) {
      this.say("BreakExp()");
   }

   void prExp(CallExp e, int d) {
      this.say("CallExp(");
      this.say(e.func.toString());
      this.sayln(",");
      this.prExplist(e.args, d + 1);
      this.say(")");
   }

   public void prExp(Exp e, int d) {
      this.indent(d);
      if (e instanceof OpExp) {
         this.prExp((OpExp)e, d);
      } else if (e instanceof VarExp) {
         this.prExp((VarExp)e, d);
      } else if (e instanceof NilExp) {
         this.prExp((NilExp)e, d);
      } else if (e instanceof Int) {
         this.prExp((Int)e, d);
      } else if (e instanceof StringLit) {
         this.prExp((StringLit)e, d);
      } else if (e instanceof CallExp) {
         this.prExp((CallExp)e, d);
      } else if (e instanceof RecordExp) {
         this.prExp((RecordExp)e, d);
      } else if (e instanceof SeqExp) {
         this.prExp((SeqExp)e, d);
      } else if (e instanceof AssignmentExpression) {
         this.prExp((AssignmentExpression)e, d);
      } else if (e instanceof IfExp) {
         this.prExp((IfExp)e, d);
      } else if (e instanceof WhileExp) {
         this.prExp((WhileExp)e, d);
      } else if (e instanceof ForExp) {
         this.prExp((ForExp)e, d);
      } else if (e instanceof BreakExp) {
         this.prExp((BreakExp)e, d);
      } else if (e instanceof LetExp) {
         this.prExp((LetExp)e, d);
      } else {
         if (!(e instanceof ArrayExpression)) {
            throw new Error("Print.prExp");
         }

         this.prExp((ArrayExpression)e, d);
      }

      if (e.type != null) {
         this.sayln("");
         this.indent(d);
         this.say(":");
         this.types.prType(e.type, d + 1);
      }

   }

   void prExp(ForExp e, int d) {
      this.sayln("ForExp(");
      this.indent(d + 1);
      this.prDec(e.var, d + 1);
      this.sayln(",");
      this.prExp(e.hi, d + 1);
      this.sayln(",");
      this.prExp(e.body, d + 1);
      this.say(")");
   }

   void prExp(IfExp e, int d) {
      this.sayln("IfExp(");
      this.prExp(e.test, d + 1);
      this.sayln(",");
      this.prExp(e.thenclause, d + 1);
      if (e.elseclause != null) {
         this.sayln(",");
         this.prExp(e.elseclause, d + 1);
      }

      this.say(")");
   }

   void prExp(Int e, int d) {
      this.say("Int(");
      this.say(e.val);
      this.say(")");
   }

   void prExp(LetExp e, int d) {
      this.say("LetExp(");
      this.sayln("");
      this.prDecList(e.decs, d + 1);
      this.sayln(",");
      this.prExp(e.body, d + 1);
      this.say(")");
   }

   void prExp(NilExp e, int d) {
      this.say("NilExp()");
   }

   void prExp(OpExp e, int d) {
      this.sayln("OpExp(");
      this.indent(d + 1);
      switch(e.oper) {
      case 0:
         this.say("PLUS");
         break;
      case 1:
         this.say("MINUS");
         break;
      case 2:
         this.say("MUL");
         break;
      case 3:
         this.say("DIV");
         break;
      case 4:
         this.say("EQ");
         break;
      case 5:
         this.say("NE");
         break;
      case 6:
         this.say("LT");
         break;
      case 7:
         this.say("LE");
         break;
      case 8:
         this.say("GT");
         break;
      case 9:
         this.say("GE");
         break;
      default:
         throw new Error("Print.prExp.OpExp");
      }

      this.sayln(",");
      this.prExp(e.left, d + 1);
      this.sayln(",");
      this.prExp(e.right, d + 1);
      this.say(")");
   }

   void prExp(RecordExp e, int d) {
      this.say("RecordExp(");
      this.say(e.typ.toString());
      this.sayln(",");
      this.prFieldExpList(e.fields, d + 1);
      this.say(")");
   }

   void prExp(SeqExp e, int d) {
      this.sayln("SeqExp(");
      this.prExplist(e.list, d + 1);
      this.say(")");
   }

   void prExp(StringLit e, int d) {
      this.say("StringLit(");
      this.say(e.val);
      this.say(")");
   }

   void prExp(VarExp e, int d) {
      this.sayln("VarExp(");
      this.prVar(e.var, d + 1);
      this.say(")");
   }

   void prExp(WhileExp e, int d) {
      this.sayln("WhileExp(");
      this.prExp(e.test, d + 1);
      this.sayln(",");
      this.prExp(e.body, d + 1);
      this.say(")");
   }

   void prExplist(ExpList e, int d) {
      this.indent(d);
      this.say("ExpList(");
      if (e != null) {
         this.sayln("");
         this.prExp(e.head, d + 1);
         if (e.tail != null) {
            this.sayln(",");
            this.prExplist(e.tail, d + 1);
         }
      }

      this.say(")");
   }

   void prFieldExpList(FieldExpList f, int d) {
      this.indent(d);
      this.say("FieldExpList(");
      if (f != null) {
         this.sayln("");
         this.indent(d + 1);
         this.say(f.name.toString());
         this.sayln(",");
         this.prExp(f.init, d + 1);
         this.sayln(",");
         this.prFieldExpList(f.tail, d + 1);
      }

      this.say(")");
   }

   void prFieldlist(FieldList f, int d) {
      this.indent(d);
      this.say("FieldList(");
      if (f != null) {
         this.sayln("");
         this.indent(d + 1);
         this.say(f.name.toString());
         this.sayln(",");
         this.indent(d + 1);
         this.say(f.typ.toString());
         this.sayln(",");
         this.indent(d + 1);
         this.say(f.escape);
         this.sayln(",");
         this.prFieldlist(f.tail, d + 1);
      }

      this.say(")");
   }

   void prTy(ArrayTy t, int i) {
      this.say("ArrayTy(");
      this.say(t.typ.toString());
      this.say(")");
   }

   void prTy(NameTy t, int i) {
      this.say("NameTy(");
      this.say(t.name.toString());
      this.say(")");
   }

   void prTy(RecordTy t, int i) {
      this.sayln("RecordTy(");
      this.prFieldlist(t.fields, i + 1);
      this.say(")");
   }

   void prTy(Ty t, int i) {
      if (t != null) {
         this.indent(i);
         if (t instanceof NameTy) {
            this.prTy((NameTy)t, i);
         } else if (t instanceof RecordTy) {
            this.prTy((RecordTy)t, i);
         } else {
            if (!(t instanceof ArrayTy)) {
               throw new Error("Print.prTy");
            }

            this.prTy((ArrayTy)t, i);
         }
      }

   }

   void prVar(FieldVar v, int d) {
      this.sayln("FieldVar(");
      this.prVar(v.var, d + 1);
      this.sayln(",");
      this.indent(d + 1);
      this.say(v.field.toString());
      this.say(")");
   }

   void prVar(SimpleVar v, int d) {
      this.say("SimpleVar(");
      this.say(v.name.toString());
      this.say(")");
   }

   void prVar(SubscriptVar v, int d) {
      this.sayln("SubscriptVar(");
      this.prVar(v.var, d + 1);
      this.sayln(",");
      this.prExp(v.index, d + 1);
      this.say(")");
   }

   void prVar(Var v, int d) {
      this.indent(d);
      if (v instanceof SimpleVar) {
         this.prVar((SimpleVar)v, d);
      } else if (v instanceof FieldVar) {
         this.prVar((FieldVar)v, d);
      } else {
         if (!(v instanceof SubscriptVar)) {
            throw new Error("Print.prVar");
         }

         this.prVar((SubscriptVar)v, d);
      }

   }

   void say(int i) {
      this.out.print(i);
   }

   void say(String s) {
      this.out.print(s);
   }

   void say(boolean b) {
      this.out.print(b);
   }

   void sayln(String s) {
      this.out.println(s);
   }
}