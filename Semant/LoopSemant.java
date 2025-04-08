package Semant;

import Absyn.BreakExp;
import Translate.Exp;
import Translate.Level;

class LoopSemant extends Semant {
   LoopSemant(Env e, Level l) {
      super(e, l);
   }

   ExpTy transExp(BreakExp e) {
      return new ExpTy((Exp)null, Semant.VOID);
   }
}