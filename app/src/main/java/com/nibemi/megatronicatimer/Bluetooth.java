package com.nibemi.megatronicatimer;

import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.UUID;

        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.widget.TextView;

public class Bluetooth {

    // id de la aplicación para la conexión con bluetooth
    private static final String UUID_CODE = "00001101-0000-1000-8000-00805F9B34FB";

    // declaración de variables para uso de bluetooth
    private ArrayList<BluetoothDevice> devices;
    private BluetoothAdapter adapter;

    // variable socket para el envio, recepción y conexión del bluetooth
    private BluetoothSocket socket;

    public Bluetooth() {
        devices = new ArrayList<BluetoothDevice>();

        //Obtenemos el dispositivo bluetooth del terminal
        adapter = BluetoothAdapter.getDefaultAdapter();

        //Si no hay dispositivo terminamos
        if (adapter == null)
            return;

        //Obtenemos todos los dispositivos bluetooth
        //vinculados
        for (BluetoothDevice d : adapter.getBondedDevices())
            devices.add(d);

    }

    /**
     * Devuelve el nombre de los dispositivos
     * vinculados para poder seleccionarlo
     * @return
     */
    public String[] getNames() {
        String names[] = new String[devices.size()];

        for (int i = 0; i < devices.size(); i++)
            names[i] = devices.get(i).getName();

        return names;
    }


    /**
     * Método para conectar a un dispositivo Bluetooth
     * según su posición en la lista.
     * @param index
     * @return si ha conectado o no
     */
    public boolean connect(int index) {
        if (index < 0 || index >= devices.size())
            return false;

        try {
            //Obtenemos el dispositivo según su posición
            BluetoothDevice device = devices.get(index);
            //Conectamos con el dispositivo
            socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(UUID_CODE));
            socket.connect();
            return true;

        } catch (IOException e) {
            //Si ha ocurrido algún error devolvermos false
            e.printStackTrace();
            return false;
        }


    }

    /**
     * Método para desconectar
     */
    public void disconnect() {
        if (isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }


    /**
     * Método que nos indica si está o no coenctado
     * el socket bluetooth
     */
    public boolean isConnected() {
        if (socket == null)
            return false;

        return socket.isConnected();
    }


    /**
     * Método para enviar un dato mediante bluetooth hacia el arduino
     * @param dato
     */
    public void send(int dato) {

        //Si el socket está a null es que no hemos conectado
        if (socket == null)
            return;


        try {
            socket.getOutputStream().write(dato);

        } catch (IOException e) {}
    }

    /**
     * Método para recibir un dato o string mediante bluetooth desde el arduino
     */

    public String recibir(){
        if (socket==null){
            return "0";
        }

        final byte delimiter=10;
        int position=0;
        String envio="";
        byte[] readBuffer=new byte[1024];

        try{
            int bytesAvailable= socket.getInputStream().available();
            if (bytesAvailable>0){
                byte[] packetBytes = new byte[bytesAvailable];
                socket.getInputStream().read(packetBytes);

                for (int i=0; i<bytesAvailable; i++){
                    byte b= packetBytes[i];
                    if(b == delimiter) {
                        byte[] encodedBytes = new byte[position];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String data = new String(encodedBytes, "US-ASCII");
                        position = 0;
                        envio=data;
                    }
                    else {
                        readBuffer[position++] = b;
                    }
                }


            }

            return envio;

        }catch (IOException e){ return "0"; }

    }


}
