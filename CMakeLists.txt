cmake_minimum_required(VERSION 2.8.2)
project(KinoloSnake)
set(LOGIC_NAME "SnakeLogicServer")

if (UNIX)
  add_definitions(-Wno-sign-compare -Wno-enum-compare)
endif()

# CMake flags
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall")


# External libraries
find_package(Boost REQUIRED COMPONENTS random chrono system thread regex filesystem)
find_package(OpenSSL REQUIRED)

include_directories(${Boost_INCLUDE_DIRS})
list( APPEND CMAKE_CXX_FLAGS -std=c++11)

link_directories(/usr/local/lib)

# Includes
include_directories(include SnakeLogicServer/include/)
# Sources - headers for QT Creator convenience
file(GLOB_RECURSE SOURCES RELATIVE ${CMAKE_CURRENT_SOURCE_DIR} FOLLOW_SYMLINKS
    src/*.cpp  include/*.hpp
    SnakeLogicServer/src/*.cpp SnakeLogicServer/include/SnakeLogic/*.hpp)


include_directories(${OPENSSL_INCLUDE_DIR})
list(APPEND LIB_LIST ${OPENSSL_LIBRARIES})

# Build and link the executable
set(EXECUTABLE_NAME ${CMAKE_PROJECT_NAME})

add_executable(${EXECUTABLE_NAME} ${SOURCES} main.cpp)
target_link_libraries(${EXECUTABLE_NAME} ${Boost_LIBRARIES} ssl )

add_executable(${LOGIC_NAME} ${SOURCES} SnakeLogicServer/main_logic.cpp)
target_link_libraries(${LOGIC_NAME} ${Boost_LIBRARIES} ssl )
