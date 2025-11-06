package com.example.lab6;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AgregarTareaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion, etFecha;
    private Button btnGuardar;
    private FirebaseFirestore db;
    private String idTarea = null;
    private Date fechaSeleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFechaLimite);
        btnGuardar = findViewById(R.id.btnGuardar);

        db = FirebaseFirestore.getInstance();
        idTarea = getIntent().getStringExtra("id");

        etFecha.setOnClickListener(v -> mostrarDatePicker());

        if (idTarea != null) {
            db.collection("tareas").document(idTarea).get().addOnSuccessListener(d -> {
                etTitulo.setText(d.getString("titulo"));
                etDescripcion.setText(d.getString("descripcion"));

                Date fecha = d.getDate("fechaLimite");
                if (fecha != null) {
                    fechaSeleccionada = fecha;
                    etFecha.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha));
                }
            });
        }

        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    calendar.set(year, month, day);
                    fechaSeleccionada = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    etFecha.setText(sdf.format(fechaSeleccionada));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        picker.show();
    }

    private void guardar() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (titulo.isEmpty() || fechaSeleccionada == null) {
            Toast.makeText(this, "Complete título y fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("titulo", titulo);
        data.put("descripcion", descripcion);
        data.put("fechaLimite", fechaSeleccionada);
        data.put("estado", false);
        data.put("uidUsuario", FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (idTarea == null) {
            db.collection("tareas").add(data);
        } else {
            db.collection("tareas").document(idTarea).update(data);
        }

        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}
