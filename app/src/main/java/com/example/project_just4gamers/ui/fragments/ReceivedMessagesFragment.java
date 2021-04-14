package com.example.project_just4gamers.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.ui.adapters.MessageListAdapter;
import com.example.project_just4gamers.ui.adapters.OrdersListAdapter;

import java.util.ArrayList;

public class ReceivedMessagesFragment extends Fragment {

    private RecyclerView rv_receivedMessages;
    private TextView tvNoMessages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_received_messages, container, false);
        rv_receivedMessages = view.findViewById(R.id.rv_receivedMessages);
        tvNoMessages = view.findViewById(R.id.tv_no_messages_found);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getReceivedMessageList();
    }

    public void getReceivedMessageList(){
        new FirestoreManager().getReceivedMessageList(ReceivedMessagesFragment.this);
    }


    public void sucessGetReceivedMessages(ArrayList<Message> messages){
        if (messages.size() > 0){
            rv_receivedMessages.setVisibility(View.VISIBLE);
            tvNoMessages.setVisibility(View.GONE);

            rv_receivedMessages.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_receivedMessages.setHasFixedSize(true);

            MessageListAdapter adapter = new MessageListAdapter(requireActivity(), messages);
            rv_receivedMessages.setAdapter(adapter);
        } else {
            rv_receivedMessages.setVisibility(View.GONE);
            tvNoMessages.setVisibility(View.VISIBLE);
        }
    }


}