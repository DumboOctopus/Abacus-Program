package abacusprogram.quiz.question.generator;

/**
 * USE THIS TO GAREENTEE OUTPUT IS POSITIVE/
 */
public class PositiveContinuallyGenerator implements QuestionGenerator {


    private QuestionGenerator generator;
    private int[] nums;
    private int index = 0;

    public PositiveContinuallyGenerator(QuestionGenerator generator, int length) {
        this.generator = generator;

        nums = new int[length];

        reset();
    }

    @Override
    public int getNextValue(int pastValue) {
        index++;
        if(index >= nums.length)
        {
            return -1;
        }
        return  nums[index - 1];
    }

    @Override
    public int getNextValue() {
        reset();
        index++;
        if(index >= nums.length)
        {
            return -1;
        }
        return  nums[index - 1];
    }


    public void reset(){
        //init first value
        int pastValue;
        do {
            pastValue = generator.getNextValue();
        }while (pastValue < 0);
        nums[0] = pastValue;
        System.out.println("num[0]" +nums[0]);

        int sum = pastValue;
        for (int i = 1; i < nums.length; i++) {
            int tmp;
            do {
                tmp = generator.getNextValue(pastValue);
            }while (sum + tmp <= 0);
            nums[i] = tmp;
            sum += tmp;
            pastValue = tmp;
        }


        index = 0;
    }
}
