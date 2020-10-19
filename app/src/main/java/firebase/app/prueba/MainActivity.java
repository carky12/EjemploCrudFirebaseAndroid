package firebase.app.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import firebase.app.prueba.model.Persona;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "NOTICIAS";

    EditText txtNombre, txtApellido, txtEmail, txtPassword;
    ListView lstPersonas;

    FirebaseDatabase firebaseDB;
    DatabaseReference dbReference;

    private List<Persona> listaPersonas = new ArrayList<Persona>();
    ArrayAdapter<Persona> personaArrayAdapter;

    Persona personaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        lstPersonas = findViewById(R.id.lstPersonas);

        iniciarFirebase();
        listarDatos();
        notification();

        lstPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSeleccionada = (Persona) parent.getItemAtPosition((position));

                txtNombre.setText(personaSeleccionada.getNombre());
                txtApellido.setText(personaSeleccionada.getApellidos());
                txtEmail.setText(personaSeleccionada.getEmail());
                txtPassword.setText(personaSeleccionada.getPassword());
            }
        });

    }

    private void notification() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d(TAG, "Token de app: " + token);
                        Toast.makeText(MainActivity.this, "Token de app: " + token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void listarDatos() {
        dbReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPersonas.clear();

                for (DataSnapshot obj : snapshot.getChildren()) {
                    Persona p = obj.getValue(Persona.class);

                    listaPersonas.add(p);

                    personaArrayAdapter = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listaPersonas);
                    lstPersonas.setAdapter(personaArrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDB = FirebaseDatabase.getInstance();
        //firebaseDB.setPersistenceEnabled(true);
        dbReference = firebaseDB.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombre = txtNombre.getText().toString().trim();
        String apellido = txtApellido.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        switch (item.getItemId()) {

            case R.id.icon_add: {
                if (nombre.equals("") || apellido.equals("") || email.equals("") || password.equals("")) {
                    validacion();
                } else {
                    Persona per = new Persona();
                    per.setUid(UUID.randomUUID().toString());
                    per.setNombre(nombre);
                    per.setApellidos(apellido);
                    per.setEmail(email);
                    per.setPassword(password);

                    dbReference.child("Persona").child(per.getUid()).setValue(per);

                    Toast.makeText(this, "Agregado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
                break;
            }

            case R.id.icon_save: {
                if (nombre.equals("") || apellido.equals("") || email.equals("") || password.equals("")) {
                    validacion();
                } else {
                    Persona per = new Persona();
                    per.setUid(personaSeleccionada.getUid());
                    per.setNombre(nombre);
                    per.setApellidos(apellido);
                    per.setEmail(email);
                    per.setPassword(password);

                    dbReference.child("Persona").child(per.getUid()).setValue(per);

                    Toast.makeText(this, "Actualizado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
                break;
            }

            case R.id.icon_delete: {
                Persona per = new Persona();
                per.setUid(personaSeleccionada.getUid());

                dbReference.child("Persona").child(per.getUid()).removeValue();

                Toast.makeText(this, "Borrado con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                break;
            }

            default: break;

        }
        return true;
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void validacion() {

        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (nombre.equals("")) {
            txtNombre.setError("Required");
        } else if (apellido.equals("")) {
            txtApellido.setError("Required");
        } else if (email.equals("")) {
            txtEmail.setError("Required");
        } else if (password.equals("")) {
            txtPassword.setError("Required");
        }

    }
}