package Symbolics;

public interface Member {
    public abstract Member add(Member oper);
    public abstract Member sub(Member oper);
    public abstract Member mult(Member oper);
    public abstract Member divi(Member oper);
}
