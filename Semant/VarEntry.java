package Semant;

import Types.Type;

public class VarEntry extends Entry {
   public Type ty;

   VarEntry(Type t) {
      this.ty = t;
   }
}