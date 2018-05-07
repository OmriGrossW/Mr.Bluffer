package Server;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Game.*;

// TODO: Auto-generated Javadoc
/**
 * The Class TBGPServerDatabase.
 * stores all of the data a TBGP game server needs
 */

public class TBGPServerDatabase {
	
	/** The players map. */
	private ConcurrentHashMap<ProtocolCallback<String>, Player> playersMap = new ConcurrentHashMap<ProtocolCallback<String>, Player>();
	
	/** The rooms map. */
	private ConcurrentHashMap<String, Room> roomsMap = new ConcurrentHashMap<String, Room>();
	
	/** The games map. */
	private ConcurrentHashMap<String, Game> gamesMap = new ConcurrentHashMap<String, Game>();
	
	/** The games list. */
	private ArrayList<String> gamesList=new ArrayList<String>();
	
	/** The nickname list. */
	private ArrayList<String> nicknameList=new ArrayList<String>();
	
	/** The singleton instance data. */
	private static TBGPServerDatabase data = new TBGPServerDatabase();
	
	/**
	 * Instantiates a new TBGP server database.
	 */
	private TBGPServerDatabase(){
		gamesList.add("BLUFFER");
		gamesMap.put("BLUFFER", new BlufferGame());
	};
	
	/**
	 * Gets the singleton data instance.
	 *
	 * @return the singleton data instance
	 */
	public static TBGPServerDatabase getDataInstance(){
		return data;
	}
	
	/**
	 * Gets the player according to his specific callback.
	 *
	 * @param callback the callback
	 * @return the player
	 */
	public Player getPlayer(ProtocolCallback<String> callback){
		return playersMap.get(callback);
	}
	
	/**
	 * Gets the room by his name.
	 *
	 * @param roomName the room name
	 * @return the room
	 */
	public Room getRoom(String roomName){
		if (!roomsMap.containsKey(roomName)){
			return null;
		}
		return roomsMap.get(roomName);
	}
	
	/**
	 * Gets the game by his name.
	 *
	 * @param gameName the game name
	 * @return the game
	 */
	public Game getGame(String gameName){
		return gamesMap.get(gameName);	
	}
	
	/**
	 * Gets the game list array of the server.
	 *
	 * @return the game list array
	 */
	public ArrayList<String> getGameList(){
		return gamesList;
	}
	
	/**
	 * Gets the game list string of the server as a String list.
	 *
	 * @return the game list String
	 */
	public String getGameListString(){
		String gameListString="GAME LIST:\n";
		for (String s : gamesList){
			gameListString += "\t" + s + "\n";
		}
		return gameListString;
	}
	
	/**
	 * Checks whether the players map contains a player by his callback.
	 *
	 * @param callback the callback
	 * @return true, if successful
	 */
	public boolean containsPlayer(ProtocolCallback<String> callback){
		return playersMap.containsKey(callback);
	}
	
	/**
	 * Checks whether the rooms map contains a room by his room.
	 *
	 * @param roomName the room name
	 * @return true, if successful
	 */
	public boolean containsRoom(String roomName){
		return roomsMap.containsKey(roomName);
	}
	
	/**
	 * Adds the player to the database.
	 *
	 * @param player the player
	 */
	public void addPlayer(Player player){
		playersMap.put(player.getCallback(),player);
		nicknameList.add(player.getName());
	}
	
	/**
	 * Adds the game to the database.
	 *
	 * @param game the game
	 */
	public void addGame(Game game){
		gamesMap.put(game.getName(), game);
		gamesList.add(game.getName());
	}
	
	/**
	 * Adds the room to the database.
	 *
	 * @param roomName the room name
	 */
	public void addRoom(String roomName){
		roomsMap.put(roomName, new Room(roomName));
	}

	/**
	 * Checks whether the players map contains a player by his name.
	 *
	 * @param nickname the nickname
	 * @return true, if successful
	 */
	public boolean containsPlayerByName(String nickname) {
		return (nicknameList.contains(nickname));
	}
	
	/**
	 * Removes the player from the database.
	 *
	 * @param callback the callback
	 */
	public void removePlayer(ProtocolCallback<String> callback){
		System.out.println(callback==null);
		if (playersMap.containsKey(callback)){
			nicknameList.remove(getPlayer(callback).getName());
			playersMap.remove(callback);
		}
	}
	
}
