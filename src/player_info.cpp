#include "player_info.hpp"

PlayerInfo::PlayerInfo(std::string _name)
{
    name = _name;
}

PlayerInfo::~PlayerInfo()
{

}

std::string PlayerInfo::getName() const
{
    return name;
}

bool PlayerInfo::getReady() const
{
    return ready;
}

void PlayerInfo::setReady(bool value)
{
    ready = value;
}
