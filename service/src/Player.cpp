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
        Point add = direction2Point(direction);
        Point tempHead = stayOnMap(map, positions[0]+add);

        if(positions.size()>1 && tempHead == positions[1]){
            return 0;
        }

        Point last = positions[positions.size()-1];
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
            for(int i = 0; i < positions.size()-1; i++){
                positions[i] = positions[i+1];
            }
            positions[positions.size()-1] = last;
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
web::json::value Player::AsJSON() const
{
    web::json::value result = web::json::value::object();
    result[U("x")] = web::json::value::number(positions[0].x);
    result[U("y")] = web::json::value::number(positions[0].y);

    std::vector<web::json::value> segments;
    for(int i = 1;i<positions.size(); i++){
        Point diff = positions[i]-positions[i-1];
        int dir = point2Direction(diff);
        segments.push_back(web::json::value::number(dir));
    }
    result[U("seg")] = web::json::value::array(segments);

    return result;
}
Point Player::direction2Point(int dir){
    Point add;
    switch(dir){
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
    return add;
}

int Player::point2Direction(Point point){
    int result = -1;
    if(point.x==0){
        if(point.y==1){
            result = 1;
        }else if(point.y==-1){
            result = 3;
        }
    }else if(point.y==0){
        if(point.x==1){
            result = 2;
        }else if(point.x==-1){
            result = 4;
        }
    }
    return result;
}
