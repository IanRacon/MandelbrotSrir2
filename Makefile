all:
	javac MandelbrotImpl.java
	gcc -std=c99 -fPIC "-I/usr/lib/jvm/java-8-oracle/include" "-I/usr/lib/jvm/java-8-oracle/include/linux" -c MandelbrotImpl.c
	gcc --shared -o libMandelbrotImpl.so MandelbrotImpl.o
	javac IMandelbrotResolver.java ChunkCoords.java ChunkData.java Server.java Client.java

start-server:
	rmic Server 2> /dev/null
	echo "" > $@
	{ ( for dir in `pwd`; do /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1199 -Djava.rmi.server.codebase=file://$dir/ ; done ) & echo $$! >> $@ }
	{ ( for dir in `pwd`; do export LD_LIBRARY_PATH=$dir; java -cp . -Djava.security.policy=$dir/java.policy -Djava.rmi.server.codebase=file://$dir/ Server 1199; done ) & echo $$! >> $@ }
	{ ( for dir in `pwd`; do /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1299 -Djava.rmi.server.codebase=file://$dir/ ; done ) & echo $$! >> $@ }
	{ ( for dir in `pwd`; do export LD_LIBRARY_PATH=$dir; java -cp . -Djava.security.policy=$dir/java.policy -Djava.rmi.server.codebase=file://$dir/ Server 1299; done ) & echo $$! >> $@ }

run:
	java -Djava.security.policy=java.policy Client localhost 0.273 0.276 0.484 0.486 0.000005 1199 1299

stop-server:
	kill `cat start-server` && rm start-server

clean:
	rm *.o *.so *.class

.PHONY: start-server run stop-server clean
