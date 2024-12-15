package com.atom.lina;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        // set filter for matrix input
        InputFilter[] matrixInputFilter = new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int i2, int i3) {
                        for(int i = 0; i < end; i++){
                            char c = charSequence.charAt(i);
                            if(!Character.isDigit(c) && c != '.' && c != '-' && c != ' ' && c != '\n'){
                                return ""; // return empty char
                            }
                        }
                        return null;
                    }
                }
        };
        // set the input filter for both edittext fields
        EditText matrixAEditText = (EditText) findViewById(R.id.matrixAInput);
        EditText matrixBEditText = (EditText) findViewById(R.id.matrixBInput);
        matrixAEditText.setFilters(matrixInputFilter);
        matrixBEditText.setFilters(matrixInputFilter);

        // create multiply button
        Button multiplyButton = (Button) findViewById(R.id.multiplyButton);
        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMultiply();
            }
        });
    }

    // read the matrices and multiply them when the multiply button is clicked
    private void onClickMultiply(){
        EditText matrixAEditText = (EditText) findViewById(R.id.matrixAInput);
        EditText matrixBEditText = (EditText) findViewById(R.id.matrixBInput);
        String matrixAString = matrixAEditText.getText().toString();
        String matrixBString = matrixBEditText.getText().toString();
        SimpleMatrix matrixA = readMatrix(matrixAString);
        SimpleMatrix matrixB = readMatrix(matrixBString);
        // check if the matrices can be multiplied
        if(matrixA.getNumCols() != matrixB.getNumRows()){
            System.out.println("Incompatible dimensions.");
            return;
        }
        SimpleMatrix productMatrix = matrixA.mult(matrixB);
        String productMatrixString = productMatrix.toString();
        TextView resultView = findViewById(R.id.resultView);
        resultView.setText(productMatrixString);
    }

//    private void matrixTest(){
//        SimpleMatrix matrixA = new SimpleMatrix(new double[][]{
//                {1.0f, 1.0f},
//                {2.0f, 2.0f}
//        });
//        TextView matrixTestView = findViewById(R.id.matrixTestView);
//        matrixTestView.setText("Matrix: " + matrixA);
//    }

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