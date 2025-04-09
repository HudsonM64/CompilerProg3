package Semant;

import Absyn.BreakExp;
import Translate.Exp;
import Translate.Level;

class LoopSemant extends Semant {
   LoopSemant(Env e) {
      super(e);
   }

   ExpTy transExp(BreakExp e) {
      return new ExpTy((Exp)null, Semant.VOID);
   }
}