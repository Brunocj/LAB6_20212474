package com.example.lab6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ResumenFragment extends Fragment {

    private AnyChartView grafico;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        grafico = view.findViewById(R.id.graficoTareas);
        db = FirebaseFirestore.getInstance();

        cargarDatos();

        return view;
    }

    private void cargarDatos() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("tareas")
                .whereEqualTo("uidUsuario", uid)
                .addSnapshotListener((value, error) -> {

                    int total = 0, completadas = 0, pendientes = 0;

                    if (value != null) {
                        total = value.size();
                        for (DocumentSnapshot d : value.getDocuments()) {
                            Boolean done = d.getBoolean("estado");
                            if (done == null) done = d.getBoolean("completada");
                            if (Boolean.TRUE.equals(done)) completadas++;
                        }
                        pendientes = total - completadas;
                    }

                    mostrarGrafico(completadas, pendientes);


                });
    }

    private void mostrarGrafico(int completadas, int pendientes) {

        Pie pie = AnyChart.pie();

        List<DataEntry> datos = new ArrayList<>();
        datos.add(new ValueDataEntry("Completadas", completadas));
        datos.add(new ValueDataEntry("Pendientes", pendientes));

        pie.data(datos);

        pie.title("Estado de tus tareas");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title().text("Detalles");
        pie.legend().position("bottom");

        grafico.setChart(pie);
    }
}
