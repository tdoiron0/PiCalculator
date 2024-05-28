package Symbolics;

import java.util.ArrayList;

public class Number implements Member {
    private ArrayList<Integer> digits = new ArrayList<>();

    public Number(String src) {
        for (int i = 0; i < src.length(); ++i) {
            digits.add(src.charAt(i) - 48);
        }
    }

    public int digitAt(int index) { return digits.get(index); }
    public int size() { return digits.size(); }

    @Override
    public Member add(Member oper) {
        if (oper == null) {
            throw new IllegalArgumentException("oper must not be null");
        }
        if (oper.getClass() != getClass()) {
            throw new IllegalArgumentException("Can only operate on members of the same type");
        }

        Number numOper = (Number)oper;

        int i = digits.size() - 1;
        int j = numOper.size() - 1;
        int carryOut = 0b0;
        String result = "";
        while (i >= 0 && j >= 0) {
            int temp = digits.get(i) + numOper.digitAt(j) + carryOut;
            carryOut = temp / 10;
            result = (char)((temp % 10) + 48) + result;

            --i;
            --j;
        }
        while (i >= 0) {
            int temp = digits.get(i) + carryOut;
            carryOut = temp / 10;
            result = (char)((temp % 10) + 48) + result;

            --i;
        }
        while (j >= 0) {
            int temp = numOper.digitAt(j) + carryOut;
            carryOut = temp / 10;
            result = (char)((temp % 10) + 48) + result;

            --j;
        }
        if (carryOut != 0) {
            result = (char)(carryOut + 48) + result;
        }

        return new Number(result);
    }

    @Override
    public Member sub(Member oper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sub'");
    }

    @Override
    public Member mult(Member oper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mult'");
    }

    @Override
    public Member divi(Member oper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'divi'");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.size(); ++i) {
            sb.append(digits.get(i));
        }
        return sb.toString();
    }
}
