#include "Settings.h"
#include "fstream"
#include "iostream"
Settings::Settings(){
    LoadFile();
}

void Settings::Load(){
    minPoints = 0;
    winPoints = 30;
    basePoints = 10;
    eatPoints = 2;
    shootPoints = -1;
    damagePoints = -2;
    hitPoints = 2;
    deathPoints = -3;

    players2Play = 2;
    startPlayers = 1;
}
void Settings::LoadFile(){

    std::ifstream info("ini.txt");
    if(info.is_open()){
        std::string line;

        info >> line;
        info >> line;
        minPoints = std::stoi(line);

        info >> line;
        info >> line;
        winPoints = std::stoi(line);

        info >> line;
        info >> line;
        basePoints = std::stoi(line);

        info >> line;
        info >> line;
        eatPoints = std::stoi(line);

        info >> line;
        info >> line;
        shootPoints = std::stoi(line);

        info >> line;
        info >> line;
        damagePoints = std::stoi(line);

        info >> line;
        info >> line;
        hitPoints = std::stoi(line);

        info >> line;
        info >> line;
        deathPoints = std::stoi(line);

        info >> line;
        info >> line;
        players2Play = std::stoi(line);

        info >> line;
        info >> line;
        startPlayers = std::stoi(line);

        std::cout<<"minimum "<<minPoints<<" win "<<winPoints<<" start "<<basePoints<<" eating "
              <<eatPoints<<" shooting "<<shootPoints<<" damaged "<<damagePoints<<" hit "
              <<hitPoints<<" death "<<deathPoints<<" players to play "<<players2Play<<" start players "<<startPlayers<<std::endl;
    }else{
        Load();
    }
}
