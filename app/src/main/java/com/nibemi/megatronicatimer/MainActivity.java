package com.nibemi.megatronicatimer;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH = 0;
    AlertDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkbluetooth();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.conectar_bluetooth:
                if (!checkbluetooth()){
                    // Muestra mensaje para habilitar bluetooth en dispositivo.
                    Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(eintent, REQUEST_BLUETOOTH);
                }else{
                    AlertDialog.Builder mBuilder= new AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.conexion_bluetooth, null);

                    ListView listado_dispositivos = (ListView)findViewById(R.id.listado_dispositivos);
                    Button conexion_dispositivo =  (Button)mView.findViewById(R.id.conexion_dispositivo);

                    conexion_dispositivo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogo.dismiss();
                        }
                    });

                    mBuilder.setView(mView);
                    dialogo = mBuilder.create();
                    dialogo.show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkbluetooth(){
        boolean respuesta = false;

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Su dispositivo no acepta conexiones Bluetooth.",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                respuesta = true;
            }
        }
        return respuesta;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){

            AlertDialog.Builder mBuilder= new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.conexion_bluetooth, null);

            ListView listado_dispositivos = (ListView)findViewById(R.id.listado_dispositivos);
            Button conexion_dispositivo =  (Button)mView.findViewById(R.id.conexion_dispositivo);

            conexion_dispositivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogo.dismiss();
                }
            });

            mBuilder.setView(mView);
            dialogo = mBuilder.create();
            dialogo.show();
        }else{
            Toast.makeText(getApplicationContext(), "No se pueden listar dispositivos si no activa el Bluetooth.",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}