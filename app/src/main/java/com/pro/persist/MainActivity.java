package com.pro.persist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pro.persist.inter.SuperBindContentView;
import com.pro.persist.inter.SuperBindOnClick;
import com.pro.persist.inter.SuperBindView;
import com.pro.persist.parse.SuperBind;


@SuperBindContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @SuperBindView(R.id.textTop)
    TextView mTextTop;
    @SuperBindView(R.id.textBottom)
    TextView mTextBottom;
    @SuperBindView(R.id.btn)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        SuperBind.bindContentView(this);
        SuperBind.bindView(this);
        SuperBind.bindOnClick(this);

        mTextTop.setText("success!");
        mTextBottom.setText("success!");
    }

    @SuperBindOnClick({R.id.btn})
    public void buttonClick(View view) {
        mTextTop.setText("click");
    }


}