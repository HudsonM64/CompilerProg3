package Absyn;
import java.util.ArrayList;

import Symbol.Symbol;
public class Parameter extends Decl {
    public Type type;
    public String name;
    public Parameter(int p, Type t, String n) {
        pos=p;
        name = n;
        type = t;
    }
}
