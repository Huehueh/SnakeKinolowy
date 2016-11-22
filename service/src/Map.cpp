#include "Map.h"

#include "Server.h"

Map::Map(Server* server_, Point size_){
    server = server_;
    size = size_;
    makeMeal();
}
Point Map::getMeal(){
    return meal;
}

Point Map::Size(){
    return size;
}

bool Map::eating(Point here){
    bool eat;
    if(meal == here){
        eat = true;
        makeMeal();
    }else{
        eat = false;
    }
    return eat;
}

void Map::makeMeal(){
    bool freeMeal = false;
    Point newMeal=Point(0,0);
    do{
        newMeal = randPlace();
        bool dead;
        freeMeal = freeSpace(newMeal, dead);
        for(Player player : server->players){
            for(Point pos : player.meal){
                if(pos==newMeal){
                    freeMeal=false;
                }
            }
        }
    }while(!freeMeal);

    meal = newMeal;

}
bool Map::freeSpace(Point here, bool& dead){
    bool free = true;
    dead = false;
    for(Player player : server->players){
        for(Point pos : player.positions){
            //cout<<"       "<<pos.x<<" "<<pos.y<<endl;
            if(pos==here){
                free=false;
                dead = true;
            }
        }
        for(Point pos : player.wall){
            //cout<<"       "<<pos.x<<" "<<pos.y<<endl;
            if(pos==here){
                free=false;
                dead = true;
            }
        }
    }
    return free&&!(meal==here);
}
Point Map::randPlace(){
    float randx = (float)rand()/(float)RAND_MAX, randy = (float)rand()/(float)RAND_MAX;
    return Point(size.x*randx, size.y*randy);
}
