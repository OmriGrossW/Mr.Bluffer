#include <stdlib.h>
#include <boost/locale.hpp>
#include <boost/asio.hpp>
#include <boost/thread.hpp>
#include <iostream>
#include "../playerClient/connectionHandler.h"

void outputStream(ConnectionHandler *connectionHandler){

	while (1) {
		const short bufsize = 1024;
		char buf[bufsize];
		std::cin.getline(buf, bufsize);
		std::string line(buf);
		if (!connectionHandler->sendLine(line)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}
	}
}

void inputStream(ConnectionHandler *connectionHandler){

	while (1) {
		std::string ans;
		if (!connectionHandler->getLine(ans)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}
		int len = ans.length();
		ans.resize(len-1);
		std::cout <<"Server reply: "<< ans << std::endl;
	}
}




int main (int argc, char *argv[]) {

	std::string host = argv[1];
	short port = atoi(argv[2]);
	ConnectionHandler *connectionHandler= new ConnectionHandler(host, port);
	if (!connectionHandler->connect()) {
		std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
		return 1;
	}

	boost::thread outputThread(outputStream, connectionHandler);
	boost::thread inputThread(inputStream, connectionHandler);
	outputThread.join();
	inputThread.join();
	delete connectionHandler;
	//return 0;
}
