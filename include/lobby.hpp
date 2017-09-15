#pragma once
#include <vector>
#include <map>
#include "player_info.hpp"
class Lobby{

public:
    Lobby();
    virtual ~Lobby();
    int getPlayersNum();
    std::vector<std::string> getPlayerList();
    std::vector<bool> getPlayerReadyness();
    void AddPlayer(std::string adress, PlayerInfo *player);
    void RemovePlayer(std::string adress);
private:
    std::map <std::string, PlayerInfo*> players;

};
