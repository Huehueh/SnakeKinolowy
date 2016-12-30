#pragma once
#include "stdafx.h"
#include "Point.h"
#include "Map.h"
#include "Settings.h"
using namespace std;

class Player{
    int id;
    string name;
    int points;
    int hits;
    int deaths;

    bool dead = false;
    double time2Resp = 10;
    std::time_t death_time;
    static Point stayOnMap(Map map, Point there);
    static Point direction2Point(int dir);
    static int point2Direction(Point point);
    void ShootLaser(vector<Player> &players, Map map);
    bool playerCut(vector<Point> point);
public:
    Player(int id_, string name_, Map map, int startLength, Settings* settings_);
    Player();
    Player(int id_);

    int ID() const;
    string Name() const;
    int Points() const;
    int Hits() const;
    int Deaths() const;

    vector<Point> positions;
    vector<Point> wall;
    vector<Point> meal;

    bool shot=false;
    int Move(Map& map, int direction, bool shoot, vector<Player> &players);
    web::json::value AsJSON() const;
    Settings* settings;
};
