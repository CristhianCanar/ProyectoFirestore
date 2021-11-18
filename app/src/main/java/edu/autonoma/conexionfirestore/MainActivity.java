package edu.autonoma.conexionfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.User;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;

    EditText et_nombre, et_email;
    TextView tv_resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instancia conectada a la base de datos que hemos definido remotamente a la consola de firebase
        db = FirebaseFirestore.getInstance();
        //Declaracion de varaibles
        et_nombre       = findViewById(R.id.et_nombre);
        et_email        = findViewById(R.id.et_email);
        tv_resultado    = findViewById(R.id.tv_resultado);

    }

    public void guardar(View view) {
        String email = et_email.getText().toString();
        String nombre = et_nombre.getText().toString();

        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        map.put("nombre",nombre);
        db.collection("usuarios").document(email).set(map);
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
    }

    public void leer(View view) {
        List<User> listaUsers = new ArrayList<User>();
        db.collection("usuarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Información pbtenida de Firestore
                for (DocumentSnapshot snapshot : task.getResult()){
                    User user = new User(snapshot.getString("nombre"),snapshot.getString("email"));
                    listaUsers.add(user);
                }
                //Interpretacion de la informacion
                StringBuilder sb = new StringBuilder();
                sb.append("Usuarios: ");
                for (User usuario:listaUsers){
                    sb.append(usuario.getNombre()).append(" - ").append(usuario.getEmail());
                }

                tv_resultado.setText(sb.toString());
            }
        });
    }

    public void actualizar(View view) {
        String email = et_email.getText().toString();
        String nombre = et_nombre.getText().toString();

        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        map.put("nombre",nombre);
        db.collection("usuarios").document(email).set(map);
        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();

    }

    public void eliminar(View view) {
        String email = et_email.getText().toString();
        db.collection("usuarios").document(email).delete();
        Toast.makeText(this, "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show();
    }
}