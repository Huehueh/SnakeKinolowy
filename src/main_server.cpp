#include "messagetypes.hpp"
#include "main_server.hpp"
#include <boost/thread.hpp>

using namespace std;
using namespace boost::asio::ip;

MainServer::MainServer(boost::asio::io_service &io_service, int port): socket_(io_service, udp::endpoint(udp::v4(), port))
{
    //temporary constructor for easy debugging
    lobbies["nie huehuaj"]= Lobby();
    start_receive();
}

MainServer::~MainServer()
{

}

void MainServer::player2Lobby(string adress)
{
    lobbies[adress].AddPlayer(adress, playersWithoutLobby[adress]);
    playersWithoutLobby.erase(adress);
}

//communication stuff
void MainServer::start_receive()
{
    socket_.async_receive_from(
            boost::asio::buffer(recv_buffer_), remote_endpoint_,
            boost::bind(&MainServer::handle_receive, this,
            boost::asio::placeholders::error,
            boost::asio::placeholders::bytes_transferred));
}

void MainServer::handle_receive(const boost::system::error_code& error, std::size_t bytes_transferred)
{
    std::string message(recv_buffer_.begin(), recv_buffer_.end());
    std::cout<<"I received "<<bytes_transferred<<" bytes: "<<message<<std::endl;
    if (!error || error == boost::asio::error::message_size)
    {
        /*boost::shared_ptr<std::string> message(new std::string(std::string("Słodki Kinolku mamy teraz: ")+
                    std::string(make_daytime_string())));
        socket_.async_send_to(boost::asio::buffer(*message), remote_endpoint_,
                    boost::bind(&Server::handle_send, this, message,
                    boost::asio::placeholders::error,
                    boost::asio::placeholders::bytes_transferred));
        */
        if(!started)
            boost::thread magic{sendAfter5, this};
        got++;
        std::cout<<"otrzymano "<<got<<" wiadomości"<<std::endl;
        start_receive();
    }
}
void MainServer::handle_send(boost::shared_ptr<std::string> message,
      const boost::system::error_code& /*error*/,
      std::size_t /*bytes_transferred*/)
{

}

void MainServer::sendAfter5(MainServer *s)
{
    s->started = true;
    //while(s->got<5)

    udp::endpoint player = s->remote_endpoint_;
    auto buffer = kinolowe_messages::ListLobbies(s->lobbies2names());
    s->socket_.send_to(boost::asio::buffer(buffer), player);

    cout<<"wysylam" <<std::string(buffer.begin(), buffer.end())<<endl;
    s->got = 0;
    s->started = false;

    boost::this_thread::sleep_for(boost::chrono::seconds{1});

    auto buffer2 = kinolowe_messages::Go2Room("nie huehuaj", s->lobbies["nie huehuaj"].getPlayerList(), s->lobbies["nie huehuaj"].getPlayerReadyness(), 30);
    s->socket_.send_to(boost::asio::buffer(buffer2), player);
    cout<<"wysylam" <<std::string(buffer2.begin(), buffer2.end())<<endl;
}

/*void MainServer::send_30_times(MainServer *s)
{
    udp::endpoint player = s->remote_endpoint_;
    for(int i = 0; i <30; i++){
        boost::this_thread::sleep_for(boost::chrono::seconds{1});
        s->socket_.send_to(boost::asio::buffer(), player);
    }
}*/

std::vector<string> MainServer::lobbies2names()
{
    vector<string> result;
    for (auto lobby : lobbies){
        result.push_back(lobby.first);
    }
    return result;
}
