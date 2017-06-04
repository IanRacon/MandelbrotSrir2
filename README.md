# MandelbrotSrir2

Made with Java, C++, RMI and JNI

*`javac MandelbrotTest.java`
*`javah MandelbrotTest`
*`gcc "-I/usr/lib/jvm/java-8-oracle/include" "-I/usr/lib/jvm/java-8-oracle/include/linux" -c MandelbrotTest.c`
*`gcc --shared -o libMandelbrotTest.so MandelbrotTest.o`
