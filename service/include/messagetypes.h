#pragma once
#include "stdafx.h"



struct Point{
    Point(){
        x=0;
        y=0;
    }

    Point(int x_, int y_):x(x_), y(y_){

    }
    int x;
    int y;

    Point operator +(Point a){
        return Point(a.x+x, a.y+y);
    }
    bool operator ==(Point a){
        return a.x==x && a.y==y;
    }
    Point operator /(int a){
        return Point(x/a, y/a);
    }
    web::json::value to_JSON() const
    {
        web::json::value Json = web::json::value::object();
        Json[U("x")] = web::json::value::number(x);
        Json[U("y")] = web::json::value::number(y);
        return Json;
    }
};

struct LoginJson{
    int id;
    float frameRate;
    Point size;
    std::vector<Point> positions;
    float time2resp;

    LoginJson(int id_, float framerate_, Point size_, std::vector<Point> positions_, float time2resp_){
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
    Point newPos;
    int notification;
    int length;
    Point meal;

    std::vector<Point> enemiesPositions;

    SnakeJson(int id_, Point newPos_, int notification_, int length_, Point meal_, std::vector<Point> enemiesPositions_){
        id = id_;
        newPos = newPos_;
        notification = notification_;
        length = length_;
        meal = meal_;
        enemiesPositions = enemiesPositions_;
    }

    web::json::value AsJSON() const
    {
        web::json::value result = web::json::value::object();
        result[U("id")] = web::json::value::number(id);
        result[U("notification")] = web::json::value::number(notification);
        result[U("length")] = web::json::value::number(length);
        result[U("newPos")] = newPos.to_JSON();
        result[U("meal")] = meal.to_JSON();

        std::vector<web::json::value> positionsJson;
        for(int i = 0;i<enemiesPositions.size(); i++){
            positionsJson.push_back(enemiesPositions[i].to_JSON());
        }
        result[U("enemiesPositions")] = web::json::value::array(positionsJson);
        return result;
    }
};


//dostane od kinoli
struct GetSnakeJson{
    int id;
    int direction;

    static GetSnakeJson FromJSON(web::json::value object)
    {
        GetSnakeJson result;
        try{
            result.id = object.at(U("id")).as_integer();
            result.direction = object.at(U("direction")).as_integer();
        }catch(web::json::json_exception e){
            std::cout<<e.what()<<std::endl;
        }
        return result;
    }
};
