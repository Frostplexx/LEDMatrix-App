package com.example.ledmatrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Objects;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button button1;
    public TextInputLayout textInput;
    boolean connected = false;
    public static String sendingWord = "";

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


    // Method to send Sting to UDP Server
    public static void sendToServer(String nachricht) throws SocketException, IOException, Exception, Throwable
    {
        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress ipaddr = InetAddress.getByName("192.168.1.9");
        int port = 10002;
        byte[] sendData    = new byte[1024];

        sendData = nachricht.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipaddr, port);

        clientSocket.send(sendPacket);
        clientSocket.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(policy);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        textInput = (TextInputLayout) findViewById(R.id.editText1);


    }




    @Override
    public void onClick(View v) {
        //Toast.makeText(getApplicationContext(), "In editText steht : " + stringEditText, Toast.LENGTH_SHORT).show();

        switch (v.getId()) {
            case R.id.button1:
                sendMessage(textInput.getEditText().getText().toString());
                break;
            default:
                break;
        }



}

    private void sendMessage(String text) {
        Thread thread = new Thread(new Runnable(){
        String sendingWord = text;
            @Override
            public void run() {
                try
                {
                    sendToServer(sendingWord);
                    sendingWord = "";
                } catch (Exception e)
                {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    String hier2 =  errors.toString();
                    //Toast.makeText(getApplicationContext(), "Exception :" + hier2, Toast.LENGTH_LONG).show();
                }
                catch (Throwable th)
                {
                    StringWriter errors = new StringWriter();
                    th.printStackTrace(new PrintWriter(errors));
                    String hier3 =  errors.toString();
                    //Toast.makeText(getApplicationContext(), "Throwable :" + hier3, Toast.LENGTH_LONG).show();
                }
            }
        });

        thread.start();
    }

    private void sendCommand(String text) throws Throwable {
        Thread thread = new Thread(new Runnable(){
            String sendingWord = "39475801569025594152" + text;
            @Override
            public void run() {
                try
                {
                    sendToServer(sendingWord);
                    sendingWord = "";
                } catch (Throwable e)
                {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    String hier2 =  errors.toString();
                    //Toast.makeText(getApplicationContext(), "Exception :" + hier2, Toast.LENGTH_LONG).show();
                }//Toast.makeText(getApplicationContext(), "Throwable :" + hier3, Toast.LENGTH_LONG).show();

            }
        });


        thread.start();
    }
}

