Authentication Prototype
========================

Based on a tutorial found http://maksim.sorokin.dk/it/2010/10/13/basic-authentication-in-glassfish-3/

If working on a local machine, you need to have access to a postgres server with a table format as follows

	CREATE TABLE users (
		uid int,
		username varchar(255),
		password varchar(255)
	)
and to have the `postgres_8.2-502.jar` library installed on your glassfish server.


