#pragma once
#include "stdafx.h"
#include "Point.h"
using namespace std;
class Server;

class Map{
    Point size;
    Point meal;
    void makeMeal();
public:
    Map(Server* server_,  Point size_);

    Point getMeal();
    Point Size();
    bool eating(Point here);
    bool freeSpace(Point here, bool& dead);
    Point randPlace();
    Server* server;
};
