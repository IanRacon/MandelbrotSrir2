#include "MandelbrotImpl.h"
#include <complex.h>
#include <math.h>
#include <stdlib.h>

static int getRows(double miny, double maxy, double step) {
    return floor((maxy - miny) / step) + 1;
}

static int getCols(double minx, double maxx, double step) {
    return floor((maxx - minx) / step) + 1;
}

static int **initBoard(int rows, int cols) {
    int **board = (int**) malloc(rows * sizeof(int *));
    for (int i = 0; i < rows; ++i) {
        board[i] = (int*) malloc(cols * sizeof(int));
    }
    return board;
}

static void freeBoard(int **board, int rows) {
    for (int i = 0; i < rows; ++i) {
         free(board[i]);
    }
    free(board);
}

static int **mandelbrot(double minx, double maxx, double miny, double maxy, double step, int rows, int cols, int maxiters) {

    int **board = initBoard(rows, cols);
    
    double x_mult = (maxx - minx) / (cols - 1);
    double y_mult = (maxy - miny) / (rows - 1);
    
    for (int row = 0; row < rows; ++row) {
        for (int col = 0; col < cols; ++col) {
            
            double complex c = (x_mult * col + minx) + (y_mult * row + miny) * I;
            double complex z = 0;
            
            int iters = 0;
            
            do {
                z = z * z + c;
                ++iters;
            } while (iters < maxiters && fabs(creal(z)) <= 2.0);
            
            board[row][col] = iters;
            
        }
    }
    
    return board;
}

JNIEXPORT jobjectArray JNICALL Java_MandelbrotImpl_compute(JNIEnv *env, jobject obj) {
    
    jclass mandelbrotClass = (*env)->GetObjectClass(env, obj);
    jclass intArray1DClass = (*env)->FindClass(env, "[I");
    
    jfieldID minxId = (*env)->GetFieldID(env, mandelbrotClass, "minx", "D");
    jfieldID maxxId = (*env)->GetFieldID(env, mandelbrotClass, "maxx", "D");
    jfieldID minyId = (*env)->GetFieldID(env, mandelbrotClass, "miny", "D");
    jfieldID maxyId = (*env)->GetFieldID(env, mandelbrotClass, "maxy", "D");
    jfieldID stepId = (*env)->GetFieldID(env, mandelbrotClass, "step", "D");
    jfieldID maxitersId = (*env)->GetStaticFieldID(env, mandelbrotClass, "maxiters", "I");
    
    jdouble minx = (*env)->GetDoubleField(env, obj, minxId);
    jdouble maxx = (*env)->GetDoubleField(env, obj, maxxId);
    jdouble miny = (*env)->GetDoubleField(env, obj, minyId);
    jdouble maxy = (*env)->GetDoubleField(env, obj, maxyId);
    jdouble step = (*env)->GetDoubleField(env, obj, stepId);
    jint maxiters = (*env)->GetStaticIntField(env, mandelbrotClass, maxitersId);
    
    int rows = getRows(miny, maxy, step);
    int cols = getCols(minx, maxx, step);
    int **board = mandelbrot(minx, maxx, miny, maxy, step, rows, cols, maxiters);
    
    jobjectArray array2D = (*env)->NewObjectArray(env, rows, intArray1DClass, NULL);
    for (int row = 0; row < rows; ++row) {
        jintArray array1D = (*env)->NewIntArray(env, cols);
        (*env)->SetIntArrayRegion(env, array1D, 0, cols, board[row]);
        (*env)->SetObjectArrayElement(env, array2D, row, array1D);
    }
    
    freeBoard(board, rows);
    
    return array2D;
    
}
