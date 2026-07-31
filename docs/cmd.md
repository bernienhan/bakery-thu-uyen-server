./gradlew clean
./gradlew build
./gradlew bootRun
./gradlew --version
./gradlew clean build
.\gradlew.bat compileJava


docker exec -it redis-local redis-cli
AUTH default redis123456
PING
QUIT
INFO keyspace
DBSIZE

INFO server
INFO memory
INFO clients
INFO persistence
INFO replication
INFO stats
CLIENT INFO

SCAN 0 MATCH * COUNT 100
GET key:cad2e849-2a2b-4e37-a169-339172b12b35