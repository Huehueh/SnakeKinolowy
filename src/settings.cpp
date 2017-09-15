#include "settings.hpp"
#include "fstream"
#include "iostream"


Settings *Settings::loadedSetting = nullptr;

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

    nameLength = 16; //jeszcze nie ma w pliku
    maxPlayersNum = 8; //jeszcze nie ma w pliku

    players2Play = 2;
    size = 20;
    frameRate = 3;
    time2resp = 10;

    startPlayers = 1;
}
void Settings::LoadFile(){

    std::ifstream info("ini.txt");
    if(info.is_open()){
        std::string line;
        info >> line;
        info >> line;
        if(line=="0.0.1")
        {
            info >> line;
            info >> line;
            minPoints = std::stoi(line);
            std::cout<<" minimumPoints "<<minPoints<<std::endl;

            info >> line;
            info >> line;
            winPoints = std::stoi(line);
            std::cout<<" winPoints "<<winPoints<<std::endl;

            info >> line;
            info >> line;
            basePoints = std::stoi(line);
            std::cout<<" basePoints "<<basePoints <<std::endl;

            info >> line;
            info >> line;
            eatPoints = std::stoi(line);
            std::cout<<" eatingPoints "<<eatPoints<<std::endl;

            info >> line;
            info >> line;
            shootPoints = std::stoi(line);
            std::cout<<" shootingPoints "<<shootPoints <<std::endl;

            info >> line;
            info >> line;
            damagePoints = std::stoi(line);
            std::cout<<" damagedPoints "<<damagePoints <<std::endl;

            info >> line;
            info >> line;
            hitPoints = std::stoi(line);
            std::cout<<" hitPoints "<<hitPoints <<std::endl;

            info >> line;
            info >> line;
            deathPoints = std::stoi(line);
            std::cout<<" deathPoints "<<deathPoints <<std::endl;

            info >> line;
            info >> line;
            players2Play = std::stoi(line);
            std::cout<<" players to play "<<players2Play<<std::endl;

            info >> line;
            info >> line;
            size = std::stoi(line);
            std::cout<<" size "<<size <<std::endl;

            info >> line;
            info >> line;
            frameRate = std::stof(line);
            std::cout<<" frameRate "<<frameRate <<std::endl;

            info >> line;
            info >> line;
            time2resp = std::stoi(line);
            std::cout<<" time2resp "<<time2resp<<std::endl;

            info >> line;
            info >> line;
            startPlayers = std::stoi(line);
            std::cout<<" start players "<<startPlayers<<std::endl;
        }else{
            std::cout<<"Wrong file type!"<<std::endl;
            Load();
        }
    }else{
        std::cout<<"No ini file!"<<std::endl;
        Load();
    }
}

Settings *Settings::getInstance()
{
    if(loadedSetting == nullptr)
        loadedSetting = new Settings();
    return loadedSetting;
}
