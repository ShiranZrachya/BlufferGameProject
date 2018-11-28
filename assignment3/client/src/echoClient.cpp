#include <stdlib.h>
#include <boost/locale.hpp>
#include "../include/connectionHandler.h"
#include <boost/thread.hpp>
using namespace boost;

void writeTh(ConnectionHandler *connectionHandler);
void readTh(ConnectionHandler *connectionHandler);


int main (int argc, char *argv[]) {

    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler ch(host, port);
    if (!ch.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    thread sender(writeTh,&ch);
    thread reader(readTh,&ch);
    reader.join();
    sender.interrupt();
    return 0;
}

void writeTh(ConnectionHandler *connectionHandler){
	bool Writing=true;
    while (Writing) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
		int len=line.length();
        if (!(*connectionHandler).sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            Writing=false;

        }
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;


    }

}


void readTh(ConnectionHandler *connectionHandler){
	bool Reading=true;
	while (Reading) {
        std::string answer;
        int len;
        if (!(*connectionHandler).getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            Reading=false;
            break;
        }
        
		len=answer.length();
		answer.resize(len-1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "SYSMSG QUIT ACCEPTED") {
        	std::cout << "Exiting...\n" << std::endl;
        	Reading=false;
            break;
        }
    }

}
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/


