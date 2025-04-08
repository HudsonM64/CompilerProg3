package Semant;

import Translate.Exp;
import java.util.Hashtable;
//import Absyn.*;
import ErrorMsg.ErrorMsg;
import Types.*;
//import Temp.*;
//import Symbol.*;

public class Semant {
  Env env;
  public Semant(ErrorMsg err) {
    this(new Env(err));
  }
  Semant(Env e) {
    env = e;
  }

  private void error(int pos, String msg) {
    env.errorMsg.error(pos, msg);
  }

  static final Types.VOID   VOID   = new Types.VOID();
  static final Types.INT    INT    = new Types.INT();
  static final Types.STRING STRING = new Types.STRING();
  static final Types.NIL    NIL    = new Types.NIL();

  private Exp checkInt(ExpTy et, int pos) {
    if (!INT.coerceTo(et.ty))
      error(pos, "integer required");
    return et.exp;
  }

  private void checkComparable(ExpTy et, int pos) {
    if (!(et.ty.coerceTo(INT) || et.ty.coerceTo(STRING)))
      error(pos, "Incomparable type");
  }
  
  private void checkOrderable(ExpTy et, int pos) {
    if (!(et.ty.coerceTo(INT) || et.ty.coerceTo(STRING)))
      error(pos, "Unorderable type");
  }

  private void transArgs(int epos, Absyn.StructMember formal, Absyn.ExpList args) {
    if (formal == null) {
      if (args != null)
        error(args.head.pos, "Too many arguments.");
      return;
    }
    if (args == null) {
      error(epos, "Missing argument for " + formal.fieldName);
      return;
    }
    ExpTy e = transExp(args.head);
    if (!e.ty.coerceTo(formal.fieldType))
      error(args.head.pos, "Argument type mismatch.");
    transArgs(epos, formal.tail, args.tail);
  }

  
  Exp transDec(Absyn.Decl d) {
    if (d instanceof Absyn.VarDeclaration)
      return transDec((Absyn.VarDeclaration)d);
    else if (d instanceof Absyn.FunctionDeclaration)
      return transDec((Absyn.FunctionDeclaration)d);
    else if (d instanceof Absyn.TypeCode)
      return transDec((Absyn.TypeCode)d);
    throw new Error("Semant.transDec");
  }

  Exp transDec(Absyn.VarDeclaration d) {
    ExpTy init = transExp(d.init);
    Type type;
    if (d.typ != null) {
      // user-specified type
      NAME name = (NAME) env.tenv.get(d.typ);
      if (name == null) {
        error(d.pos, "Undefined type " + d.typ);
        type = VOID;
      } else {
        type = name.actual();
        if (!init.ty.coerceTo(type))
          error(d.pos, "Initializer type does not match declared type.");
      }
    } else {
      // infer type from initializer
      if (init.ty instanceof NIL)
        error(d.pos, "Cannot assign NIL without declared type.");
      type = init.ty;
    }
  
    d.entry = new VarEntry(type);
    env.venv.put(d.name, d.entry);
    return null;
  }

  Exp transDec(Absyn.TypeCode d) {
    Hashtable<Object, Object> Hash = new Hashtable<>();
    Absyn.TypeCode type;

    for (type = d; type != null; type = type.next) {
      if (Hash.put(type.name, type.name) != null)
        error(type.pos, "Type is redeclared.");
      type.entry = new NAME(type.name);
      env.tenv.put(type.name, type.entry);
    }

    for (type = d; type != null; type = type.next) {
      NAME name = type.entry;
      name.bind(transTy(type.ty));
    }

    for (type = d; type != null; type = type.next) {
      NAME name = type.entry;
      if (name.isLoop())
        error(type.pos, "Illegal type cycle.");
    }
    return null;
  }

  Exp transDec(Absyn.FunctionDeclaration d) {
    Hashtable<Object, Object> Hash = new Hashtable<>();
    for (Absyn.FunctionDeclaration f = d; f != null; f = f.next) {
      if (Hash.put(f.name, f.name) != null)
        error(f.pos, "Function is redeclared");
      Types.Absyn.StructMember fields = transTypeFields(new HashTable<>(), f.params);
      Type type = transTy(f.result);
      f.entry = new FunEntry(fields, type);
      env.venv.put(f.name, f.entry);
    }

    for (Absyn.FunctionDeclaration f = d; f != null; f = f.next) {
      env.venv.beginScope();
      putTypeFields(f.entry.formals);
      Semant fun = new Semant(env);
      ExpTy body = fun.transExp(f.body);
      if(!body.ty.coerceTo(f.entry.result))
        error(f.body.pos, "Result type is incompatible.");
      env.venv.endScope();
    }
    return null;
  }

