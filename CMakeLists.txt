cmake_minimum_required(VERSION 2.8.2)
project(SnakeKinolowy)

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
#include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/preprocessing/include ${CMAKE_CURRENT_SOURCE_DIR}/service/include ${CMAKE_CURRENT_SOURCE_DIR})
# Sources - headers for QT Creator convenience
file(GLOB_RECURSE SOURCES RELATIVE ${CMAKE_CURRENT_SOURCE_DIR} FOLLOW_SYMLINKS
     src/*.cpp src/*.hpp include/*.h include/*.hpp
     preprocessing/src/*.cpp preprocessing/src/*.hpp preprocessing/include/*.h preprocessing/include/*.hpp
     service/src/*.cpp service/src/*.hpp service/include/*.h service/include/*.hpp
     )


 include_directories(${OPENSSL_INCLUDE_DIR})
 list(APPEND LIB_LIST ${OPENSSL_LIBRARIES})


# Build and link the executable
set(EXECUTABLE_NAME ${CMAKE_PROJECT_NAME})
set(Casablanca_LIBRARY
    ${LIB}cpprest)
set(Casablanca_LIBRARIES
    ${Casablanca_LIBRARY}
    ${Boost_FILESYSTEM_LIBRARY}
    ${Boost_SYSTEM_LIBRARY}
    ${Boost_THREAD_LIBRARY}
    ${Boost_ATOMIC_LIBRARY}
    ${Boost_CHRONO_LIBRARY}
    ${Boost_RANDOM_LIBRARY}
    ${Boost_REGEX_LIBRARY}
    ${Boost_FRAMEWORK}
  )

add_executable(${EXECUTABLE_NAME} ${SOURCES} main.cpp)
target_link_libraries(${EXECUTABLE_NAME} ${Casablanca_LIBRARIES} ${Boost_LIBRARIES} ssl crypto)
