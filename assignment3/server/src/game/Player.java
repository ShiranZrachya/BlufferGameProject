package game;



public class Player {
        private String name;
        private String room;
        private boolean isPlaying;
        private ProtocolCallback<StringMessage> callback;

        public Player(String name , ProtocolCallback<StringMessage> callback){
                this.name = name;
                this.callback = callback;
                this.room = null;
                this.isPlaying = false;
        }
         
        public String getName() {
                return name;
        }
            
        public String getRoomName() {
                return name;
        }

        public boolean isPlaying() {
               return isPlaying;
        }
         
        public void setRoom(String room) {
                this.room = room;
           
        }

		public String getRoom() {
			return room;
		}

		public ProtocolCallback<StringMessage> getCallback() {
			return callback;
		}

		public void leaveCurrRoom(){
			this.room = null;
			isPlaying = false;
		}
		public void setIsPlaying (boolean ans){
			this.isPlaying = ans;
		}
		
		
		
		
		
}

