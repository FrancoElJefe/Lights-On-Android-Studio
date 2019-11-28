package aplicacion.android.feli.httpprueba;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import xyz.hanks.library.bang.SmallBangView;

public class MainActivity extends AppCompatActivity {

    public  String CHANNEL_ID =  "Notificacion";
    private TextView DisplayEstadoLed;
    private Toolbar toolbar;
    SeekBar BarraTimer;
    int numero = 1;
    String SinConexion = "Sin Conexion";
    String LightsOn = "Lights ON";
    String LightsOff = "Lights OFF";
    String URL = "http://192.168.0.28/";
    public static String Usuario;
    public static String Contra;
    int estado = 0;
    boolean OnOff = false;
    public static boolean OnOffSensor;

    private SensorManager sensorManager;
    private Sensor proximity;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



/**************************** animacion **********************************************/
        final SmallBangView BtnTimmer = findViewById(R.id.btn_timmer);
        BtnTimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetTimer(v);

                if (BtnTimmer.isSelected()) {
                    BtnTimmer.setSelected(false);
                } else {
                    BtnTimmer.setSelected(true);
                    BtnTimmer.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                }
            }
        });

/************************************************************************************/

        DisplayEstadoLed = findViewById(R.id.textView);
        BarraTimer = findViewById(R.id.BarraTimer);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        /*
        SharedPreferences myPreferencest = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor myEditor = myPreferencest.edit();
        myEditor.putBoolean("EstadoSwitch",OnOffSensor);
        myEditor.apply();


        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this); // esto sirve para declarar el archivo preferencial, donde se guardan datos en la memoria
        Usuario = myPreferences.getString("Usuario","unknown");
        Contra = myPreferences.getString("Contra","unknown");
        if(myPreferences.contains("EstadoSwitch"))
        OnOffSensor = myPreferences.getBoolean("EstadoSwitch",false);
*/

        BarraTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numero = i+1;
                String display = "Set Timer en " + Integer.toString(numero) + "min";
                DisplayEstadoLed.setText(display);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /******************** Seteo el sensor de aproximacion*******************************/

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {// Do something with this sensor data.
                Float distance = sensorEvent.values[0];

                if(distance == 0){
                    if(!OnOff){
                        EncenderLuzSensorDeAproximacion();
                    }else {
                        ApagarLuzSensorDeAproximacion();
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }

        };


        if(OnOffSensor){ sensorManager.registerListener(sensorEventListener, proximity, SensorManager.SENSOR_DELAY_NORMAL);//activo el sensor
        }else if(!OnOffSensor){sensorManager.unregisterListener(sensorEventListener);}//desactivo el sensor


        /***********************************************************************************/



    }//protected void onCreate(Bundle savedInstanceState)


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Settings:
                Intent intent = new Intent(MainActivity.this,opciones.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void EncenderLuz(View v) {

        OnOff = true;

        String LedOn = "LED=ON";

        Request request = null;
        OkHttpClient client = new OkHttpClient();
        request = new Request.Builder().url(URL+LedOn+"/"+Usuario+"/"+Contra).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                   //final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayEstadoLed.setText(LightsOn);
                        }
                    });
                }
            }
        });

    }//public void EncenderLuz(View v)

    public void ApagarLuz(View v){

        OnOff = false;

        String LedOff = "LED=OFF";

        Request request = null;
        OkHttpClient client = new OkHttpClient();
        request = new Request.Builder().url(URL+LedOff+"/"+Usuario+"/"+Contra).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    //final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          DisplayEstadoLed.setText(LightsOff);
                        }
                    });
                }
            }
        });


    }//public void ApagarLuz(View v)

    public void SetTimer(View v){
        Request request = null;

        OkHttpClient client = new OkHttpClient();

        if (numero == 1){
            request = new Request.Builder().url("http://192.168.0.28/1min").build();
        }else if (numero == 2){
            request = new Request.Builder().url("http://192.168.0.28/2min").build();
        }else if(numero == 3){
            request = new Request.Builder().url("http://192.168.0.28/3min").build();
        }else if(numero == 4){
            request = new Request.Builder().url("http://192.168.0.28/4min").build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {//si no se puede realizar la Request
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    String display = "Lights OFF en " + Integer.toString(numero) + "minutos";
                    NotificacionTimer(display);//notificacion para avisar que se seteo el timer
                }
            }
        });


    }//public void SetTimer(View v)

    private void NotificacionTimer(String display){

        Intent intent = new Intent(MainActivity.this, MainActivity.class);//accion que se realiza cuando toco la notificacion
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,  CHANNEL_ID)
                .setSmallIcon(R.mipmap.lights_round)
                .setContentTitle("Timer")
                .setContentText(display)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)//le setie la accion que realiza si apretas la notificacion
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.App_LightsON);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(22, builder.build());

    }

    public void EncenderLuzSensorDeAproximacion(){

        OnOff = true;

        String LedOn = "LED=ON";

        Request request = null;
        OkHttpClient client = new OkHttpClient();
        request = new Request.Builder().url(URL+LedOn+"/"+Usuario+"/"+Contra).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    //final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayEstadoLed.setText(LightsOn);
                        }
                    });
                }
            }
        });

    }


    public void ApagarLuzSensorDeAproximacion(){

        OnOff = false;
        String LedOff = "LED=OFF";

        Request request = null;
        OkHttpClient client = new OkHttpClient();
        request = new Request.Builder().url(URL+LedOff+"/"+Usuario+"/"+Contra).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    //final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayEstadoLed.setText(LightsOff);
                        }
                    });
                }
            }
        });

    }


}


//****************Si no te instala la aplicacion ya que te pone Installation failed with message Failed to finalize session : INSTALL_FAILED_INVALID_APK: **********//

//Build->Rebuild Proyect