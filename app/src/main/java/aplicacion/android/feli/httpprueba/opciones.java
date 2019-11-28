package aplicacion.android.feli.httpprueba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class opciones extends MainActivity {

    private EditText TextoUsuario;
    private EditText TextoContra;
    private SharedPreferences myPreference;
    private SharedPreferences.Editor myEditor;
    private Switch SetSensAprox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        TextoUsuario = findViewById(R.id.UsuarioPT);
        TextoContra = findViewById(R.id.ContraPT);
        SetSensAprox = findViewById(R.id.SwitchSensor);

        TextoUsuario.setText(Usuario);
        TextoContra.setText(Contra);
        SetSensAprox.setChecked(OnOffSensor);


    }

    public void SetUsuarioyContra(View view){

        Usuario = TextoUsuario.getText().toString();
        Contra = TextoContra.getText().toString();

        Intent intent = new Intent(opciones.this,MainActivity.class);
        startActivity(intent);

    }

    public  void SetSensor(View v){

        if(SetSensAprox.isChecked()){
            OnOffSensor = true;
            sensorManager.registerListener(sensorEventListener, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }else {
            OnOffSensor = false;
            sensorManager.unregisterListener(sensorEventListener);
        }
    }


}

