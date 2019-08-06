package aplicacion.android.feli.httpprueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class MainActivity extends AppCompatActivity {

    private TextView DisplayEstadoLed;
    private TextView Minutos;
    private Toolbar toolbar;
    SeekBar BarraTimer;
    int numero = 1;
    String SinConexion = "Sin Conexion";
    String LightsOn = "Lights ON";
    String LightsOff = "Lights OFF";
    String URL = "http://192.168.0.28/";
    String Usuario = "";
    String Contra = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Minutos = findViewById(R.id.DispTimer);
        DisplayEstadoLed = findViewById(R.id.textView);
        BarraTimer = findViewById(R.id.BarraTimer);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        BarraTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numero = i+1;
                String display = Integer.toString(numero) + "min";
                Minutos.setText(display);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }//protected void onCreate(Bundle savedInstanceState)

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

        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this); // esto sirve para declarar el archivo preferencial, donde se guardan datos en la memoria
        Usuario = myPreference.getString("Usuario","unknown");
        Contra = myPreference.getString("Contra","unknown");

        String LedOn = "LED=ON";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL+LedOn).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                   final String myResponse = response.body().string();

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

        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this); // esto sirve para declarar el archivo preferencial, donde se guardan datos en la memoria
        Usuario = myPreference.getString("Usuario","unknown");
        Contra = myPreference.getString("Contra","unknown");

        String LedOff = "LED=OFF";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL+LedOff).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                DisplayEstadoLed.setText(SinConexion);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponse = response.body().string();

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
                   DisplayEstadoLed.setText(display);
                }
            }
        });


    }//public void SetTimer(View v)


}


//****************Si no te instala la aplicacion ya que te pone Installation failed with message Failed to finalize session : INSTALL_FAILED_INVALID_APK: **********//

//Build->Rebuild Proyect