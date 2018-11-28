package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class Bluffer {
	private CurrGame blufferGame;
	private HashMap<String, ArrayList<ProtocolCallback<StringMessage>>> bluffAnswers;
	private int numOfPlayers;
	private int tmpNumOfPlayers;
	private boolean gameState;
	private Question question;
	private Room room;
	private HashMap<Integer, String> numberedAnswers;
	private HashMap<String, Boolean> isPlayerChoseAnswer;
	
	
	
	public Bluffer (TBGP blufferGame, Room room, Question question ){
		this.blufferGame = (CurrGame) blufferGame;
		this.bluffAnswers = new HashMap<String, ArrayList<ProtocolCallback<StringMessage>>>();
		this.gameState = false;
		this.room = room;
		this.numOfPlayers = room.getNumOfPlayers();
		this.question = question;
		this.tmpNumOfPlayers = 0;
		this.isPlayerChoseAnswer = new HashMap<String, Boolean>();
	}
	
	 public String getQuestion() {
         return question.getQuestion();
 }
	
	public void GetFakeAnswers(String fake, ProtocolCallback<StringMessage> callback){
		if (!bluffAnswers.containsValue(callback) && numOfPlayers!=tmpNumOfPlayers && !gameState){
			if(!fake.equals(question.getAnswer())){
				if(!bluffAnswers.containsKey(fake))
					bluffAnswers.put(fake, new ArrayList<ProtocolCallback<StringMessage>>());
				bluffAnswers.get(fake).add(callback);
				try {
					callback.sendMessage(new StringMessage("SYSMSG TXTRESP ACCEPTED"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				tmpNumOfPlayers++;
				if (tmpNumOfPlayers == numOfPlayers){
					tmpNumOfPlayers = 0;
					this.mixResp();
					
				}
				
			}
			else{
				try {
					callback.sendMessage(new StringMessage("SYSMSG TXTRESP REJECTED"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		else{
			try {
				callback.sendMessage(new StringMessage("SYSMSG TXTRESP REJECTED"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	private void mixResp() {
		 int random = (int)((bluffAnswers.size())*Math.random());
		 numberedAnswers=new HashMap<Integer, String>();
		 numberedAnswers.put(random,question.getAnswer());
		 int i=0;
		 for( String currAns: bluffAnswers.keySet()){
			 if(i!=random)
				 numberedAnswers.put(i, currAns);
				 else {
					 i++;
					 numberedAnswers.put(i, currAns);
				 }
			 i++;
		 }
		 String toChose = "";
		 for (Integer j : numberedAnswers.keySet())
			 toChose=toChose+j+": "+numberedAnswers.get(j)+" ";
		 gameState = true;
         room.sendMess("",new StringMessage("ASKCHOICES "+toChose));  
		 	
	}
	
	public void sendChoise(int ans,String name){
		if(numOfPlayers!=tmpNumOfPlayers && gameState && (!isPlayerChoseAnswer.containsKey(name) || !isPlayerChoseAnswer.get(name)) && ans>=0 && ans<=numOfPlayers){
			if(!numberedAnswers.get(ans).equals(this.question.getAnswer())){
				try {
					Game.getInstance().getCallback(name).sendMessage(new StringMessage("GAMEMSG WORNG ANSWER"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				room.updateScore(0, name);
				int size=bluffAnswers.get(numberedAnswers.get(ans)).size();
				for(int i=0;i<size;i++){
					String gainPlayer= Game.getInstance().getName(bluffAnswers.get(numberedAnswers.get(ans)).get(i));
					room.updateScore(5, gainPlayer);
					try {
						bluffAnswers.get(numberedAnswers.get(ans)).get(i).sendMessage(new StringMessage("GAMEMSG SOMEONE CHOSE YOUR BLUFF +5 POINTS "));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else{
				room.updateScore(10, name);
				try {
					Game.getInstance().getCallback(name).sendMessage(new StringMessage("GAMEMSG ANSWER CORRECT +10 POINTS"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			isPlayerChoseAnswer.put(name, true);
			tmpNumOfPlayers++;
			if (tmpNumOfPlayers == numOfPlayers){
				tmpNumOfPlayers = 0;
				gameState = false;
				if (!blufferGame.isGameFinish()){
					blufferGame.run();
					for(String str : isPlayerChoseAnswer.keySet()){
						isPlayerChoseAnswer.put(str, false);	
					}
				}
				else{
					room.sendMess("", new StringMessage("GAMEMSG "+room.Summary()));
					room.endGame();
				
				}
					
			}
			
		}
		else{
			try {
				Game.getInstance().getCallback(name).sendMessage(new StringMessage("SYSMSG SELECTRESP REJECTED"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}