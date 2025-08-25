package com.example.treffuns.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treffuns.Activities.ChatActivity;
import com.example.treffuns.R;
import com.example.treffuns.model.TreffenModel;
import com.example.treffuns.utils.AndroidUtil;
import com.example.treffuns.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SucheTreffenRecyclerAdapter extends FirestoreRecyclerAdapter<TreffenModel, SucheTreffenRecyclerAdapter.UserModelViewHolder> {

    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SucheTreffenRecyclerAdapter(@NonNull FirestoreRecyclerOptions<TreffenModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull TreffenModel model) {
        holder.meetingname.setText(model.getTreffenName());
        if (model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.meetingname.setText(model.getTreffenName() + "(Mein)");
        }

        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passTreffenModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.suche_treffen_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView meetingname;
        public UserModelViewHolder(@NonNull View itemView){
            super(itemView);
            meetingname = itemView.findViewById(R.id.meeting_name);
        }
    }
}
