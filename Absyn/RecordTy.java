package Absyn;

public class RecordTy extends Ty {
   public FieldList fields;

   public RecordTy(int p, FieldList f) {
      super.pos = p;
      this.fields = f;
   }
}