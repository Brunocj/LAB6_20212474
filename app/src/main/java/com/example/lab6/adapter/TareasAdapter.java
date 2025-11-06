package com.example.lab6.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.R;
import com.example.lab6.model.Tarea;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder> {

    private List<Tarea> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditar(Tarea tarea);
        void onEliminar(Tarea tarea);
    }

    public TareasAdapter(List<Tarea> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = lista.get(position);

        holder.tvTitulo.setText(tarea.getTitulo());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvFecha.setText("Fecha límite: " + sdf.format(tarea.getFechaLimite()));

        holder.checkEstado.setChecked(tarea.isEstado());

        holder.checkEstado.setOnCheckedChangeListener((buttonView, checked) -> {
            FirebaseFirestore.getInstance()
                    .collection("tareas")
                    .document(tarea.getId())
                    .update("estado", checked);
        });

        holder.itemView.setOnClickListener(v -> listener.onEditar(tarea));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onEliminar(tarea);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvFecha;
        CheckBox checkEstado;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloTarea);
            tvFecha = itemView.findViewById(R.id.tvFechaLimite);
            checkEstado = itemView.findViewById(R.id.checkEstado);
        }
    }
}
