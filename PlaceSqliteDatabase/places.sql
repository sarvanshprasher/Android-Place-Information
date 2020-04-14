DROP TABLE place;


CREATE TABLE place (
name PRIMARY KEY NOT NULL,
description TEXT,
category TEXT,
address_title TEXT,
address_street TEXT,
elevation DOUBLE,
latitude DOUBLE,
longitude DOUBLE);


INSERT INTO place VALUES
('Notre-Dame-Paris','The 13th century cathedral with gargoyles, one of the first flying buttress, and home of the purported crown of thorns.',
'Travel'
,'Cathedral Notre Dame de Paris',
'6 Parvis Notre-Dame Pl Jean-Paul-II$75004 Paris France',
115,48.852972,2.34991);

INSERT INTO place VALUES
('Toreros','The University of San Diego, a private Catholic undergraduate university.',
'School'
,'University of San Diego',
'5998 Alcala Park$San Diego CA 92110',
200,32.771923,-117.188204);

INSERT INTO place VALUES
('Rogers-Trailhead','Trailhead for hiking to Rogers Canyon Ruins and Reavis Ranch',
'Hike'
,NULL,
NULL,
4500,33.422212,-111.173393);

INSERT INTO place VALUES
('New-York-NY','New York City Hall at West end of Brooklyn Bridge',
'Travel'
,'New York City Hall',
'1 Elk St$New York NY 10007',
2,40.712991,-74.005948);
