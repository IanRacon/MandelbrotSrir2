# MandelbrotSrir2

Made with Java, C++, RMI and JNI

* `javac MandelbrotTest.java`
* `javah MandelbrotTest`
* `gcc "-I/usr/lib/jvm/java-8-oracle/include" "-I/usr/lib/jvm/java-8-oracle/include/linux" -c MandelbrotTest.c`
* `gcc --shared -o libMandelbrotTest.so MandelbrotTest.o`

RMI how to:
* `javac IMandelbrotResolver.java ChunkCoords.java ChunkData.java Server.java`
* `rmic Server`
* `javac Client.java`
* `( for dir in `pwd`; do cd /tmp ; /usr/lib/jvm/java-8-oracle/bin/rmiregistry 1199 -Djava.rmi.server.codebase=file://$dir/ ; done) &`
* `(for dir in `pwd`; do export LD_LIBRARY_PATH=$dir;java -cp . -Djava.security.policy=$dir/java.policy -Djava.rmi.server.codebase=file://$dir/ Server 1099; done) &`
