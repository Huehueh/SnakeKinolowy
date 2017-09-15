#include <iostream>
#include <main_server.hpp>
#include <boost/asio.hpp>
using namespace std;

#ifdef _WIN32
int wmain(int argc, wchar_t *argv[])
#else
int main(int argc, char *argv[])
#endif
{
    cout<<"Server start"<<endl;
    try{
        boost::asio::io_service io_service;
        MainServer server(io_service, 1256);
        io_service.run();
    }catch (std::exception& e)
    {
        std::cerr << e.what() << std::endl;
    }
    cout<<"Server stop"<<endl;
}
