package Game;

/**
 * The Class BlufferScore.
 * contains the dedicated score to handle a bluffer game 
 */
public class BlufferScore {
	
	/** The total score. */
	private int totalScore=0;
	
	/** The round score. */
	private int roundScore=0;
	
	/** The "answered right" indicator. */
	private boolean answeredRight=false;
	
	/**
	 * Instantiates a new bluffer score.
	 */
	public BlufferScore(){}

	/**
	 * Gets the total score.
	 *
	 * @return the total score
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * Sets the total score.
	 *
	 * @param totalScore the new total score
	 */
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
	/**
	 * Increase total score.
	 *
	 * @param pointsToAdd the points to add
	 */
	public void increaseTotalScore(int pointsToAdd){
		totalScore+=pointsToAdd;
	}
	
	/**
	 * Gets the round score.
	 *
	 * @return the round score
	 */
	public int getRoundScore() {
		return roundScore;
	}

	/**
	 * Sets the round score.
	 *
	 * @param roundScore the new round score
	 */
	public void setRoundScore(int roundScore) {
		this.roundScore = roundScore;
	}
	
	/**
	 * Increase round score.
	 *
	 * @param pointsToAdd the points to add
	 */
	public void increaseRoundScore(int pointsToAdd){
		if (pointsToAdd==10){//indicates that the answer was right
			answeredRight=true;
		}
		roundScore+=pointsToAdd;
	}
	
	/**
	 * Reset round.
	 * sets the round score to 0
	 */
	public void resetRound(){
		roundScore=0;
		answeredRight=false;
	}
	
	/**
	 * Answered right.
	 * indicates if the player has answered right to the current round's question.
	 * @return true, if successful
	 */
	public boolean answeredRight(){
		return answeredRight;
	}
}
