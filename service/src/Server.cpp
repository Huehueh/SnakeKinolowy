#include "messagetypes.h"
#include "Server.h"

using namespace std;
using namespace web; 
using namespace utility;
using namespace http;
using namespace web::http::experimental::listener;

Server::Server(utility::string_t url) : mapSize(20,20), m_listener(url), map(this, mapSize)
{
    m_listener.support(methods::GET, std::bind(&Server::handle_get, this, std::placeholders::_1));
    //m_listener.support(methods::PUT, std::bind(&Server::handle_put, this, std::placeholders::_1));
    m_listener.support(methods::POST, std::bind(&Server::handle_post, this, std::placeholders::_1));
    //m_listener.support(methods::DEL, std::bind(&Server::handle_delete, this, std::placeholders::_1));

}
int query_no = 0;

void Server::handle_get(http_request message)
{
    if(message.content_ready().wait() == pplx::completed)
    {
        //decode request
        auto query = uri::split_query(uri::decode(message.relative_uri().query()));
        auto PreProItr = query.find("login");
        std::cout<<message.relative_uri().query()<<std::endl;
        \
        //check if request has id
        if(PreProItr!=query.end())
        {
            string name = PreProItr->second;
            cout<<name<<endl;

            bool nameOk = true;
            for(Player player : players){
                if(player.Name()==name){
                    nameOk = false;
                    break;
                }
            }
            Player newOne = Player(players.size(), name, map);
            int id = newOne.id;
            if(nameOk){
                players.push_back(newOne);
            }else{
                id =-1;
            }
            LoginJson login(id, 3, mapSize, newOne.positions, 10);
            message.reply(200, login.AsJSON());

        }else{
            std::cout<<"Message has no id"<<std::endl;
            message.reply(403, U("Bad Request, wrong ID."));
        }
    }
}

void Server::handle_post(http_request message)
{
    if(message.content_ready().wait() == pplx::completed)
    {
        GetSnakeJson info = GetSnakeJson::FromJSON(message.extract_json().get());
        int notification = 0;
        int tempPlayer = -1;
        for(int i = 0; i < players.size(); i++){
            if(players[i].id == info.id){
                tempPlayer = i;
                break;
            }
        }
        if(tempPlayer>-1){
            notification = players[tempPlayer].Move(map, info.direction, info.shoot, players);

            vector<Point> meal, wall;
            meal.push_back(map.getMeal());

            SnakeJson snake(players[tempPlayer].id, notification, players[tempPlayer], players, meal, wall);
            message.reply(200, snake.AsJSON());
        }else{
            cout<<"Zle id"<<endl;
            message.reply(403, U("Bad Request, wrong ID."));
        }
    }
}
/*
void Server::handle_delete(http_request message)
{
    ucout <<  message.to_string() << endl;
    ucout << "Got new message!" << endl;
}

void Server::handle_put(http_request message)
{
    ucout <<  message.to_string() << endl;
    ucout << "Got new message!" << endl;
}*/
bool checkPlayers(string s){
return true;
}
