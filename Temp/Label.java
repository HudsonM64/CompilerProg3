package Temp;

import Symbol.Symbol;

public class Label {
   private String name;
   private static int count;

   public Label() {
      this("L" + count++);
   }

   public Label(Symbol s) {
      this(s.toString());
   }

   public Label(String n) {
      this.name = n;
   }

   public String toString() {
      return this.name;
   }
}