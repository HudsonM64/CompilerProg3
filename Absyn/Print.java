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

   void prExp(OpExp e, int d) {
      sayln("OpExp(");
      indent(d+1); 
      switch(e.oper) {
      case OpExp.PLUS: say("PLUS"); break;
      case OpExp.MINUS: say("MINUS"); break;
      case OpExp.MUL: say("MUL"); break;
      case OpExp.DIV: say("DIV"); break;
      case OpExp.EQ: say("EQ"); break;
      case OpExp.NE: say("NE"); break;
      case OpExp.LT: say("LT"); break;
      case OpExp.LE: say("LE"); break;
      case OpExp.GT: say("GT"); break;
      case OpExp.GE: say("GE"); break;
      default:
        throw new Error("Print.prExp.OpExp");
      }
      sayln(",");
      prExp(e.left, d+1); sayln(",");
      prExp(e.right, d+1); say(")");
    }
  
    void prExp(VarExp e, int d) {
      sayln("VarExp("); prVar(e.var, d+1);
      say(")");
    }
  
    void prExp(NilExp e, int d) {
      say("NilExp()");
    }
  
    void prExp(Int e, int d) {
      say("Int("); say(e.val); say(")");
    }
  
    void prExp(StringLit e, int d) {
      say("StringLit("); say(e.val); say(")");
    }
  
    void prExp(CallExp e, int d) {
      say("CallExp("); say(e.func.toString()); sayln(",");
      prExplist(e.args, d+1); say(")");
    }
  
    void prExp(RecordExp e, int d) {
      say("RecordExp("); say(e.typ.toString());  sayln(",");
      prFieldExpList(e.fields, d+1); say(")");
    }
  
    void prExp(SeqExp e, int d) {
      sayln("SeqExp(");
      prExplist(e.list, d+1); say(")");
    }
  
    void prExp(AssignmentExpression e, int d) {
      sayln("AssignExp(");
      prVar(e.var, d+1); sayln(",");
      prExp(e.exp, d+1); say(")");
    }
    
    void prExp(IfExp e, int d) {
      sayln("IfExp(");
      prExp(e.test, d+1); sayln(",");
      prExp(e.thenclause, d+1);
      if (e.elseclause!=null) { /* else is optional */
        sayln(",");
        prExp(e.elseclause, d+1);
      }
      say(")");
    }
  
    void prExp(WhileExp e, int d) {
      sayln("WhileExp(");
      prExp(e.test, d+1); sayln(",");
      prExp(e.body, d+1); say(")");
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
  
    void prExp(BreakExp e, int d) {
      say("BreakExp()");
    }
  
    void prExp(LetExp e, int d) {
      say("LetExp("); sayln("");
      prDecList(e.decs, d+1); sayln(",");
      prExp(e.body, d+1); say(")");
    }
  
    void prExp(ArrayExpression e, int d) {
      say("ArrayExp("); say(e.typ.toString()); sayln(",");
      prExp(e.size, d+1); sayln(",");
      prExp(e.init, d+1); say(")");
    }
  
    /* Print Exp class types. Indent d spaces. */
    public void prExp(Exp e, int d) {
      indent(d);
      if (e instanceof OpExp) prExp((OpExp)e, d);
      else if (e instanceof VarExp) prExp((VarExp) e, d);
      else if (e instanceof NilExp) prExp((NilExp) e, d);
      else if (e instanceof Int) prExp((Int) e, d);
      else if (e instanceof StringLit) prExp((StringLit) e, d);
      else if (e instanceof CallExp) prExp((CallExp) e, d);
      else if (e instanceof RecordExp) prExp((RecordExp) e, d);
      else if (e instanceof SeqExp) prExp((SeqExp) e, d);
      else if (e instanceof AssignmentExpression) prExp((AssignmentExpression) e, d);
      else if (e instanceof IfExp) prExp((IfExp) e, d);
      else if (e instanceof WhileExp) prExp((WhileExp) e, d);
      else if (e instanceof ForExp) prExp((ForExp) e, d);
      else if (e instanceof BreakExp) prExp((BreakExp) e, d);
      else if (e instanceof LetExp) prExp((LetExp) e, d);
      else if (e instanceof ArrayExpression) prExp((ArrayExpression) e, d);
      else throw new Error("Print.prExp");
      if (e.type != null) {
        sayln(""); indent(d); say(":"); types.prType(e.type, d+1);
      }
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