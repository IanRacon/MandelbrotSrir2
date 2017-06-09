* `javac Mandelbrot.java`
* `javah Mandelbrot`
* `gcc "-I/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/include" "-I/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/include/darwin" -c MandelbrotTest.c`
* `gcc --shared -o MandelbrotTest.so MandelbrotTest.o "-L/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/jre/lib/server/"`
* `java Mandelbrot`