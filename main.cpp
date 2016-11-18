#include "Server.h"

using namespace std;
using namespace web;
using namespace http;
using namespace utility;
using namespace http::experimental::listener;

std::unique_ptr<Server> g_httpPreproces;
/**
 * @brief on initialize - makes new server open
 * @param address - address of the server
 */
void on_initialize(const string_t& address)
{
    // Build our listener's URI from the configured address and the hard-coded path "preprocesing"

    uri_builder uri(address);
    uri.append_path(U("wonsz"));

    auto addr = uri.to_uri().to_string();
    g_httpPreproces = std::unique_ptr<Server>(new Server(addr));
	// And then make it wait for requests
    g_httpPreproces->open().wait();

    ucout << utility::string_t(U("Listening for requests at: ")) << addr << std::endl;

    return;
}
/**
 * @brief on_shutdown - closes server
 */
void on_shutdown()
{
    ucout <<"Service shutted down"<<std::endl;
    g_httpPreproces->close().wait();
    return;
}

#ifdef _WIN32
int wmain(int argc, wchar_t *argv[])
#else
int main(int argc, char *argv[])
#endif
{
    srand( time( NULL ) );
    utility::string_t address;
	if(argc == 2)
    {
        address = "http://";
		address.append(string(argv[1]));
    }else
	{
		address = "http://localhost:8000";
    }
	//initialize server
    on_initialize(address);
    std::cout << "Press ENTER to exit." << std::endl;
	
	//stop main till ENTER
    std::string line;
    std::getline(std::cin, line);

    on_shutdown();
    return 0;
}
