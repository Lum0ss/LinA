package com.atom.lina;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// custom imports
import org.ejml.simple.SimpleMatrix;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        matrixTest();
    }

    private void matrixTest(){
        SimpleMatrix matrixA = new SimpleMatrix(new double[][]{
                {1.0f, 1.0f},
                {2.0f, 2.0f}
        });
        TextView matrixTestView = findViewById(R.id.matrixTestView);
        matrixTestView.setText("Matrix: " + matrixA);
    }

    private SimpleMatrix readMatrix(String input){
        // split string into rows
        String[] rows = input.split("\n");
        int n = rows.length;
        int m = rows[0].split(" ").length;
        double[][] matrixVals = new double[n][m];
        boolean sameLength = true;
        for(int i = 0; i < n; i++){
            String[] cols = rows[i].split(" ");
            int m_ = cols.length;
            if(m_ != m){
                sameLength = false;
                break;
            }
            for(int j = 0; j < m; j++) {
                try {
                    double value = Double.parseDouble(cols[j]);
                    matrixVals[i][j] = value;
                } catch (NumberFormatException e) {
                    System.out.println("Cannot convert input: " + cols[j]);
                }
            }
        }
        // check if all rows are of the same length
        if(!sameLength){
            System.out.println("Invalid matrix dimensions");
            SimpleMatrix errMat = SimpleMatrix.filled(n, m, 0.0);
            return errMat;
        }
        // return the correct matrix
        SimpleMatrix matrix = new SimpleMatrix(matrixVals);
        return matrix;
    }
}