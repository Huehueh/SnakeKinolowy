#define MAINFILE

#include <iostream>
#include "stdafx.h"
#include "Process.h"

std::string Process::process(std::string session){
    std::string answer="";
    if(session == "godzina"){
        std::chrono::time_point<std::chrono::system_clock> date = std::chrono::system_clock::now();
        std::time_t end_time = std::chrono::system_clock::to_time_t(date);
        answer = (std::string)std::ctime(&end_time);
    }else{
        if(session == "slodkie_slowko"){
            answer = "czekolada";
        }else{
            if(session == "usmiech"){
                answer = ":)";
            }
        }
    }
    return  answer;
}


