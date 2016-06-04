package abacusprogram.quiz.question;

/**
 * this class stores data for the idea of the section C tests. The C test include questions where
 * certain 1's digit operations between 2 specific numbers is required. For example, some
 * may force a 5 + 6 situation to occur in the one's digit.
 *
 * the first number in the (5 + 6) example is the value variable while the second is the secondValue variable
 * in order to get represent something like (5 - 6), where the operation is subtraction, this secondValue
 * will be negative
 */
public class Required1ColumnSum extends RequiredQuestion{
    private int secondValue;
    public Required1ColumnSum(int value, int secondValue) {
        super(value);
        this.secondValue = secondValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    @Override
    public RequiredQuestion clone() {
        return new Required1ColumnSum(getValue(), secondValue);
    }

    @Override
    public String toString() {
        return "Required1ColumnSum{" +
                "value=" + getValue() +
                "secondValue=" + secondValue +
                "hasBeenAsked=" + hasBeenAsked() +
                '}';
    }
}
