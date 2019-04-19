package com.example.udemyone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class ThirdActivity extends AppCompatActivity {
    private EditText etPhone;
    private EditText etWeb;
    private ImageButton ibPhone;
    private ImageButton ibWeb;
    private ImageButton ibCamera;
    private final int PHONE_CALL_CODE=100;
    private final int PICTURE_FROM_CAMERA=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        etPhone= (EditText) findViewById(R.id.etPhone);
        etWeb= (EditText) findViewById(R.id.etWeb);
        ibPhone= (ImageButton) findViewById(R.id.ibPhone);
        ibWeb= (ImageButton) findViewById(R.id.ibWeb);
        ibCamera= (ImageButton) findViewById(R.id.ibCamera);
        // boton para llamada
        ibPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=etPhone.getText().toString();
                if((phone!=null)&&(!phone.isEmpty())){
                    //comprobar version actual de android en ejecución
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        //comprobar si ha aceptado, no ha aceptado y nunca se le ha preguntado
                        if (checkPermission(Manifest.permission.CALL_PHONE)){
                            //ha aceptado
                            Intent intentCall= new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
                            if(ActivityCompat.checkSelfPermission(ThirdActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED) return;
                            startActivity(intentCall);
                        }else{
                            //no ha aceptado o es la primera vez que se le pregunta
                            if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //si no se le ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PHONE_CALL_CODE);
                            }else{
                                Toast.makeText(ThirdActivity.this,"Please, enable the request permission",Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:"+getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(intent);
                            }
                        }
                    }else{
                        olderVersion(phone);
                    }
                }else{
                    Toast.makeText(ThirdActivity.this,"Insert phone number",Toast.LENGTH_SHORT).show();
                }
            }
            private void olderVersion(String phoneNumber){
                //ACTION_CALL o ACTION_DIAL
                Intent intentCall= new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                if(checkPermission(Manifest.permission.CALL_PHONE)){
                    startActivity(intentCall);
                }else{
                    Toast.makeText(ThirdActivity.this,"You declined the access",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ibWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= etWeb.getText().toString();
                if((url!=null)&&(!url.isEmpty())){
                    //primera forma
                    //Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+url));
                    //segunda forma
                    //intent para web
                    Intent intentWeb= new Intent();
                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("https://"+url));
                    //intent para contactos
                    Intent intentContacts= new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people"));
                    //intent para emails rapido
                    //Intent intentEmails= new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:reinaldoagf"));
                    //intent para emails completo
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/email");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Mail's title");
                    emailIntent.putExtra(Intent.EXTRA_TEXT,"Hello, this is the extra text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "roiner123@gmail.com"});
                    final PackageManager pm = ThirdActivity.this.getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    String className = null;
                    for (final ResolveInfo info : matches) {
                        if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                            className = info.activityInfo.name;

                            if(className != null && !className.isEmpty()){
                                break;
                            }
                        }
                    }
                    emailIntent.setClassName("com.google.android.gm", className);
                    startActivity(emailIntent);
                }
            }
        });
        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent camara
                Intent intentCamara= new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intentCamara, PICTURE_FROM_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case PICTURE_FROM_CAMERA:
                if (resultCode== Activity.RESULT_OK){
                    String result = data.toUri(0);
                    Toast.makeText(ThirdActivity.this,"Result: "+result,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ThirdActivity.this,"There was an error, try again.",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PHONE_CALL_CODE:
                String permission=permissions[0];
                int result= grantResults[0];
                if (permission.equals(Manifest.permission.CALL_PHONE)){
                    if(result==PackageManager.PERMISSION_GRANTED){
                        //permiso concedido
                        Intent intentCall= new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+etPhone.getText().toString()));
                        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED) return;
                        startActivity(intentCall);
                    }else{
                        //permiso no concedido
                        Toast.makeText(ThirdActivity.this,"You declined the access",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean checkPermission(String permission){
        int result=this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
