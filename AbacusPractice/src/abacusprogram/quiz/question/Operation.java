package abacusprogram.quiz.question;

/**
 * Created on 2/21/16.
 */
public enum Operation {
    ADDITION("+", (d1, d2) -> d1 + d2),
    SUBTRACTION("-", (d1, d2) -> d1 - d2),
    MULTIPLICATION("*", (d1, d2) -> d1 * d2),
    DIVISION("/", (d1, d2) -> d1/d2);


    //======================ENUM STUFF======================//
    private final String asString;
    private final Operatable operatable;

    Operation(String asString, Operatable operatable)
    {
        this.asString = asString;
        this.operatable = operatable;
    }

    @Override
    public String toString() {
        return asString;
    }

    public static Operation parseOperation(String s)
    {
        switch (s)
        {
            case "+":
                return ADDITION;
            case "-":
                return SUBTRACTION;
            case "*":
                return MULTIPLICATION;
            case "/":
                return DIVISION;
        }
        return ADDITION;
    }

    public double doOperation(double d1, double d2)
    {
        return operatable.doOperation(d1, d2);
    }

    private interface Operatable
    {
        double doOperation(double d1, double d2);
    }

}
