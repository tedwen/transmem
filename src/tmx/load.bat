javac -classpath ../../build/WEB-INF/classes -d . LoadTmx.java
@if errorlevel 1 goto end
java -cp ./;../../build/WEB-INF/classes;../../jdbc/postgresql-8.2-504.jdbc3.jar loader.LoadTmx %1
:end
