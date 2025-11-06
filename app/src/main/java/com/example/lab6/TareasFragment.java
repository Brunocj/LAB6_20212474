package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.adapter.TareasAdapter;
import com.example.lab6.model.Tarea;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TareasFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<Tarea> listaTareas = new ArrayList<>();
    private TareasAdapter adapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);

        recyclerView = view.findViewById(R.id.recyclerTareas);
        fab = view.findViewById(R.id.fabAgregar);
        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TareasAdapter(listaTareas, new TareasAdapter.OnItemClickListener() {
            @Override
            public void onEditar(Tarea tarea) {
                Intent i = new Intent(getContext(), AgregarTareaActivity.class);
                i.putExtra("id", tarea.getId());
                startActivity(i);
            }

            @Override
            public void onEliminar(Tarea tarea) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar tarea")
                        .setMessage("¿Seguro que deseas eliminar esta tarea?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            db.collection("tareas")
                                    .document(tarea.getId())
                                    .delete();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);

        cargarTareas();

        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AgregarTareaActivity.class)));

        return view;
    }

    private void cargarTareas() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("tareas")
                .whereEqualTo("uidUsuario", uid)
                .addSnapshotListener((value, error) -> {
                    listaTareas.clear();
                    for (DocumentSnapshot d : value.getDocuments()) {
                        Tarea tarea = d.toObject(Tarea.class);
                        tarea.setId(d.getId());
                        listaTareas.add(tarea);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
