package Game;

/**
 * The Interface Game.
 */
public interface Game {
	
	/**
	 * Start game.
	 *	initializing all of the fields required to start a specific game
	 * @param room the room which the game is played in.
	 */
	public void startGame(Room room);
	
	/**
	 * Creates the game .
	 *
	 * @return the game
	 */
	public Game createGame();
	
	/**
	 * Txt response.
	 * used when a player responses to a query.
	 * @param response the response
	 * @param player the player
	 */
	public void txtResponse(String response , Player player);
	
	/**
	 * Select response.
	 * used when a player responses to a selection phase query.
	 * @param response the response
	 * @param player the player
	 */
	public void selectResponse(String response , Player player);
	
	/**
	 * Gets the name of the game.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Checks if is time to select.
	 * indicates whether it is time to text response or select response.
	 * @return true, if is time to select
	 */
	public boolean isTimeToSelect();
	
	

}
