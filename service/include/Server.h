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
    Server():mapSize(20,20), map(this, mapSize) {}
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
    Map map;
private:
#if DEBUG
	bool DEBUG = true;
#else
	bool DEBUG = false;
#endif
	std::string name;
    void handle_get(http_request message);
    //void handle_put(http_request message);
    void handle_post(http_request message);
    //void handle_delete(http_request message);



    http_listener m_listener;
};
