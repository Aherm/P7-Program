library(RPostgreSQL)
library(sqldf)
drv = dbDriver("PostgreSQL")
con = dbConnect (drv,dbname="postgres", host = "localhost", port=5432, user="postgres"
,password="21")

options(sqldf.RPostgreSQL.user = "postgres", 
  sqldf.RPostgreSQL.password = "21",
  sqldf.RPostgreSQL.dbname = "postgres",
  sqldf.RPostgreSQL.host = "localhost", 
  sqldf.RPostgreSQL.port = 5432)
  
  