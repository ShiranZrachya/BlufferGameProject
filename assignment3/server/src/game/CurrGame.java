package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import json.Json;

public class CurrGame implements TBGP {
	private ArrayList<Question> questionsData; 
	private ArrayList <Question> questionsInGame; 
	private boolean isGameFinish;
	private Room currRoom;
	private Bluffer currentGame;
	
	public CurrGame(Room room) {
		questionsData=new ArrayList<Question>();
		questionsInGame=new ArrayList<Question>();
        this.currRoom = room;
       
		 	Gson gson =new GsonBuilder().create();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader ("/users/studs/bsc/2016/sparzada/Desktop/example.json"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Json json = gson.fromJson(reader, Json.class);
			
			for(int i=0;i<json.getQuestions().length;i++){
				
				questionsData.add(new Question(json.getQuestions()[i].getQuestionText(),json.getQuestions()[i].getRealAnswer()));
			}
			if(json.getQuestions().length<3){
	            currRoom.endGame();
	            String name= "";
	            currRoom.sendMess(name, new StringMessage("SYSMSG THRERE IS NOT ENOUGH QUESTIONS"));
			}
			// getQuestionsForRoom
			if(questionsData.size()>=3){
				int q1 = ((int)((questionsData.size())*Math.random()));
				int q2 = ((int)((questionsData.size())*Math.random()));
				while(q1==q2){
					q2=((int)((questionsData.size())*Math.random()));
					
				}
				int q3=((int)((questionsData.size())*Math.random()));
				while(q1==q3 || q2==q3){
					q3=((int)((questionsData.size())*Math.random()));
					
				}
				questionsInGame.add(questionsData.get(q1));
				questionsInGame.add(questionsData.get(q2));
				questionsInGame.add(questionsData.get(q3));	
			}
       
         isGameFinish = false;
        currRoom.sendMess("",new StringMessage("GAMEMSG STARTING GAME"));
         run();
}
	 
	 public void run(){
		 Question q= questionsInGame.get(questionsInGame.size()-1);
         currentGame= new Bluffer(this,currRoom,q);
         questionsInGame.remove(questionsInGame.size()-1);
         currRoom.sendMess("",new StringMessage("ASKTXT "+currentGame.getQuestion()));
         if(questionsInGame.size()==0){
                 this.isGameFinish = true;
         }
	 }
	 
	 public Bluffer getCurrentGame() {
         return currentGame;
	 }



	 public boolean isGameFinish() {
         return isGameFinish;
	 }

	
 }


	
