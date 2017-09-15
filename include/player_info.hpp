#pragma once
#include <iostream>

class PlayerInfo{
public:
    PlayerInfo(std::string _name);
    virtual ~PlayerInfo();
    std::string getName() const;

    bool getReady() const;
    void setReady(bool value);

private:
    std::string name;
    bool ready;
};
