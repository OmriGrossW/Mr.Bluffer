package Server;
import java.io.IOException;
import java.util.Iterator;

import Game.Player;

/**
 * The Class TBGPProtocol.
 * implements AsyncServerProtocol
 */
public class TBGPProtocol implements AsyncServerProtocol<String> {
	
	/** The data of the server. */
	private TBGPServerDatabase data=TBGPServerDatabase.getDataInstance();
	
	/** The should close indicator. */
	private boolean shouldClose = false;
	
	/** The connection terminated indicator. */
	private boolean connectionTerminated=false;
	
	/**
	 * Instantiates a new TBGP protocol.
	 */
	public TBGPProtocol() { }
	
	@Override
	public void processMessage(String msg, ProtocolCallback<String> callback){
		String command="";
		String parameters="";
		Player player=null;
		if (msg.contains(" ")){
			command = msg.substring(0,msg.indexOf(" "));
			parameters = msg.substring(msg.indexOf(" ")+1);
		}
		else {
			command = msg;
		}
		
		if (isEnd(command)){
			shouldClose=true;
			try {
				callback.sendMsg("SYSMSG "+command+ " ACCEPTED: QUITING...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (data.containsPlayer(callback)){ //only for command types different then "NICK", means they already exist in the data and players map
			player= data.getPlayer(callback);
			
			switch (command) {
			case "JOIN":
				if (data.getRoom(parameters)==null){
					data.addRoom(parameters);
				}
				if(!data.getRoom(parameters).isActive()){
					if(player.getRoom()!=null){
						if (player.getRoom().isActive()){
							try {
								callback.sendMsg("SYSMSG "+command+ " REJECTED: YOU ARE PLAYING RIGHT NOW!");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					else{
						player.setRoom(data.getRoom(parameters));
						data.getRoom(parameters).addPlayer(player);
						try {
							callback.sendMsg("SYSMSG "+command+ " ACCEPTED.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}	
				}
				else{
					try {
						callback.sendMsg("SYSMSG "+command+ " REJECTED: THE REQUESTED ROOM IS IN GAME.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				break;
				
			case "MSG":
				if(player.getRoom()!=null){
					if(player.getRoom().getNumOfPlayersInRoom()>1){
						Iterator<Player> it = player.getRoom().getPlayers().iterator();
						while(it.hasNext()){
							Player currentPlayer = it.next();
							if(currentPlayer!=player){
								try {
									currentPlayer.getCallback().sendMsg("USRMSG: "+parameters);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						try {
							callback.sendMsg("SYSMSG "+command+ " ACCEPTED: YOUR MASSEGE HAS BEEN SENT.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else{
						try {
							callback.sendMsg("SYSMSG "+command+ " REJECTED: YOU ARE THE ONLY PLAYER IN THE ROOM.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					try {
						callback.sendMsg("SYSMSG "+command+ " REJECTED: YOU ARE NOT INSIDE ANY ROOM.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			case "LISTGAMES":
				try {
					callback.sendMsg("SYSMSG "+command+ " ACCEPTED: YOUR MASSEGE HAS BEEN SENT.");
					callback.sendMsg(data.getGameListString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "STARTGAME":
				if (player.getRoom()!=null){
					if(data.getGameList().contains(parameters)){
						if (!player.getRoom().isActive()){
							synchronized (player.getRoom()) {
								player.getRoom().startGame();
								for (Player p : player.getRoom().getPlayers()){
									try {
										p.getCallback().sendMsg("SYSMSG "+command+ " ACCEPTED: "+parameters+"GAME STARTED.");
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								player.getRoom().setGame(data.getGame(parameters));
							}
						}
						else{
							try {
								callback.sendMsg("SYSMSG "+command+ " REJECTED: THE ROOM IS CURRENTLY PLAYING OTHER GAME.");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					else{
						try {
							callback.sendMsg("SYSMSG "+command+ " REJECTED: NO SUCH GAME IN THE SYSTEM.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					try {
						callback.sendMsg("SYSMSG "+command+ " REJECTED: FIND YOURSELF A ROOM FIRST!!");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			case "TXTRESP":
				if (!player.getRoom().isActive()){
					try {
						callback.sendMsg("SYSMSG "+command+ " REJECTED: NO ONE ASKED YOU TO RESPONSE.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					if(!player.getRoom().getGame().isTimeToSelect()){
						try {
							callback.sendMsg("SYSMSG "+command+ " ACCEPTED.");
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.getRoom().getGame().txtResponse(parameters, player);
					}
					else{
						try {
							callback.sendMsg("SYSMSG "+command+ " REJECTED: THIS IS NOT THE TIME TO TXT RESPONSE, SELECT YOUR ANSWER.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case "SELECTRESP":
				if (!player.getRoom().isActive()){
					try {
						callback.sendMsg("SYSMSG "+command+ " REJECTED: NOTHING TO SELECT YOU ARE NOT IN A ROOM.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					if(player.getRoom().getGame().isTimeToSelect()){
						try {
							callback.sendMsg("SYSMSG "+command+ " ACCEPTED.");
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.getRoom().getGame().selectResponse(parameters, player);
					}
					else{
						try {
							callback.sendMsg("SYSMSG "+command+ " REJECTED: THIS IS NOT THE TIME TO SELECT RESPONSE, BEHAVE YOURSELF.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				break;
				
			}
		}
		else{
			if(command.equals("NICK")) {
				if (data.containsPlayerByName(parameters)){
					try {
						callback.sendMsg("SYSMSG "+command+ " REJECTED: NICKNAME ALREADY EXIST.");
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
				else{
					data.addPlayer(new Player(parameters,  callback));
					try {
						callback.sendMsg("SYSMSG "+command+ " ACCEPTED.");
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
			}
		}
	}
	
	@Override
	public boolean isEnd(String msg){
		return msg.equals("QUIT");
	}


	@Override
	public boolean shouldClose() {
		return shouldClose;
	}

	@Override
	public void connectionTerminated() {
		connectionTerminated = true;
	}


}
