package aplicacion.android.feli.httpprueba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class opciones extends AppCompatActivity {

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

        TextoUsuario.setText(MainActivity.Usuario);
        TextoContra.setText(MainActivity.Contra);
        SetSensAprox.setChecked(MainActivity.OnOffSensor);

    }

    public void SetUsuarioyContra(View view){

        MainActivity.Usuario = TextoUsuario.getText().toString();
        MainActivity.Contra = TextoContra.getText().toString();

        Intent intent = new Intent(opciones.this,MainActivity.class);
        startActivity(intent);

    }

    public  void SetSensor(View v){
        if(SetSensAprox.isChecked()){ MainActivity.OnOffSensor = true; }else MainActivity.OnOffSensor = false;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}

