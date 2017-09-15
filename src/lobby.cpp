#include "lobby.hpp"
Lobby::Lobby()
{

}

Lobby::~Lobby()
{

}

int Lobby::getPlayersNum()
{
    return players.size();
}

std::vector<std::string> Lobby::getPlayerList()
{
    std::vector<std::string> result;
    for(auto player : players){
        result.push_back(player.second->getName());
    }
    return result;
}

std::vector<bool> Lobby::getPlayerReadyness()
{
    std::vector<bool> result;
    for(auto player : players){
        result.push_back(player.second->getReady());
    }
    return result;
}

void Lobby::AddPlayer(std::string adress, PlayerInfo *player)
{
    players[adress] = player;
}

void Lobby::RemovePlayer(std::string adress)
{
    players.erase(adress);
}

