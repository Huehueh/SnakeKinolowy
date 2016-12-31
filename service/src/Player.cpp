#include "Player.h"


Player::Player(int id_, string name_, Map map, int startLength, Settings* settings_){
    settings = settings_;
    points = settings->basePoints;
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
        place = stayOnMap(map, place);
        if(map.freeSpace(place, dead)){
            positions.push_back(place);
        }else{
            place.x++;
            place.y--;
            place = stayOnMap(map, place);
            if(map.freeSpace(place, dead)){
                positions.push_back(place);
            }else{
                place.y++;
                place.x++;
                place = stayOnMap(map, place);
                if(map.freeSpace(place, dead)){
                    positions.push_back(place);
                }else{
                    place.x--;
                    place.y++;
                    place = stayOnMap(map, place);
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
Player::Player(){

}

Player::Player(int id_){
    id = id_;
}

int Player::ID()const{
    return id;
}
string Player::Name()const{
    return name;
}
int Player::Points()const{
    return points;
}
int Player::Hits()const{
    return hits;
}
int Player::Deaths()const{
    return deaths;
}
int Player::Move(Map &map, int direction, bool shoot, vector<Player> &players){
    int happens=1;
    shot = false;
    //cout<<"umarl "<<dead<<endl;
     
    if(!dead){
	    if(direction>0){  
            Point add = direction2Point(direction);
            Point tempHead = stayOnMap(map, positions[0]+add);
        //czy glowa nie zawraca
            if(positions.size()>1 && tempHead == positions[1]){
                return 0;
            }
        //sprawdz czy glowa zjada cos
            Point last = positions[positions.size()-1];
            if(map.eating(tempHead)){
                happens = 3;
                points += settings->eatPoints;
                positions.push_back(Point(positions[positions.size()-1]));
            }
        //przesun cialko za glowa
            for(int i = positions.size()-1; i>0; i --){
                if(!(positions[i] == positions[i-1])){
                    positions[i] = positions[i-1];
                }
            }
        //sprawdz czy glowa umiera
            map.freeSpace(tempHead, dead);
        //przystaw weza na nowa pozycje
            positions[0] = tempHead;
            //jezeli umarl
            if(dead){
                happens = 2;
                points+=settings->deathPoints;
            //zapisz czas smierci
                std::chrono::time_point<std::chrono::system_clock> date = std::chrono::system_clock::now();
                death_time = std::chrono::system_clock::to_time_t(date);
                deaths++;
            //cofnij weza
                for(int i = 0; i < positions.size()-1; i++){
                    positions[i] = positions[i+1];
                }
            //pamietajac o ostatnim segmencie
                positions[positions.size()-1] = last;
                //positions.erase(--positions.end(), positions.end());
                if(positions.size()>3){
                    positions.pop_back();
                }
                if(positions.size()>3){
                    positions.pop_back();
                }
            }else{
                if(shoot){
                    ShootLaser(players, map);
                }
            }
    	}
    }else{
        happens = 1;
	    //sprawdz czas
        std::chrono::time_point<std::chrono::system_clock> date = std::chrono::system_clock::now();
        double diff = std::difftime(std::chrono::system_clock::to_time_t(date), death_time);
        if(diff>settings->time2resp){
            dead = false;
            happens = 1;
        }
        //std::cout<<diff<<std::endl;
    }
    if(points<settings->minPoints)
        points = settings->minPoints;
    if(points>=settings->winPoints)
        happens = 4;
    return happens;
}
void Player::ShootLaser(vector<Player> &players, Map map){

    if(positions.size()>3){
        vector<Point> laser;
        int dir = point2Direction(positions[0]-positions[1]);
        laser.push_back(stayOnMap(map, positions[0]+direction2Point(dir)));
        laser.push_back(stayOnMap(map, laser[0]+direction2Point(dir)));
        laser.push_back(stayOnMap(map, laser[1]+direction2Point(dir)));

        positions.pop_back();
        for(int i =0;i<players.size();i++){
           if(players[i].playerCut(laser)){
               points += settings->hitPoints;
               hits++;
           }
        }
        shot = true;
        points += settings->shootPoints;
    }
}
bool Player::playerCut(vector<Point> point){
    for(auto it= wall.begin(); it<wall.end();it++){
        for(int l = 0; l<point.size();l++){
            if(point[l] == *it){
                wall.erase(it);
                it--;
                l--;
            }
        }
    }

    if(positions.size()<=3){
        return false;
    }
    vector<int> hits;
    vector<Point> newSnake;
    vector<Point> fallOff;
    for(int i = 0;i<positions.size();i++){
        for(int l = 0; l<point.size();l++){
            if(point[l] == positions[i]){
                hits.push_back(i);
            }
        }
    }

    bool cut = false;
    if(hits.size()>0){
        int i = 0, h = 0;
        if(hits[h]<3){
            while(h<3&&hits[h]<3){
                h++;
            }
            newSnake.push_back(positions[0]);
            newSnake.push_back(positions[1]);
            newSnake.push_back(positions[2]);
            cut = true;
            i=3;
        }
        for(;i<positions.size();i++){
            if(hits[h]<=i){
                h++;
                cut=true;
            } else{
                if(cut){
                    fallOff.push_back(positions[i]);
                }else{
                    newSnake.push_back(positions[i]);
                }
            }
        }
        positions = newSnake;
        float prob = (float)rand()/(float)RAND_MAX;
        if(prob<((float)fallOff.size()-1.0)/4.0){
            for(int i =0;i<fallOff.size();i++){
                wall.push_back(fallOff[i]);
            }
        }else{
            for(int i =0;i<fallOff.size();i++){
                meal.push_back(fallOff[i]);
            }
        }
        points+=settings->damagePoints;
        return true;
    }else{
        return false;
    }

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
    result[U("laser")] = web::json::value::boolean(shot);
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
        if(point.y==1||point.y<-1){
            result = 1;
        }else if(point.y==-1||point.y>1){
            result = 3;
        }
    }else if(point.y==0){
        if(point.x==1||point.x<-1){
            result = 2;
        }else if(point.x==-1||point.x>1){
            result = 4;
        }
    }
    return result;
}
