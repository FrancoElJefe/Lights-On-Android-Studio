package aplicacion.android.feli.httpprueba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
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
    SeekBar BarraTimer;
    int numero = 1;
    String SinConexion = "Sin Conexion";
    String LightsOn = "Lights ON";
    String LightsOff = "Lights OFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Minutos = findViewById(R.id.DispTimer);
        DisplayEstadoLed = findViewById(R.id.textView);
        BarraTimer = findViewById(R.id.BarraTimer);

        BarraTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numero = i+1;
                String display = Integer.toString(numero) + "m";
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

    public void EncenderLuz(View v) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.0.28/LED=ON").build();
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
                           char [] array = myResponse.toCharArray(); //convierto el string en array de caracteres asi lo puedo recorrer

                           for (int i = 0 ; i < array.length;i++){ //recorro el array hasta encontrar la 'O' mayuscula y luego me fijo si en el siguente
                               if (array[i] == 'O'){               //caracter hay una 'N' o 'F'
                                   if (array[i+1] == 'N'){
                                       DisplayEstadoLed.setText(LightsOn);
                                       break;
                                   }else if (array[i+1] == 'F'){
                                       DisplayEstadoLed.setText(LightsOff);
                                       break;
                                   }
                               }
                           }
                        }
                    });
                }
            }
        });

    }//public void EncenderLuz(View v)

    public void ApagarLuz(View v){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.0.28/LED=OFF").build();
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
                            char [] array = myResponse.toCharArray(); //convierto el string en array de caracteres asi lo puedo recorrer

                            for (int i = 0 ; i < array.length;i++){ //recorro el array hasta encontrar la 'O' mayuscula y luego me fijo si en el siguente
                                if (array[i] == 'O'){               //caracter hay una 'N' o 'F'
                                    if (array[i+1] == 'N'){
                                        DisplayEstadoLed.setText(LightsOn);
                                        break;
                                    }else if (array[i+1] == 'F'){
                                        DisplayEstadoLed.setText(LightsOff);
                                        break;
                                    }
                                }
                            }
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