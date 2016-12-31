#pragma once
#include "stdafx.h"
#include "Player.h"

struct OutLoginJson{
    int id;
    float frameRate;
    Point size;
    std::vector<Point> positions;
    int time2resp;

    OutLoginJson(int id_, float framerate_, Point size_, std::vector<Point> positions_, int time2resp_){
        id = id_;
        frameRate = framerate_;
        size = size_;
        positions = positions_;
        time2resp = time2resp_;
    }

    web::json::value AsJSON() const
    {
        web::json::value result = web::json::value::object();
        result[U("id")] = web::json::value::number(id);
        result[U("frameRate")] = web::json::value::number(frameRate);
        result[U("time2resp")] = web::json::value::number(time2resp);
        result[U("size")] = size.to_JSON();

        std::vector<web::json::value> positionsJson;
        for(int i = 0;i<positions.size(); i++){
            positionsJson.push_back(positions[i].to_JSON());
        }
        result[U("positions")] = web::json::value::array(positionsJson);

        return result;
    }
};
struct InLoginJson{
    string name;
    static InLoginJson FromJSON(web::json::value object)
    {
        InLoginJson result;
        try{
            result.name = object.at(U("login")).as_string();
        }catch(web::json::json_exception e){
            std::cout<<e.what()<<std::endl;
        }
        return result;
    }
};

//oddać kinolom
struct OutMoveJson{
   // int id;
    int notification;
    Player snake;
    std::vector<Player > enemies;
    std::vector<Point> meal;
    std::vector<Point> wall;

    OutMoveJson(/*int id_,*/ int notification_, Player snake_, std::vector<Player > enemies_, std::vector<Point> meal_, std::vector<Point> wall_):snake(snake_){
        //id = id_;
        notification = notification_;
        enemies = enemies_;
        meal = meal_;
        wall = wall_;
    }

    web::json::value AsJSON() const
    {
        web::json::value result = web::json::value::object();
        //result[U("id")] = web::json::value::number(id);
        result[U("notification")] = web::json::value::number(notification);
        result[U("snake")] = snake.AsJSON();

        result[U("meal")] = Point::arrayOfPoints2JSON(meal);

        result[U("wall")] = Point::arrayOfPoints2JSON(wall);
        std::vector<web::json::value> enemiesJson;
        for(int i = 0;i<enemies.size(); i++){
            if(enemies[i].ID()!=snake.ID())
                enemiesJson.push_back(enemies[i].AsJSON());
        }
        result[U("enemies")] = web::json::value::array(enemiesJson);
        result[U("points")] = web::json::value::number(snake.Points());
        return result;
    }
};


//dostane od kinoli
struct InMoveJson{
    int direction;
    bool shoot;
    static InMoveJson FromJSON(web::json::value object)
    {
        InMoveJson result;
        try{
            result.direction = object.at(U("direction")).as_integer();
            result.shoot = object.at(U("laser")).as_bool();
        }catch(web::json::json_exception e){
            std::cout<<e.what()<<std::endl;
        }
        return result;
    }
};

struct OutScores{
    vector<Player> players;
    OutScores(vector<Player> _players):players(_players){
    }

    web::json::value AsJSON() const
    {
        web::json::value result = web::json::value::object();

        std::vector<web::json::value> scoresJson;
        for(int i = 0;i<players.size(); i++){
            web::json::value score = web::json::value::object();
            score[U("name")] = web::json::value::string(players[i].Name());
            score[U("points")] = players[i].Points();
            score[U("deaths")] = players[i].Deaths();
            score[U("hits")] = players[i].Hits();
            scoresJson.push_back(score);
        }
        result[U("scores")] = web::json::value::array(scoresJson);
        return result;
    }
};
