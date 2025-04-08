package Translate;

import Frame.Frame;
import Symbol.Symbol;
import Temp.Label;
import Util.BoolList;

public class Level {
   Level parent;
   Frame frame;
   public AccessList formals;
   public AccessList frameFormals;

   public Level(Frame f) {
      this.frame = f;
   }

   public Level(Level parent, Symbol name, BoolList formals) {
      this(parent, name, formals, false);
   }

   public Level(Level parent, Symbol name, BoolList formals, boolean leaf) {
      this.parent = parent;
      this.frame = parent.frame.newFrame(name, new BoolList(leaf ^ true, formals));
      this.frameFormals = this.allocFormals(this.frame.formals);
      this.formals = this.frameFormals.tail;
   }

   private AccessList allocFormals(Frame.AccessList formals) {
      return formals == null ? null : new AccessList(new Access(this, formals.head), this.allocFormals(formals.tail));
   }

   public Access allocLocal(boolean escape) {
      return new Access(this, this.frame.allocLocal(escape));
   }

   public Label name() {
      return this.frame.name;
   }
}