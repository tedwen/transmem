psql transmem postgres -f droptables.sql
psql transmem postgres -f createtables.sql
psql transmem postgres -c "COPY T_Languages FROM '/work/wen/tm/src/db/languages.txt'"
