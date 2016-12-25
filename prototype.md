### Team Members
Khyber Sen

Stanley Lin

### Team Name

SenSational TraveLin

# Project Title
QuickTrip: An All-in-One Trip Planner

### 1. Project Idea: Ultimate Goal
An application to assist in trip planning. Recommends hotels, flights, rental cars, directions, restaurants, and events to do based on user input. It scrapes information from the Google Maps and TripAdvisor, Travelocity, etc. APIs and plans the optimal trip, start to finish, all costs included, based on some minimal user input, with the option to tune parameters and filter results.

### 2. Critical Features
__Input__
- origin
- start date
- list of destinations with number of days
- number of people
- approximate budget

__Output__
- Itinerary Overview  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  __Each Destination__
  - hotel logo
  - hotel name
  - hotel location (address)
  - mode of transportation (will begin with only driving)
  - number of days
  - dates (perhaps on a calendar)
- cost
- option to see detailed itinerary
- option to see directions (maybe embedded Google Maps or just text directions)

### 3. Features to Be Added Later
- other modes of transportation besides driving (i.e. trains, planes, rental cars, buses, and other public transportation)
- if flying (ships, too) is added, allow for transoceanic travel
- add in things to do in itinerary
- add in places to eat in itinerary
- while the program is searching, prompt the user to see if certain optimizations are to their liking, or allow them to select certain areas to focus the optimization on

### 4. Development Stages
1. set up API connectons with API keys
2. decide what information from API responses are needed
3. use Google Maps Direction API to find optimal order of destinations
4. using destination order, find dates for each destination
5. search for hotels for those dates at those locations
6. use simulated annealing or something similar to find the optimal set of hotels within the budget
7. output the data in the GUI
8. set up the input GUI and parse the input

### 5. Diagrams of Features
// TODO