  ExpTy transExp(Absyn.Exp e) {
    ExpTy result;

    if (e == null)
      return new ExpTy(null, (Type)VOID);
      if (e instanceof Absyn.VarExp) {
        result = transExp((Absyn.VarExp)e);
      } else if (e instanceof Absyn.NilExp) {
        result = transExp((Absyn.NilExp)e);
      } else if (e instanceof Absyn.Int) {
        result = transExp((Absyn.Int)e);
      } else if (e instanceof Absyn.StringLit) {
        result = transExp((Absyn.StringLit)e);
      } else if (e instanceof Absyn.CallExp) {
        result = transExp((Absyn.CallExp)e);
      } else if (e instanceof Absyn.BinOp) {
        result = transExp((Absyn.BinOp)e);
      } else if (e instanceof Absyn.RecordExp) {
        result = transExp((Absyn.RecordExp)e);
      } else if (e instanceof Absyn.SeqExp) {
        result = transExp((Absyn.SeqExp)e);
      } else if (e instanceof Absyn.AssignmentExpression) {
        result = transExp((Absyn.AssignmentExpression)e);
      } else if (e instanceof Absyn.IfExp) {
        result = transExp((Absyn.IfExp)e);
      } else if (e instanceof Absyn.WhileStatement) {
        result = transExp((Absyn.WhileStatement)e);
      } else if (e instanceof Absyn.ForExp) {
        result = transExp((Absyn.ForExp)e);
      } else if (e instanceof Absyn.BreakExp) {
        result = transExp((Absyn.BreakExp)e);
      } else if (e instanceof Absyn.LetExp) {
        result = transExp((Absyn.LetExp)e);
      } else if (e instanceof Absyn.ArrayTy) {
        result = transExp((Absyn.ArrayTy)e);
      } else {
        throw new Error("Semant.transExp");
      } 
    e.type = result.ty;
    return result;
  }

  ExpTy transExp(Absyn.VarDeclaration e) {
    return transVar(e.var);
  }

  ExpTy transExp(Absyn.NilExp e) {
    return new ExpTy(null, (Type)NIL);
  }

  ExpTy transExp(Absyn.Int e) {
    return new ExpTy(null, (Type)INT);
  }

  ExpTy transExp(Absyn.StringLit e) {
    return new ExpTy(null, (Type)STRING);
  }

  ExpTy transExp(Absyn.BinOp e) {
    ExpTy left = transExp(e.left);
    ExpTy right = transExp(e.right);

    switch (e.oper) {
      case Absyn.BinOp.PLUS:
        checkInt(left, e.left.pos);
        checkInt(right, e.right.pos);
        return new ExpTy(null, INT);
      case Absyn.BinOp.MINUS:
        checkInt(left, e.left.pos);
        checkInt(right, e.right.pos);
        return new ExpTy(null, INT);
      case Absyn.BinOp.MUL:
        checkInt(left, e.left.pos);
        checkInt(right, e.right.pos);
        return new ExpTy(null, INT);
      case Absyn.BinOp.DIV:
        checkInt(left, e.left.pos);
        checkInt(right, e.right.pos);
        return new ExpTy(null, INT);
      case Absyn.BinOp.EQ:
        checkComparable(left, e.left.pos);
        checkComparable(right, e.right.pos);
        if(STRING.coerceTo(left.ty) && STRING.coerceTo(right.ty))
          return new ExpTy(null, INT);
        else if (!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "Operands not valid.");
        return new ExpTy(null, INT);
      case Absyn.BinOp.NE:
        checkComparable(left, e.left.pos);
        checkComparable(right, e.right.pos);
        if(STRING.coerceTo(left.ty) && STRING.coerceTo(right.ty))
          return new ExpTy(null, INT);
        else if (!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "Operands not valid for equality.");
        return new ExpTy(null, INT);
      case Absyn.BinOp.LT: 
        checkOrderable(left, e.left.pos);
        checkOrderable(right, e.right.pos);
        if(STRING.coerceTo(left.ty) && STRING.coerceTo(right.ty))
          return new ExpTy(null, INT);
        else if(!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "Operands not valid for inequality.");
        return new ExpTy(null, INT);
      case Absyn.BinOp.LE: 
        checkOrderable(left, e.left.pos);
        checkOrderable(right, e.right.pos);
        if(STRING.coerceTo(left.ty) && STRING.coerceTo(right.ty))
          return new ExpTy(null, INT);
        else if(!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "Operands not valid for inequality.");
        return new ExpTy(null, INT);
      case Absyn.BinOp.GT:
        checkOrderable(left, e.left.pos);
        checkOrderable(right, e.right.pos);
        if(STRING.coerceTo(left.ty) && STRING.coerceTo(right.ty))
          return new ExpTy(null, INT);
        else if(!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "Operands not valid for inequality.");
        return new ExpTy(null, INT);
      case Absyn.BinOp.GE:
        checkOrderable(left, e.left.pos);
        checkOrderable(right, e.right.pos);
        if(STRING.coerceTo(left.ty) && STRING.coerceTo(right.ty))
          return new ExpTy(null, INT);
        else if(!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "Operands not valid for inequality.");
        return new ExpTy(null, INT);
    default:
      throw new Error("unknown operator");
    }
  }

