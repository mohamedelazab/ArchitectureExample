package com.mobile.architecture.architectureexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.architecture.architectureexample.model.Note;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends ListAdapter<Note,NotesAdapter.NotesViewHolder> {

    private Context context;
    private OnItemClickListener listener;

    public NotesAdapter() {
        super(diffCallback);
    }

    public static final DiffUtil.ItemCallback<Note> diffCallback =new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())&& oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() ==newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        View v =LayoutInflater.from(context).inflate(R.layout.rv_notes,parent,false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvDescription.setText(getItem(position).getDescription());
        holder.tvPriority.setText(String.valueOf(getItem(position).getPriority()));
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle, tvDescription, tvPriority;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle =itemView.findViewById(R.id.tv_title);
            tvDescription =itemView.findViewById(R.id.tv_description);
            tvPriority =itemView.findViewById(R.id.tv_priority);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null&& getAdapterPosition() !=RecyclerView.NO_POSITION)
                    listener.onItemClick(getItem(getAdapterPosition()));
                }
            });
        }
    }

    public Note getNoteAt(int position){
        return getItem(position);
    }

    public interface OnItemClickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener =listener;
    }
}