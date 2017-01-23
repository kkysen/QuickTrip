# QuickTrip
### An All-in-One Trip Planner

Stuy APCS Fall Final Project

#### What It's Supposed To Be
An application to assist in trip planning. Recommends hotels, flights, rental cars, directions, restaurants, and events to do based on user input. It scrapes information from the Google Maps and TripAdvisor, Travelocity, etc. APIs and plans the optimal trip, start to finish, all costs included, based on some minimal user input, with the option to tune parameters and filter results.

#### Instructions to run
Clone the repo and run QuickTrip.jar by double clicking on it.  This runs the working demo version, because the updated one, NewQuickTrip.jar is not completely finished.

#### QuickTrip.jar
This old, working version only finds the hotels.  There is nothing about flights in this one, so it will never work with overseas destinations, and will only use driving to get from one destination to another.  In the itinerary screen at the end, it's supposed to display hotel names, pictures, and some other stuff, but we never worked much of perfecting that part since we were focusing on the new flight stuff that we never finished.

#### NewQuickTrip.jar
This updated version attempts to do a lot more and is able to do most of it, but it's not completely finished. The final GUI isn't done and there are some bugs.

First you enter the origin address, the start date, the list of destinations and the number of days to you want to stay there, the number of people, and your budget.  After validating this input, especially validating the addresses through Google's Geocoding API, the program attempts to plan your trip.

The destinations you enter are unordered, so the program tries to find the optimal route through them (like a TSP). It first tries to do this by sending a Google Maps Directions API request to optimize the waypoints, with the origin as the origin and destination and the destinations as the waypoints.  If Google Maps finds a route successfully, this means that none of the destinations require flying (more on that later).

The program then starts building the itinerary.  If the driving distance between two places is greater than a certain amount of time (set to 6 hrs now), it finds a flight there instead (more on than later).  It keeps building the itinerary, tracking where and on what dates hotels need to be found.  The final itinerary consists of a list of list of destinations, with each of the lists of destinations being separated by flights and each of the destinations containing the start and end date there and the hotel (or airport).

Having built the scaffolding of the itinerary, it now tries to find actual flights and hotels.  For the flights, it uses Google's QPX Express API.  After finding nearby airports at that location using our own downloaded list of airports, it creates of a list of possible flights for each real flight.  It does the same for the hotels, finding a list of possible hotels for each destination by scraping data from hotels.com.

Having found the lists of possible flights and possible hotels, it tries to find the optimal ones.  For the hotels, it uses simulated annealing to find the best hotels considering both price (compared to the budget) and rating.  For flights, we aim for it to do something similar in the future, but right now it just chooses the shortest (by time) flight.

Having found all the destinations, flights, and hotels, it finds driving directions between each list of destinations (separated by flights).

In the case that some destinations are overseas and thus necessitate flying, the program employs a different strategy.  It's very hard to know between which two places flights are required, and we can't just try them all out using Google's API since it would quickly go over daily quotas.  Therefore, we used a DBSCAN clusterer from Apache Commons Math to cluster the destinations into clusters within a certain radius, inside which the likelihood that a flight is needed is much lower.

In certain cases, a short flight will still be needed, but this hopefully solves the majority of the cases.  The program then finds flights between the clusters and then pretty much continues as before (with no overseas destinations).  This part is mostly untested (see below for why), but in theory it should usually work.

One major problem with our program is the daily quotas enforced by APIs like Google's.  We tried to solve this partially by caching all the requests made by serializing them as JSON files, so when testing this helped a lot when trying to test the same thing over and over again.  It also dramatically increases the speed of the program when run with the same input.

However, nothing can be done about new API requests.  We are currently using four different Google API keys from different Google accounts, so for most of the APIs, this is okay.  But the QPX Express one is limited to 50 requests per day instead of the usually 1,000 for other APIs, so this is a major bottleneck.  This is why some of the new flight stuff was very hard to test, since we kept going over the limit.

You can still run this jar.  The input screen works fine, but nothing is yet shown for the itinerary screen.  The programs prints a bunch of unorganized stuff about the itinerary to the standard output, so you can kind of see what it finds, but sometimes it fails.  The QPX Express API has a knack for finding really bad flights, like routing you through Toronto or Boston if you try to fly from New York to Chicago, so this creates a lot of the errors.  If a ExceededDailyQuotaException is thrown, then the daily quote (for QPX Express probably) has been exceeded, so you'll need to wait until tomorrow to run more tests.  You could try adding your own Google API keys to GoogleApiRequest and commenting out the used up ones and then rebuilding the jar.
