all:
	gcc -std=c99 -fPIC "-I/usr/lib/jvm/java-8-oracle/include" "-I/usr/lib/jvm/java-8-oracle/include/linux" -c MandelbrotImpl.c
	gcc --shared -o libMandelbrotImpl.so MandelbrotImpl.o
	javac MandelbrotImpl.java IMandelbrotResolver.java ChunkCoords.java ChunkData.java Server.java Client.java

start-server:
	rmic Server -nowarn
	cp /dev/null $@
	{ /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1199 -Djava.rmi.server.codebase=file://`pwd`/ & echo $$! >> $@; }
	{ export LD_LIBRARY_PATH=.; java -cp . -Djava.security.policy=java.policy -Djava.rmi.server.codebase=file://`pwd`/ Server 1199 & echo $$! >> $@; }
	{ /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1299 -Djava.rmi.server.codebase=file://`pwd`/ & echo $$! >> $@; }
	{ export LD_LIBRARY_PATH=.; java -cp . -Djava.security.policy=java.policy -Djava.rmi.server.codebase=file://`pwd`/ Server 1299 & echo $$! >> $@; }
run:
	java -Djava.security.policy=java.policy Client localhost 0.273 0.276 0.484 0.486 0.000005 1199 1299
	gnuplot plot.plt
	rm data.txt

stop-server:
	kill `cat start-server` && rm start-server

clean:
	rm *.o *.so *.class data.txt *.png

.PHONY: start-server run stop-server clean
