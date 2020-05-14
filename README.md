# Android-Place-Information

This repository contains:

* Android Application
* JsonRPC Server
* SQLite Database
* Google Maps Integration


# More information related to Project

- This android application displays the list of all the places according to their categories(Hiking,Adventure,School) .

- Places can be added, updated, deleted from JsonRPC server as well as from SQLite Database present inside the application.

- On clicking on any place, it takes you to the place detail page where you will be seeing basic information about that
specific place.

- Distance between two places can also be measured based on their latitude and longitude information. We are calcuating these distances using this reference http://www.movable-type.co.uk/scripts/latlong.html

- Two places between which distance is measured can also be seen on inbuilt Google Maps integration using marker point. 

- New places can be added if you try to click on any place on Map. It will just request you for giving basic information such as name,description.

# Technologies used in this project

- Android studio[Development Environment]

- SQLite Database[Database] 

- JsonRPC Server[Local Server]

# Special Mention

- While implementing this project, lot of references were taken from Prof. Timothy Lindquist resources who teaches course SER-423(Mobile Systems).

- JsonRPC server was provided to us by Prof. Timothy Lindquist. 

# References 

1. http://pooh.poly.asu.edu/Mobile/Schedule/schedule.html

2. http://www.movable-type.co.uk/scripts/latlong.html
