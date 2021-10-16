import java.util.*;
//Source from: https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html

public class Question {
	
	String question;
	String[] answers;
	boolean[] correctAnswerIndex;
	
	public Question(String q, String[] ans, boolean[] ind) {
		question = q;
		answers = ans;
		correctAnswerIndex = ind;
	}
	
	public boolean correct(boolean[] ans) {
		for(int i = 0; i < correctAnswerIndex.length; i++) {
			if(ans[i]!=correctAnswerIndex[i]) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return question + " " + Arrays.toString(answers) + " " + Arrays.toString(correctAnswerIndex);
	}
	
}
