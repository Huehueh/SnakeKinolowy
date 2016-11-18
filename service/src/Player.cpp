#include "Player.h"


Player::Player(int id_, string name_, Map map){
    id = id_;
    name = name_;
    bool foundPlace = false;
    Point place;
    while(!foundPlace){
        place = map.randPlace();
        foundPlace = map.freeSpace(place, dead);
    }
    positions.push_back(place);
    for(int i = 1; i<startLength;i++){
        place.x--;
        if(map.freeSpace(place, dead)){
            positions.push_back(place);
        }else{
            place.x++;
            place.y--;
            if(map.freeSpace(place, dead)){
                positions.push_back(place);
            }else{
                place.y++;
                place.x++;
                if(map.freeSpace(place, dead)){
                    positions.push_back(place);
                }else{
                    place.x--;
                    place.y++;
                    if(map.freeSpace(place, dead)){
                        positions.push_back(place);
                    }else{
                        break;
                    }
                }
            }
        }
    }
    dead = false;
}

string Player::Name(){
    return name;
}

int Player::Move(Map &map, int direction){
    int happens=0;
    //cout<<"umarl "<<dead<<endl;
    if(direction>0&&!dead){
        Point add;
        switch(direction){
        case 1:
            add.y++;
            break;
        case 2:
            add.x++;
            break;
        case 3:
            add.y--;
            break;
        case 4:
            add.x--;
            break;
        default:
            break;
        }
        Point tempHead = stayOnMap(map, positions[0]+add);
        if(map.eating(tempHead)){
            happens = 1;
            positions.push_back(Point(positions[positions.size()-1]));
        }
        for(int i = positions.size()-1; i>0; i --){
            if(!(positions[i] == positions[i-1])){
                positions[i] = positions[i-1];
            }
        }
        map.freeSpace(tempHead, dead);

        positions[0] = tempHead;
        if(dead){
            happens = 2;
            std::chrono::time_point<std::chrono::system_clock> date = std::chrono::system_clock::now();
            death_time = std::chrono::system_clock::to_time_t(date);
            deathCount++;
        }
    }
    if(dead){
        happens = 2;
        std::chrono::time_point<std::chrono::system_clock> date = std::chrono::system_clock::now();
        double diff = std::difftime(std::chrono::system_clock::to_time_t(date), death_time);
        if(diff>time2Resp){
            dead = false;
            happens = 0;

        }
        std::cout<<diff<<std::endl;
    }
    return happens;
}
Point Player::stayOnMap(Map map, Point there){
    there.x = there.x%map.Size().x;
    if(there.x<0){
        there.x+=map.Size().x;
    }
    there.y = there.y%map.Size().y;
    if(there.y<0){
        there.y+=map.Size().y;
    }
    return there;
}
