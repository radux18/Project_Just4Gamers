package com.example.project_just4gamers.userInterface.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.userInterface.activities.MessageViewActivity;
import com.example.project_just4gamers.utilities.Constants;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Message> messageList;

    public MessageListAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(
                R.layout.item_message_layout,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Message model = messageList.get(position);

            if (holder instanceof MessageListAdapter.ViewHolder){
                    ((ViewHolder) holder).tvTitle.setText(model.getTitle());
                    ((ViewHolder) holder).tvDate.setText(model.getDate());
                    ((ViewHolder) holder).tvDescription.setText(model.getDescription());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageViewActivity.class);
                    intent.putExtra(Constants.getExtraSelectMessage(),model);

                    if (model.getReceiver_id().equals(new CloudFirestoreManager().getCurrentUserID())){
                        intent.putExtra(Constants.getExtraBoolean(), true);
                    }
                    context.startActivity(intent);
                }
            });
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvDescription;


        public ViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_message_title);
            tvDate = itemView.findViewById(R.id.tv_message_date);
            tvDescription = itemView.findViewById(R.id.tv_message_description);

        }
    }
}
