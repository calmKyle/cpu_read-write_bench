#!/bin/bash
rm *.class
javac -cp .:sqlite-jdbc-3.47.0.0.jar *.java

echo "Running Perform . . ."
java Perform

echo "--------------" 
sleep 1

echo "Running Perform Read & Write . . ."
java PerformReadWrite 

echo "--------------" 
sleep 1

echo "Running Perform SQLite JDBC . . ."
java -cp .:sqlite-jdbc-3.47.0.0.jar PerformJDBC

