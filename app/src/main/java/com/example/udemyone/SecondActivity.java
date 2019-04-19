package com.example.udemyone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    private TextView tv1;
    private Button btnPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tv1=(TextView) findViewById(R.id.tv1);
        Bundle bundle= getIntent().getExtras();
        if((bundle!=null)&&(bundle.getString("greeter")!=null)){
            String greeter=bundle.getString("greeter");
            Toast.makeText(this,greeter,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Empty bundle",Toast.LENGTH_LONG).show();
        }
        btnPermission=(Button) findViewById(R.id.btnPermission);
        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });
    }
}
