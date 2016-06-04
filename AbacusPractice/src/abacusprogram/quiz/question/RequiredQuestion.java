package abacusprogram.quiz.question;

/**
 * Created on 3/29/16.
 */
public class RequiredQuestion{
    private int value;
    private boolean bAsked;

    public RequiredQuestion(int value) {
        this.value = value;
        this.bAsked = false;
    }

    //========================GETTERS AND SETTERS==================================//

    public void asked()
    {
        bAsked = true;
    }

    public boolean hasBeenAsked() {
        return bAsked;
    }

    public int getValue() {
        return value;
    }


    //========================OVERIDE FROM OBJECT====================//
    //i know this is wrong way of implementing clone but its so much simpler in my case
    /*
        @return: returns a unused copy of this requiredQuestion (hasBeenAsked() == false)
     */
    public RequiredQuestion clone()
    {
        RequiredQuestion out = new RequiredQuestion(value);
        out.bAsked = false;
        return out;
    }

    //============================UTILS=====================================//
    public static RequiredQuestion[] copyArray(RequiredQuestion[] in)
    {
        RequiredQuestion[] out = new RequiredQuestion[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].clone();
        }
        return out;
    }
}
