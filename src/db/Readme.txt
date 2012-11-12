psql -U postgres
CREATE ROLE tm LOGIN PASSWORD='tm';
\du
CREATE DATABASE transmem WITH ENCODING 'UNICODE' OWNER tm;
\lt
\i createtables.sql;
\dt
\i langcodes.sql;
