﻿CREATE TABLE tweets
  (
  	tweetid bigint PRIMARY KEY,
  	userid bigint NOT NULL,
  	responseid bigint NOT NULL,
  	retweetid bigint NOT NULL,
  	tweet varchar(140) NOT NULL,
  	createat varchar(50) NOT NULL,
  	lat double precision NOT NULL,
  	lon double precision NOT NULL
  );


CREATE TABLE Keywords
  (
  	tweetid bigint REFERENCES tweets (tweetid),
  	keyword varchar(255)
  );