package Translate;

public class Access {
   Level home;
   Frame.Access acc;

   Access(Level h, Frame.Access a) {
      this.home = h;
      this.acc = a;
   }

   public String toString() {
      return "[" + this.home.frame.name.toString() + "," + this.acc.toString() + "]";
   }
}