  ExpTy transExp(Absyn.RecordExp e) {
    NAME name = (NAME)this.env.tenv.get(e.typ);
    if (name != null) {
      Type actual = name.actual();
      if (actual instanceof Absyn.StructMember) {
        Absyn.StructMember r = (Absyn.StructMember)actual;
        transFields(e.pos, r, e.fields);
        return new ExpTy(null, (Type)name);
      }
      error(e.pos, "Absyn.StructMember type required.");
    }
    else {
      error(e.pos, "Undeclared type: " + e.typ);
    }
    return new ExpTy(null, (Type)VOID);
  }

  ExpTy transExp(Absyn.SeqExp e) {
    Type type = VOID;
    for (ExpList exp = e.list; exp != null; exp = exp.tail) {
      ExpTy et = transExp(exp.head);
      type = et.ty;
    }
    return new ExpTy(null, type);
  }

  ExpTy transExp(Absyn.AssignmentExpression e) {
    ExpTy var = transVar(e.var);
    ExpTy exp = transExp(e.exp);
    if (!(exp.ty.coerceTo(var.ty)))
      error(e.pos, "Assignment type mismatch.");
    return new ExpTy(null, (Type)VOID);
  }

  ExpTy transExp(Absyn.IfExp e) {
    ExpTy test = transExp(e.test);
    checkInt(test, e.test.pos);
    ExpTy thenClause = transExp(e.thenClause);
    ExpTy elseClause = transExp(e.elseClause);
    if (!thenClause.ty.coerceTo(elseClause.ty) && !elseClause.ty.coerceTo(thenClause.ty))
      error(e.pos, "Result type mismatch.");
    return new ExpTy(null, elseClause.ty);
  }

  ExpTy transExp(Absyn.WhileStatement e) {
    ExpTy test = transExp(e.test);
    checkInt(test, e.test.pos);
    LoopSemant loop = new LoopSemant(env);
    ExpTy body = loop.transExp(e.body);
    if (!body.ty.coerceTo((Type)VOID))
      error(e.body.pos, "Result type mismatch.");
    return new ExpTy(null, (Type)VOID);
  }

  ExpTy transExp (Absyn.BreakExp e) {
    error(e.pos, "Break outside loop.");
    return new ExpTy(null, (Type)VOID);
  }

  ExpTy transExp(Absyn.LetExp e) {
    env.venv.beginScope();
    env.tenv.beginScope();
    for (Absyn.DecList d = e.decs; d != null; d = d.tail) {
      transDec(d.head);
    }
    ExpTy body = transExp(e.body);
    env.venv.endScope();
    env.tenv.endScope();
    return new ExpTy(null, body.ty);
  }

  ExpTy transExp (Absyn.ArrayTy e) {
    NAME name = (NAME)env.tenv.get(e.typ);
    ExpTy size = transExp(e.size);
    ExpTy init = transExp(e.init);
    checkInt(size, e.size.pos);

    if (name != null) {
      Type actual = name.actual();
      if (actual instanceof ARRAY) {
        ARRAY array = (ARRAY)actual;
        if (!init.ty.coerceTo(array.element))
          error(e.init.pos, "Element type mismatched.");
        return new ExpTy(null, (Type)name);
      }
      else
        error(e.pos, "Array type required.");
    }
    else {
      error(e.pos, "Undeclared type: " + e.typ);
    }
    return new ExpTy(null, (Type)VOID);
  }
  

  /*ExpTy transExp(Absyn.ForExp e) {
    ExpTy lo = transExp(e.lo);
    ExpTy hi = transExp(e.hi);
  
    checkInt(lo, e.lo.pos);
    checkInt(hi, e.hi.pos);
  
    env.venv.beginScope();
    env.venv.put(e.var, new VarEntry(INT));
  
    inLoop = true;
    ExpTy body = transExp(e.body);
    inLoop = false;
  
    env.venv.endScope();
  
    return new ExpTy(null, VOID);
  }*/

