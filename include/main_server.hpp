#pragma once
#include <iostream>
#include <boost/bind.hpp>
#include <boost/asio.hpp>
#include <boost/array.hpp>
#include <map>
#include <list>
#include "player_info.hpp"
#include "lobby.hpp"
#include "settings.hpp"

class MainServer
{
public:
    MainServer(boost::asio::io_service& io_service, int port);
    virtual ~MainServer();
private:
    void start_receive();
    void handle_receive(const boost::system::error_code& error, std::size_t bytes_transferred);
    void handle_send(boost::shared_ptr<std::string> message,
                     const boost::system::error_code& /*error*/,
                     std::size_t /*bytes_transferred*/);

    boost::asio::ip::udp::socket socket_;
    boost::asio::ip::udp::endpoint remote_endpoint_;
    boost::array<char, 40> recv_buffer_;
    //static void send_30_times(MainServer *s);
    bool started = false;
    int got = 0;
    static void sendAfter5(MainServer *s);
    std::vector<std::string> lobbies2names();
    std::map<std::string, Lobby> lobbies;
    //adress and playerinfo *
    std::map<std::string, PlayerInfo*> playersWithoutLobby;
    void player2Lobby(std::string adress);
};
