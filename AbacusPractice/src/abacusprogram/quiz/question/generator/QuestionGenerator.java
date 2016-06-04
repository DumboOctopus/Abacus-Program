package abacusprogram.quiz.question.generator;

/**
 * This is a fairly interesting interface:
 *
 * it gets values for a certain set of values
 */
public interface QuestionGenerator {

    /*
    called on the first call for a value. This also serves as a reset
     */
    int getNextValue();

    /*
    called on subsequent calls. It might use pastValue to calculate new, or it might not
    depends on implementation.
     */
    int getNextValue(int pastValue);


}
