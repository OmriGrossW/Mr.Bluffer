package Game;

/**
 * The Class Question.
 */
public class Question {
	
	/** The question. */
	private String question;
	
	/** The answer matching the question. */
	private String answer;
	
	/**
	 * Instantiates a new question.
	 * 
	 * @param question the question
	 * @param answer the answer
	 */
	public Question(String question, String answer){
		this.question = question;
		this.answer = answer;
	}
	
	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public String getQuestion(){
		return question;
	}
	
	/**
	 * Gets the answer.
	 *
	 * @return the answer
	 */
	public String getAnswer(){
		return answer;
	}
}
