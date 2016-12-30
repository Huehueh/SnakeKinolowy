#pragma once
//points
//  bazowo jest 10
//  minimum 10
//  zwyciestwo 30
// za co daje punkty
//  zjedzenie 2
//  trafienie 1
// za co odejmuje punkty
//  smierc -3
//  strzal -1
//  oberwanie -2
class Settings{
public:
    Settings();
    int minPoints;
    int winPoints;
    int basePoints;
    int eatPoints;
    int shootPoints;
    int damagePoints;
    int hitPoints;
    int deathPoints;

    int players2Play;
    int startPlayers;

    void Load();
    void LoadFile();
};
