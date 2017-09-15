#include "messagetypes.hpp"
#include "settings.hpp"
#include "stdio.h"
#include "string.h"

kinolowe_messages::Header *kinolowe_messages::Header::Header_instance = nullptr;

kinolowe_messages::Header::Header()
{
    message_headers[1] = "Chce zagrac w wonszyka!";
    message_headers[2] = "Masz tu pokoje";
    message_headers[3] = "Wciaz wybieram pokoik";
    message_headers[4] = "Wybieram pokoj";
    message_headers[5] = "Zamknijmy sie w";
    message_headers[6] = "Jam podaje swa gotowosc";
    message_headers[7] = "Czas wonszykowac";
    message_headers[8] = "Zmieniam rozmiar planszy na";
    message_headers[9] = "Wonszykujta";
    message_headers[100] = "Wielgosz wita";
    message_headers[101] = "Wciaz gram w wonszyka";
    message_headers[102] = "Wszystko plynie";
    message_headers[103] = "Steruje mym wonszem";
    message_headers[104] = "No i powonszowane";

}

kinolowe_messages::Header::~Header()
{

}

kinolowe_messages::Header *kinolowe_messages::Header::getInstance()
{
    if(Header_instance == nullptr)
        Header_instance = new Header();
    return Header_instance;
}

void kinolowe_messages::Header::LoadFromFile()
{

}

std::map<unsigned char, std::string> kinolowe_messages::Header::getMessage_headers() const
{
    return message_headers;
}

string kinolowe_messages::make_sendable(string text)
{
    std::string temp;
    for(int i2 = 0; i2 < Settings::getInstance()->nameLength; i2++){
        if(text.length()>i2){
            temp+=text[i2];
        }else{
            temp+=string((unsigned char)0);
        }
    }
    return temp;
}

boost::array<unsigned char, 495> kinolowe_messages::ListLobbies(std::vector<std::string> lobbies)
{
    int number_of_message = 2;
    boost::array<unsigned char, 495> result;
    result.assign(0);

    int pos = 0;
    result[pos] = number_of_message;
    pos+=1;

    memcpy(result.begin()+pos, Header::getInstance()->getMessage_headers()[number_of_message].c_str(),
            Header::getInstance()->getMessage_headers()[number_of_message].size());
    pos+=Header::getInstance()->getMessage_headers()[number_of_message].size();

    for(int i = 0; i < lobbies.size(); i++){
        memcpy(result.begin()+pos, lobbies[i].c_str(),
                lobbies[i].size());
        pos+=Settings::getInstance()->nameLength;
    }
    return result;
}

boost::array<unsigned char, 165> kinolowe_messages::Go2Room(string name, std::vector<string> players, std::vector<bool> players_readiness, int map_size)
{
    int number_of_message = 5;
    boost::array<unsigned char, 165> result;
    result.assign(0);

    int pos = 0;
    result[pos] = number_of_message;
    pos+=1;

    memcpy(result.begin()+pos, Header::getInstance()->getMessage_headers()[number_of_message].c_str(),
            Header::getInstance()->getMessage_headers()[number_of_message].size());
    pos+=Header::getInstance()->getMessage_headers()[number_of_message].size();

    memcpy(result.begin()+pos, name.c_str(),
            name.size());
    pos+=Settings::getInstance()->nameLength;

    for(int i = 0; i < players.size(); i++){
        memcpy(result.begin()+pos, players[i].c_str(),
                players[i].size());
        pos+=Settings::getInstance()->nameLength;
    }

    unsigned char readiness = 0;
    for(int i = 0; i < players_readiness.size(); i++){
        if(players_readiness[i])
            readiness += 2^i;
    }
    result[pos] = readiness;
    pos+=1;

    auto mapsize = intToBytes(map_size);
    memcpy(result.begin()+pos, &(*mapsize.begin()), mapsize.size());
    pos+=mapsize.size();

    return result;
}

vector<unsigned char> kinolowe_messages::intToBytes(int paramInt)
{
     vector<unsigned char> arrayOfByte(4);
     for (int i = 0; i < 4; i++)
         arrayOfByte[3 - i] = (paramInt >> (i * 8));
     return arrayOfByte;
}
