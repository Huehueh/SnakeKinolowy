#include "messagetypes.h"
#include "Server.h"

using namespace std;
using namespace web; 
using namespace utility;
using namespace http;
using namespace web::http::experimental::listener;

Server::Server(utility::string_t url) : settings(), mapSize(settings.size, settings.size), m_listener(url),
    map(this, mapSize), frameRate(settings.frameRate), time2resp(settings.time2resp)
{
    m_listener.support(methods::GET, std::bind(&Server::handle_get, this, std::placeholders::_1));
    //m_listener.support(methods::PUT, std::bind(&Server::handle_put, this, std::placeholders::_1));
    m_listener.support(methods::POST, std::bind(&Server::handle_post, this, std::placeholders::_1));
    m_listener.support(methods::DEL, std::bind(&Server::handle_delete, this, std::placeholders::_1));

    for(int i = 0 ;i < settings.startPlayers; i++)
    {
        Player newOne = Player(randId(), "player", map, 10, &settings);
        int id = newOne.ID();
        players.push_back(newOne);
        std::cout<<"robie wroga na starcie"<<endl;
    }
}
int query_no = 0;

void Server::handle_get(http_request message)
{
    if(message.content_ready().wait() == pplx::completed)
    {
        //decode request
        auto query = uri::split_query(uri::decode(message.relative_uri().query()));

        auto PreProItr = query.find("score");
        if(PreProItr!=query.end())
        {
            score( stoi(PreProItr->second), message);
            return;
        }

        std::cout<<"Message has wrong query"<<std::endl;
        message.reply(400, U("Bad Request, wrong query."));
    }
}

void Server::score(int id, http_request message)
{
    //check if id exist, then answer
    if(playerIdExist(id)){
        message.reply(200, OutScores(players).AsJSON());
    }else{
        message.reply(403, U("Forbidden, you cannot view scores of this game."));
    }
}

void Server::handle_post(http_request message)
{
    if(message.content_ready().wait() == pplx::completed)
    {
        //decode request
        auto query = uri::split_query(uri::decode(message.relative_uri().query()));
        auto PreProItr = query.find("login");
        if(PreProItr!=query.end())
        {
            login( stoi(PreProItr->second), message);
            return;
        }
        PreProItr = query.find("move");
        if(PreProItr!=query.end())
        {
            move( stoi(PreProItr->second), message);
            return;
        }

        std::cout<<"Message has wrong query"<<std::endl;
        message.reply(400, U("Bad Request, wrong query."));
    }
}

void Server::login(int id_new, http_request message){
    InLoginJson json = InLoginJson::FromJSON(message.extract_json().get());
    cout<<json.name<<" tries to login."<<endl;
    Player found;
    bool nameFree = true;
    for(Player player : players){
        if(player.Name()==json.name){
            nameFree = false;
            found = player;
            break;
        }
    }
    Player newOne(-1);
    //jest zajęte i to zajęte ma podane id to relogin
    if(!nameFree && found.ID()==id_new){
        newOne = found;
        cout<<"Was logged, ok."<<std::endl;
    } else{
        //jest wolne
        if(nameFree){
            newOne = Player(randId(), json.name, map, 3, &settings);
            cout<<"Logged and created successfully."<<endl;
        }
    }
    int id = newOne.ID();
    vector<Point> positions = newOne.positions;
    if(nameFree){
        players.push_back(newOne);
    }
    message.reply(200, OutLoginJson(id, frameRate, mapSize, positions, time2resp).AsJSON());
}

void Server::move(int id, http_request message)
{
    InMoveJson info = InMoveJson::FromJSON(message.extract_json().get());

    int notification = 1;
    int tempPlayer = -1;
    for(int i = 0; i < players.size(); i++){
        if(players[i].ID() == id){
            tempPlayer = i;
            break;
        }
    }
    if(tempPlayer>-1){
        if(players.size()>=settings.players2Play){
            if(!gameOver){
                notification = players[tempPlayer].Move(map, info.direction, info.shoot, players);
                if(notification == 4){
                    gameOver = true;
                    std::cout<<"Game Over "<<players[tempPlayer].Name()<<" won."<<std::endl;
                }
            }else{
                notification = 4;
            }
        }else{
            notification = 5;
        }
        vector<Point> meal, wall;
        meal.push_back(map.getMeal());
        for(int i = 0;i<players.size();i++){
            for(int j = 0; j<players[i].meal.size();j++)
                meal.push_back(players[i].meal[j]);
            for(int j = 0; j<players[i].wall.size();j++)
                wall.push_back(players[i].wall[j]);
        }

        OutMoveJson snake(notification, players[tempPlayer], players, meal, wall);
        message.reply(200, snake.AsJSON());
    }else{
        cout<<"Zle id"<<endl;
        message.reply(400, U("Bad Request, wrong ID."));
    }
}

void Server::handle_delete(http_request message)
{
    //decode request
    auto query = uri::split_query(uri::decode(message.relative_uri().query()));

    auto PreProItr = query.find("logout");
    if(PreProItr!=query.end())
    {
        logout( stoi(PreProItr->second), message);
        return;
    }

    PreProItr = query.find("reset");
    if(PreProItr!=query.end())
    {
        reset( stoi(PreProItr->second), message);
        return;
    }

    std::cout<<"Message has wrong query"<<std::endl;
    message.reply(403, U("Bad Request, wrong query."));
}

void Server::logout(int id, http_request message)
{
    if(playerIdExist(id)){
        int remove= -1;
        for(int i = 0; i<players.size(); i++){
            if(players[i].ID() == id){
                remove = i;
                break;
            }
        }
        if(remove!=-1){
            players.erase(players.begin() + remove);
            message.reply(200, U("Player loged out."));
        }
    }
    message.reply(400, U("Player couldn't be found."));
}

void Server::reset(int id, http_request message)
{
    if(playerIdExist(id)){
        players.clear();
        gameOver = false;
        message.reply(200, U("Map cleared."));
    }
    message.reply(403, U("You can't clear the map."));
}

bool Server::playerIdExist(int id)
{
    bool playersOk = false;
    for(int i = 0; i<players.size(); i++){
        if(players[i].ID() == id){
            playersOk = true;
            break;
        }
    }
    return playersOk;
}
int Server::randId(){
    bool taken = false;
    int result = rand();
    do{
        result = rand();
        taken = false;
        for(int i = 0; i<players.size(); i++){
            if(players[i].ID()==result){
                taken=true;
                break;
            }
        }
    }while(taken);
    return result;
}

/*
void Server::handle_put(http_request message)
{
    ucout <<  message.to_string() << endl;
    ucout << "Got new message!" << endl;
}*/