  private void transFields(int epos, Absyn.StructMember f, Absyn.FieldExpList exp) {
    if (f == null) {
      if (exp != null) 
        error(exp.pos, "Too many expressions.");
    }
    if (exp == null) {
      error(epos, "Missing expression for " + f.fieldName);
    }
    ExpTy e = transExp(exp.init);
    if (exp.name != f.fieldName)
      error(exp.pos, "Field name mismatch.");
    if (!e.ty.coerceTo(f.fieldType))
      error(exp.pos, "Field type mismatch.");
  }

  public void transProg(Absyn.Exp exp) {
    transExp(exp);
  }

  private boolean compParamTypes(Absyn.StructMember formal1, Absyn.StructMember formal2) {
    if (formal1 == null || formal2 == null) 
      return (formal1 == formal2);
    if (!formal1.fieldType.coerceTo(formal2.fieldType) || !formal2.fieldType.coerceTo(formal1.fieldType))
      return false;
    return compParamTypes(formal1.tail, formal2.tail);
  }

  private Absyn.StructMember transTypeFields(Hashtable Hash, Absyn.FieldList f) {
    if (f == null)
      return null;
    NAME name = (NAME)env.tenv.get(f.typ);
    if (name == null)
      error(f.pos, "Undeclared type: " + f.typ);
    if (Hash.put(f.name, f.name) != null)
      error(f.pos, "Function redeclared: " + f.name);
    return new Absyn.StructMember(f.name, (Type)name, transTypeFields(Hash, f.tail));
  }

  Type transTy(Absyn.Ty t) {
    if (t instanceof NameTy) 
      return transTy((NameTy)t);
    if (t instanceof RecordTy)
      return transTy((RecordTy)t);
    if (t instanceof ArraryTy)
      return transTy((ArraryTy)t);
    throw new Error("Semant.transTy");
  }

  Type transTy(Absyn.NameTy t) {
    if (t == null)
      return VOID;
    NAME name = (NAME)env.tenv.get(t.name);
    if (name != null)
      return name;
    error(t.pos, "Undeclared type: " + t.name);
    return VOID;
  }

  Type transTy(Absyn.RecordTy t) {
    Absyn.StructMember type = transTypeFields(new Hashtable<>(), t.fields);
    if (type != null)
      return type;
    return VOID;
  }

  Type transTy(Absyn.ArrayTy t) {
    NAME name = (NAME)env.tenv.get(t.typ);
    if (name != null)
      return new ARRAY(name);
    error(t.pos, "Undeclared type: " + t.typ);
    return (Type)VOID;
  }

  ExpTy transVar(Absyn.VarDeclaration v) {
    // Handle VarDeclaration specifically
    if (v instanceof Absyn.VarDeclaration) {
        Entry x = (Entry) env.venv.get(v.name);
        if (x instanceof VarEntry) {
            VarEntry ent = (VarEntry) x;
            return new ExpTy(null, ent.ty);
        } else {
            error(v.pos, "Undefined variable.");
            return new ExpTy(null, INT); // Default return
        }
    }
    // Handle FieldList
    if (v instanceof Absyn.FieldList) {
        return transVar((Absyn.FieldList) v);
    }
    // Handle ArrayExpression
    if (v instanceof Absyn.ArrayExpression) {
        return transVar((Absyn.ArrayExpression) v);
    }
    throw new Error("Semant.transVar");
}

  
ExpTy transVar(Absyn.FieldList v) {
  ExpTy var = transVar(v.var); // Recursively check the base variable
  Type actual = var.ty.actual();
  if (actual instanceof Absyn.StructMember) {
      for (Absyn.StructMember field = (Absyn.StructMember) actual; field != null; field = field.tail) {
          if (field.fieldName.equals(v.field)) { // Compare field names
              return new ExpTy(null, field.fieldType);
          }
      }
      error(v.pos, "Undeclared field: " + v.field);
  } else {
      error(v.var.pos, "Absyn.StructMember required.");
  }
  return new ExpTy(null, VOID); // Default return for error cases
}

ExpTy transVar(Absyn.ArrayExpression v) {
  ExpTy var = transVar(v.var); // Check the base variable
  ExpTy index = transExp(v.index); // Get the index expression
  checkInt(index, v.index.pos); // Ensure it's an integer
  Type actual = var.ty.actual();
  if (actual instanceof ARRAY) {
      ARRAY array = (ARRAY) actual;
      return new ExpTy(null, array.element); // Return the type of the array element
  }
  error(v.var.pos, "Array required.");
  return new ExpTy(null, VOID); // Default return for error cases
}

}