#pragma once
#include "SnakeLogic/player.hpp"
#include <iostream>
#include <map>
#include <boost/array.hpp>

namespace kinolowe_messages
{
    class Header{
    public:
        static Header *getInstance();
        void LoadFromFile();
        std::map<unsigned char, std::string> getMessage_headers() const;
    private:
        std::map<unsigned char, std::string> message_headers;
        Header();
        virtual ~Header();
        static Header *Header_instance;
    };

    std::string make_sendable(std::string text);


    boost::array<unsigned char, 495> ListLobbies(std::vector<std::string> lobbies);
    boost::array<unsigned char, 165> Go2Room(string name, std::vector<string> players, std::vector<bool> players_readiness, int map_size);
    std::vector<unsigned char> intToBytes(int paramInt);
}
