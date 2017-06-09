#!/bin/bash

java -Djava.security.policy=java.policy Client localhost $1 $2 $3 $4 $5 1199 1299
gnuplot plot.plt
rm data.txt
