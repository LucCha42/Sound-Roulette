package fr.chardonnet_rakotoanosy.soundroulette;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder> {

    private ArrayList<Sound> sounds;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class SoundHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public SoundHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.sound_name);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public SoundAdapter(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        return new SoundHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
        holder.name.setText(sounds.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }
}
