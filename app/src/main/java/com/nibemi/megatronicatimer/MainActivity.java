package com.nibemi.megatronicatimer;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ConexionBluetooth bsocket = new ConexionBluetooth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Su dispositivo no acepta conexiones Bluetooth.",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                Toast.makeText(getApplicationContext(), "Habilitando módulo Bluetooth automáticamente...",Toast.LENGTH_SHORT).show();
                Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(eintent, 1);
                // cargar lista de dispositivos
                bsocket.Listado_Dispositivos();
            }else{
                // Bluetooth esta activo
                // cargar lista de dispositivos
                bsocket.Listado_Dispositivos();
            }
        }
    }
}