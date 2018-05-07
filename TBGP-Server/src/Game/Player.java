package Game;
import Server.*;

/**
 * The Class Player.
 */
public class Player {
	
	/** The player's nickname. */
	private String nickname;
	
	/** The player's callback. */
	private ProtocolCallback<String> callback;
	
	/** The room the player is inside. */
	private Room room=null;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param nickname the nickname
	 * @param callback the callback
	 */
	public Player(String nickname, ProtocolCallback<String> callback){
		this.nickname=nickname;
		this.callback=callback;
	}
	
	/**
	 * Gets the player's name.
	 *
	 * @return the name
	 */
	public String getName() {
		return nickname;
	}

	/**
	 * Gets the player's callback.
	 *
	 * @return the callback
	 */
	public ProtocolCallback<String> getCallback() {
		return callback;
	}

	/**
	 * Gets the player's room.
	 *
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Sets the player's room.
	 *
	 * @param room the new room
	 */
	public void setRoom(Room room) {
		if(this.room!=null){
			this.room.removePlayer(this);
		}
		this.room = room;
	}

}
