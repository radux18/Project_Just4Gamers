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

import java.util.ArrayList;


public class SentMessagesFragment extends Fragment {

    private RecyclerView rv_sentMessages;
    private TextView tvNoMessages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sent_messages, container, false);
        rv_sentMessages = view.findViewById(R.id.rv_sentMessages);
        tvNoMessages = view.findViewById(R.id.tv_no_messages_found);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSentMessageList();
    }

    private void getSentMessageList() {
        new FirestoreManager().getSentMessageList(SentMessagesFragment.this);
    }

    public void successGetSentMessageFromFirestore(ArrayList<Message> messages) {
        if (messages.size() > 0){
            rv_sentMessages.setVisibility(View.VISIBLE);
            tvNoMessages.setVisibility(View.GONE);

            rv_sentMessages.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_sentMessages.setHasFixedSize(true);

            MessageListAdapter adapter = new MessageListAdapter(requireActivity(),messages);
            rv_sentMessages.setAdapter(adapter);
        } else {
            rv_sentMessages.setVisibility(View.GONE);
            tvNoMessages.setVisibility(View.VISIBLE);
        }
    }
}