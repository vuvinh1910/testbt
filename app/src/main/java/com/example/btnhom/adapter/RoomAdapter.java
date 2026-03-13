package com.example.btnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btnhom.R;
import com.example.btnhom.model.Room;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private Context context;
    private List<Room> roomList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(Room room, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Room room, int position);
    }

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getRoomName());
        holder.tvRentalPrice.setText(String.format("%.0f VND", room.getRentalPrice()));
        holder.tvStatus.setText(room.getStatus());

        // Đặt màu cho status
        if (room.getStatus().equals("Còn trống")) {
            holder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.status_available, null));
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.white, null));
        } else {
            holder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.status_rented, null));
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.white, null));
        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(roomList.get(adapterPosition), adapterPosition);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemLongClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                onItemLongClickListener.onItemLongClick(roomList.get(adapterPosition), adapterPosition);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRentalPrice, tvStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            tvRentalPrice = itemView.findViewById(R.id.tv_rental_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }

    // Phương thức để cập nhật danh sách
    public void updateList(List<Room> newList) {
        this.roomList = newList;
        notifyDataSetChanged();
    }
}
