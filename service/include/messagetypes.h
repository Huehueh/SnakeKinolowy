#pragma once
#include "stdafx.h"
#include "Player.h"

struct LoginJson{
    int id;
    float frameRate;
    Point size;
    std::vector<Point> positions;
    int time2resp;

    LoginJson(int id_, float framerate_, Point size_, std::vector<Point> positions_, int time2resp_){
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
//oddać kinolom
struct SnakeJson{
    int id;
    int notification;
    Player snake;
    std::vector<Player > enemies;
    std::vector<Point> meal;
    std::vector<Point> wall;


    SnakeJson(int id_, int notification_, Player snake_, std::vector<Player > enemies_, std::vector<Point> meal_, std::vector<Point> wall_):snake(snake_){
        id = id_;
        notification = notification_;
        enemies = enemies_;
        meal = meal_;
        wall = wall_;
    }

    web::json::value AsJSON() const
    {
        web::json::value result = web::json::value::object();
        result[U("id")] = web::json::value::number(id);
        result[U("notification")] = web::json::value::number(notification);
        result[U("snake")] = snake.AsJSON();

        result[U("meal")] = Point::arrayOfPoints2JSON(meal);

        result[U("wall")] = Point::arrayOfPoints2JSON(wall);
        std::vector<web::json::value> enemiesJson;
        for(int i = 0;i<enemies.size(); i++){
            if(enemies[i].id!=snake.id)
                enemiesJson.push_back(enemies[i].AsJSON());
        }
        result[U("enemies")] = web::json::value::array(enemiesJson);
        return result;
    }
};


//dostane od kinoli
struct GetSnakeJson{
    int id;
    int direction;
    bool shoot;
    static GetSnakeJson FromJSON(web::json::value object)
    {
        GetSnakeJson result;
        try{
            result.id = object.at(U("id")).as_integer();
            result.direction = object.at(U("direction")).as_integer();
            result.shoot = object.at(U("laser")).as_bool();
        }catch(web::json::json_exception e){
            std::cout<<e.what()<<std::endl;
        }
        return result;
    }
};
