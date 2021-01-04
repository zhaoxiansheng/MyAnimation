package com.autopai.common.utils.numeral;

public class MatrixUtil {
    public static float getMatrixTransX(float[] matrix){
        if (matrix.length >= 9) {
            return matrix[2];
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float getMatrixTransY(float[] matrix){
        if (matrix.length >= 9) {
            return matrix[5];
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float getMatrixScaleX(float[] matrix){
        if (matrix.length >= 9) {
            return matrix[0];
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float getMatrixScaleY(float[] matrix){
        if (matrix.length >= 9) {
            return matrix[4];
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float getMatrixSkewX(float[] matrix){
        if (matrix.length >= 9) {
            return matrix[1];
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float getMatrixSkewY(float[] matrix){
        if (matrix.length >= 9) {
            return matrix[3];
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    //$$$$$$$$$$$$$$$$$$$$$$$$$$$
    public static float[] setMatrixTransX(float[] matrix, final float transX){
        if (matrix.length >= 9) {
            matrix[2] = transX;
            return matrix;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float[] setMatrixTransY(float[] matrix, final float transY){
        if (matrix.length >= 9) {
            matrix[5] = transY;
            return matrix;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float[] setMatrixScaleX(float[] matrix, final float scalX){
        if (matrix.length >= 9) {
            matrix[0] = scalX;
            return matrix;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float[] setMatrixScaleY(float[] matrix, final float scalY){
        if (matrix.length >= 9) {
            matrix[4] = scalY;
            return matrix;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float[] setMatrixSkewX(float[] matrix, final float skewX){
        if (matrix.length >= 9) {
            matrix[1] = skewX;
            return matrix;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static float[] setMatrixSkewY(float[] matrix, float skexY){
        if (matrix.length >= 9) {
            matrix[3] = skexY;
            return matrix;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

}
