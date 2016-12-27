#pragma once
#include "stdafx.h"
#include "Process.h"
#include "Player.h"

using namespace web;
using namespace http;
using namespace utility;
using namespace http::experimental::listener;
/**
 * @brief Server that performs preprocessing on POST request.
 */
class Server
{
public:
    Server():mapSize(20,20), map(this, mapSize), frameRate(3), time2resp(10) {}
/**
 * @brief Constructor - makes server with given URL
 * @param url
 */
    Server(utility::string_t url);
    virtual ~Server(){
    }

    pplx::task<void> open() { return m_listener.open(); }
    pplx::task<void> close() { return m_listener.close(); }

    std::vector<Player> players;
    Point mapSize;
    float frameRate;
    int time2resp;
    Map map;
private:
#if DEBUG
	bool DEBUG = true;
#else
	bool DEBUG = false;
#endif
    void handle_get(http_request message);
    void score(int id, http_request message);

    //void handle_put(http_request message);

    void handle_post(http_request message);
    void login(int id, http_request message);
    void move(int id, http_request message);


    void handle_delete(http_request message);
    void logout(int id, http_request message);
    void reset(int id, http_request message);

    bool playerIdExist(int id);
    http_listener m_listener;
    int randId();
};
