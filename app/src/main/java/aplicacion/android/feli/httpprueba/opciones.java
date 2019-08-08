package aplicacion.android.feli.httpprueba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class opciones extends AppCompatActivity {

    private String Usuario = "";
    private String Contra = "";
    private EditText TextoUsuario;
    private EditText TextoContra;
    private SharedPreferences myPreference;
    private SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        TextoUsuario = findViewById(R.id.UsuarioPT);
        TextoContra = findViewById(R.id.ContraPT);

        myPreference = PreferenceManager.getDefaultSharedPreferences(opciones.this); // esto sirve para declarar el archivo preferencial, donde se guardan datos en la memoria
        Usuario = myPreference.getString("Usuario","unknown");
        Contra = myPreference.getString("Contra","unknown");

        TextoUsuario.setText(Usuario);
        TextoContra.setText(Contra);

    }

    public void SetUsuarioyContra(View view){

        myEditor = myPreference.edit();

        myEditor.putString("Usuario", TextoUsuario.getText().toString());
        myEditor.apply();

        myEditor.putString("Usuario", TextoUsuario.getText().toString());
        myEditor.apply();

        Intent intent = new Intent(opciones.this,MainActivity.class);
        intent.putExtra("usuario",Usuario);
        intent.putExtra("contra",Contra);
        startActivity(intent);

    }
}

