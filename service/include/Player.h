#pragma once
#include "stdafx.h"
#include "Point.h"
#include "Map.h"
using namespace std;

class Player{
    string name;
    int id;
    int points;

    bool dead = false;
    double time2Resp = 10;
    std::time_t death_time;
    int deathCount;
    static Point stayOnMap(Map map, Point there);
    static Point direction2Point(int dir);
    static int point2Direction(Point point);
    void ShootLaser(vector<Player> &players, Map map);
    void playerCut(vector<Point> point);
public:
    Player(int id_, string name_, Map map, int startLength = 3);
    Player();
    int ID() const;
    string Name() const;
    int Points() const;

    vector<Point> positions;
    vector<Point> wall;
    vector<Point> meal;

    bool shot=false;
    int Move(Map& map, int direction, bool shoot, vector<Player> &players);
    web::json::value AsJSON() const;

};
