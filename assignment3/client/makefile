CFLAGS:=-c -Wall -Weffc++ -g 
LDFLAGS:=-lboost_system -lboost_locale -lboost_thread

all: client

client: bin/echoClient.o bin/connectionHandler.o 
	g++ bin/echoClient.o bin/connectionHandler.o -o bin/client -L/usr/lib -lboost_system -lboost_locale -lboost_thread
	
bin/echoClient.o: src/echoClient.cpp
	g++ -c -Wall -Weffc++ -g  -lboost_system -lboost_locale -lboost_thread -Linclude src/echoClient.cpp -o bin/echoClient.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ -c -Wall -Weffc++ -g  -lboost_system -lboost_locale -lboost_thread -Linclude src/connectionHandler.cpp -o bin/connectionHandler.o
	
clean:
	rm -f bin/*
