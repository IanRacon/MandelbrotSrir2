# MandelbrotSrir2

Made with Java, C++, RMI and JNI

* `javac MandelbrotTest.java`
* `javah MandelbrotTest`
* `gcc -sdt=c99 -fPIC "-I/usr/lib/jvm/java-8-oracle/include" "-I/usr/lib/jvm/java-8-oracle/include/linux" -c MandelbrotTest.c`
* `gcc --shared -o libMandelbrotTest.so MandelbrotTest.o`
* `cp MandelbrotTest.java libMandelbrotTest.so ..`

RMI how to:
* `javac IMandelbrotResolver.java ChunkCoords.java ChunkData.java Server.java`
* `rmic Server`
* `javac Client.java`
* ```( for dir in `pwd`; do /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1199 -Djava.rmi.server.codebase=file://$dir/ ; done) &```
* ```(for dir in `pwd`; do export LD_LIBRARY_PATH=$dir; java -cp . -Djava.security.policy=$dir/java.policy -Djava.rmi.server.codebase=file://$dir/ Server 1199; done) &```
* ```( for dir in `pwd`; do /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1299 -Djava.rmi.server.codebase=file://$dir/ ; done) &```
* ```(for dir in `pwd`; do export LD_LIBRARY_PATH=$dir; java -cp . -Djava.security.policy=$dir/java.policy -Djava.rmi.server.codebase=file://$dir/ Server 1299; done) &```
* ```java -Djava.security.policy=java.policy Client localhost 0.273 0.276 0.484 0.486 0.000005 1199 1299```
