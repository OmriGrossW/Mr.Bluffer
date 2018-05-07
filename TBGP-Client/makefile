CFLAGS:=-c -Wall -Weffc++ -g
LDFLAGS:=-lboost_system -lboost_locale -lboost_thread

all: bin/encoder.o PlayerClient
	g++ -o bin/playerClient bin/encoder.o bin/connectionHandler.o bin/playerClient.o $(LDFLAGS) 

PlayerClient: bin/connectionHandler.o bin/playerClient.o
	
bin/connectionHandler.o: playerClient/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o playerClient/connectionHandler.cpp

bin/playerClient.o: playerClient/playerClient.cpp
	g++ $(CFLAGS) -o bin/playerClient.o playerClient/playerClient.cpp
	
bin/encoder.o: encoder/encoder.cpp
	g++ $(CFLAGS) -o bin/encoder.o encoder/encoder.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
