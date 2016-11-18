#pragma once
#include "stdafx.h"

#include "messagetypes.h"
#ifdef _WIN32
#include <concrt.h>
#endif

class Process
{
public:
    Process(int _id) : id(_id)
    {
        init();
    }
    std::string process(std::string session);//the only function

private:
    void init(){
        //workingFolder = "Session_"+std::to_string(id)+"/";
    }
    int id;
};
