package Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


// TODO: Auto-generated Javadoc
/**
 * The Class BlufferGame.
 * Implementing Game class, every instance simulates a game.
 */
public class BlufferGame implements Game{
	
	/** The room of every specific game instance. */
	private Room room;
	
	/** The name. */
	private final String name = "BLUFFER";
	
	/** The "already answered" players counter. */
	private int alreadyAnswered = 0;
	
	/** The current question counter. */
	private int currentQuestion=0;
	
	/** The questions array. */
	private Question[] questions = new Question[3];
	
	/** The questions bank. */
	private ArrayList<Question> questionsBank= new ArrayList<Question>();
	
	/** The player responses array. */
	private ArrayList<String> responses = new ArrayList<String>();
	
	/** The game score map. */
	private ConcurrentHashMap<Player, BlufferScore> scoreMap = new ConcurrentHashMap<Player, BlufferScore>();
	
	/** The players answers map. */
	private ConcurrentHashMap<String, ArrayList<Player>> playersAnswers = new ConcurrentHashMap<String, ArrayList<Player>>();
	
	/** The time to select indicates whether it is time to text response or select response. */
	private boolean timeToSelect = false;
	
	
	
	/**
	 * Instantiates a new bluffer game.
	 */
	public  BlufferGame() {}

	
	@Override
	public void txtResponse(String response , Player player){
		responses.add(response.toLowerCase());
		if (!playersAnswers.containsKey(response.toLowerCase())){
			playersAnswers.put(response.toLowerCase(), new ArrayList<Player>());
		}
		playersAnswers.get(response.toLowerCase()).add(player);
		alreadyAnswered++;
		if (everybodyAnswered()){
			if (!responses.contains(questions[currentQuestion].getAnswer().toLowerCase())){
				responses.add(questions[currentQuestion].getAnswer().toLowerCase());
			}
			alreadyAnswered=0;
			timeToSelect = true;
			askChoices();
		}
	}

	
	@Override
	public void selectResponse(String response, Player player) {
		String selectedResponse = responses.get((Integer.decode(response)).intValue());
		if (playersAnswers.containsKey(selectedResponse)){//won't get inside this 'if' state if and only if the player has chosen the right answer, and no one else has written it as a 'bluff' answer
			for (Player p : playersAnswers.get(selectedResponse)){
				scoreMap.get(p).increaseRoundScore(5);
			}
		}
		if (selectedResponse.equals(questions[currentQuestion].getAnswer().toLowerCase())){
			scoreMap.get(player).increaseRoundScore(10);
		}
		alreadyAnswered++;
		
		if (everybodyAnswered()){
			for (Player p : room.getPlayers()){	
				try {
					p.getCallback().sendMsg("GAMEMSG: The correct answer is: " + questions[currentQuestion].getAnswer().toLowerCase());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (scoreMap.get(p).answeredRight()){
					try {
						p.getCallback().sendMsg("GAMEMSG: correct! +" + scoreMap.get(p).getRoundScore() + "pts");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						p.getCallback().sendMsg("GAMEMSG: wrong! +" + scoreMap.get(p).getRoundScore() + "pts");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				scoreMap.get(p).increaseTotalScore( scoreMap.get(p).getRoundScore() );
				scoreMap.get(p).resetRound();	
			}
			alreadyAnswered=0;
			responses.clear();
			playersAnswers.clear();
			timeToSelect = false;
			if (currentQuestion<2){//Next Question
				currentQuestion++;
				askQuestion(questions[currentQuestion].getQuestion());
			}
			else {//GameOver
				String summary="SYSMSG: GAMEMSG Summary: ";
				for (Player p : room.getPlayers()){	
					summary += p.getName()+": " + scoreMap.get(p).getTotalScore() + "pts, ";
				}
				summary.substring(0, summary.length()-3);//deleting the last ", "
				broadcastMessage(summary);
				currentQuestion=0;
				room.endGame();
				broadcastMessage("GAMEMSG: GAMEOVER!");
			}
			
		}

	}
	
	@Override
	public void startGame(Room room){
		synchronized (room) {
			this.room = room;
			for (Player player : room.getPlayers()){
				scoreMap.put(player, new BlufferScore());
			}
		}
		if (questionsBank.size()<3){
			//creating reader
			JsonReader jreader = null;
			while (jreader == null){
				try {
					jreader = new JsonReader(new FileReader("/users/studs/bsc/2016/omerbenc/new-workspace/Ass3Server/bluffer.json"));
				} catch (FileNotFoundException e) {

				}
			}

			//reading Json
			JsonParser jparser = new JsonParser();
			JsonElement element = jparser.parse(jreader);
			if (element.isJsonObject()){
				JsonObject jobject = element.getAsJsonObject();
				JsonArray questions = jobject.get("questions").getAsJsonArray();
				for (JsonElement question : questions){ 
					String q = (question.getAsJsonObject().get("questionText").getAsString());
					String a = (question.getAsJsonObject().get("realAnswer").getAsString());
					questionsBank.add(new Question(q, a));
				}
			}
		}
		for (int i=0 ; i<3 ; i++){//selecting 3 random questions from the questionsBank for the specific game session
			questions[i] = questionsBank.remove((int)(Math.random()*questionsBank.size()));
		}
		for (Question q:questions){
			System.out.println(q.getQuestion());
		}
		askQuestion(questions[currentQuestion].getQuestion());
		
	}
	
	
	/**
	 * Everybody answered.
	 *
	 * @return true, if all of the players inside the game has answered (the current round's question).
	 */
	public boolean everybodyAnswered(){
		return room.getNumOfPlayersInRoom()==alreadyAnswered;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Game createGame() {
		return new BlufferGame();
	}
	
	/**
	 * Ask all of the players a question.
	 *
	 * @param question the question
	 */
	public void askQuestion(String question){
		broadcastMessage("ASKTXT: " + question);
	}
	
	/**
	 * Ask all of the players to submit their choice for the specific question.
	 */
	public void askChoices(){
		ArrayList<String> tempResponses = new ArrayList<String>();
		for (int i=responses.size() ; i>0 ; i--){
			tempResponses.add(responses.remove((int)(Math.random()*i)));
		}
		responses=tempResponses;
		String choices = "";
		for (int i=0 ; i<responses.size() ; i++){
			choices += i+". " + responses.get(i) + "; ";
		}
		broadcastMessage("ASKCHOICES: " + choices );
	}
	
	/**
	 * Broadcasts a message to all the players in the game 
	 * (in the game's room).
	 *
	 * @param message the message
	 */
	private void broadcastMessage(String message){
		for (Player player : room.getPlayers()){
			try {
				player.getCallback().sendMsg(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean isTimeToSelect(){
		return timeToSelect;
	}
}

 
