package com.example.ledmatrix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private Button help1;
    public TextInputLayout textInput;
    boolean connected = false;
    public static String sendingWord = "";
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        button1 = (Button) findViewById(R.id.button1);
        help1 = findViewById(R.id.help);
        button1.setOnClickListener(this);
        help1.setOnClickListener(this);
        textInput = (TextInputLayout) findViewById(R.id.editText1);

    }

    // Method to send Sting to UDP Server
    public void sendToServer(String nachricht) throws SocketException, IOException, Exception, Throwable
    {
        // Mit this muss ein Context-Objekt übergeben werden, wie bspw. eine Activity-Instanz
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int port = Integer.parseInt(sharedPrefs.getString("port_preference", null));

        InetAddress ipaddr = InetAddress.getByName(sharedPrefs.getString("ip_preference", null));

        DatagramSocket clientSocket = new DatagramSocket();


        byte[] sendData    = new byte[1024];

        sendData = nachricht.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipaddr, port);

        clientSocket.send(sendPacket);
        clientSocket.close();
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));

                return true;
            default:
                // Wenn wir hier ankommen, wurde eine unbekannt Aktion erfasst.
                // Daher erfolgt der Aufruf der Super-Klasse, die sich darum kümmert.
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public void onClick(View v) {
        //Toast.makeText(getApplicationContext(), "In editText steht : " + stringEditText, Toast.LENGTH_SHORT).show();

        switch (v.getId()) {
            case R.id.button1:
                sendMessage(textInput.getEditText().getText().toString());
                break;
            case R.id.help:
                goToUrl("https://github.com/Frostplexx/LEDMatrix-App/blob/master/README.md");
            default:
                break;
        }


    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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

                }

            }
        });


        thread.start();
    }
}

