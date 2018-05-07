package Game;

import java.util.Vector;


// TODO: Auto-generated Javadoc
/**
 * The Class Room.
 * A room inside a server.
 */
public class Room {
	
	/** The room's name. */
	private String name;
	
	/** The game. */
	private Game game;
	
	/** The is active indicator, checks if there is a game running in the room. */
	private boolean isActive = false;
	
	/** The num of players in room. */
	private int numOfPlayersInRoom = 0;
	
	/** The players. */
	private Vector<Player> players = new Vector<Player>();
	
	/**
	 * Instantiates a new room.
	 *
	 * @param name the name
	 */
	public Room(String name){
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the game.
	 *
	 * @return the game
	 */
	public Game getGame(){
		return game;
	}
	
	/**
	 * Gets the room's players list (Vector).
	 *
	 * @return the players
	 */
	public Vector<Player> getPlayers(){
		return players;
	}
	
	/**
	 * Adds the given player to the room.
	 *
	 * @param player the player
	 */
	public synchronized void addPlayer(Player player){
		players.addElement(player);
		numOfPlayersInRoom++;
	}
	
	/**
	 * Sets the game.
	 *
	 * @param game the new game
	 */
	public synchronized void setGame(Game game){
		this.game = game.createGame();
		this.game.startGame(this);
	}
	
	/**
	 * Removes the player from the room.
	 *
	 * @param player the player
	 */
	public synchronized void removePlayer(Player player){
		players.remove(player);
		numOfPlayersInRoom--;
	}
	
	/**
	 * Checks if there is a game running in the room..
	 *
	 * @return true, if is active
	 */
	public boolean isActive(){
		return isActive;
	}
	
	/**
	 * Starts the game.
	 */
	public void startGame(){
		isActive = true;
	}
	
	/**
	 * Ends the game.
	 */
	public void endGame(){
		isActive = false;
	}
	
	/**
	 * Gets the number of players in room.
	 *
	 * @return the number of players in room
	 */
	public int getNumOfPlayersInRoom(){
		return numOfPlayersInRoom;
	}
	
}
