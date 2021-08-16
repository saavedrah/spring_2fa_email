CREATE TABLE loginUser(
    userid      varchar(40) CONSTRAINT firstkey PRIMARY KEY,
    email       varchar(200) NOT NULL,
    password    varchar(40) NOT NULL,
    username    varchar(200) NOT NULL,
    one_time_password  varchar(64),
    otp_requested_time  timestamp,
    enabled     int
);
