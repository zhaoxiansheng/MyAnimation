package com.autopai.common.shader.EffectShader;

import android.graphics.ColorMatrix;
import android.util.Log;

import org.ujmp.core.Matrix;
import org.ujmp.core.enums.ValueType;


public class MultiColorMatrix extends ColorMatrix {
    private static final String TAG = "MultiColorMatrix";
    private static boolean USE_REVISE = true;

    private static class DisposeMatrix {
        Matrix mMatrix4;
        Matrix mColumn;

        private void dispose(ColorMatrix color){
            if(color != null){
                float[] thisArray = color.getArray();
                mMatrix4 = Matrix.Factory.zeros(ValueType.FLOAT, 4, 4);
                mColumn = Matrix.Factory.zeros(ValueType.FLOAT,4, 1);

                for (int i = 0, row = 0, column = 0; i < thisArray.length; ++i) {
                    row = i / 5;
                    column = i % 5;
                    if (column != 4) {
                        mMatrix4.setAsFloat(thisArray[i], row, column);
                    } else {
                        mColumn.setAsFloat(thisArray[i], row, 0);
                    }
                }
            }
        /*thisM.minus();
        thisM.plus();
        thisM.mtimes();
        thisM.divide();*/
        }

        static float[] compose(float[] newColorM, Matrix matrix4, Matrix columnM) {
            if (matrix4 != null && columnM != null) {
                if(newColorM == null) {
                    newColorM = new float[20];
                }
                for (int i = 0, row = 0, column = 0; i < newColorM.length; ++i) {
                    row = i / 5;
                    column = i % 5;
                    if (column != 4) {
                        newColorM[i] = matrix4.getAsFloat(row, column);
                    } else {
                        newColorM[i] = columnM.getAsFloat(row, 0);
                    }
                    if(USE_REVISE){
                        if(Math.abs(newColorM[i]) < 3E-5){
                            newColorM[i] = 0.0f;
                        }
                    }
                }
                return newColorM;
            }
            return null;
        }
    }

    public MultiColorMatrix(float[] src){
        super(src);
    }

    public MultiColorMatrix(){
        super();
    }

    public void divide(ColorMatrix rMatrix){
        DisposeMatrix leftDispose = new DisposeMatrix();
        leftDispose.dispose(this);

        DisposeMatrix rightDispose = new DisposeMatrix();
        rightDispose.dispose(rMatrix);

        Matrix newMatrix4 = leftDispose.mMatrix4.mtimes(rightDispose.mMatrix4.inv());

        Matrix newColumn = leftDispose.mColumn.minus(newMatrix4.mtimes(rightDispose.mColumn));

        float[] newArray = DisposeMatrix.compose(this.getArray(), newMatrix4, newColumn);

        //this.set(newArray);
    }



}
