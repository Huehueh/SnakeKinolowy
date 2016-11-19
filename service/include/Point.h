#pragma once

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
    Point operator -(Point a) const{
        return Point(x-a.x, y-a.y);
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
    static web::json::value arrayOfPoints2JSON(std::vector<Point> points){
        std::vector<web::json::value> pointsJson;
        for(int i = 0;i<points.size(); i++){
            pointsJson.push_back(points[i].to_JSON());
        }
        return web::json::value::array(pointsJson);
    }
};
