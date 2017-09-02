package com.example.faridfedora.knowledgeshare;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by faridfedora on 5/29/17.
 */

public class PostRecyclerView extends RecyclerView.Adapter<PostRecyclerView.PostViewHolder> {
    ArrayList<PostModel> postModelArrayList;
    RecyclerViewClickListener itemListener;
    public PostRecyclerView(ArrayList<PostModel> postModelArrayList,RecyclerViewClickListener itemListener) {
        this.postModelArrayList=postModelArrayList;
        this.itemListener=itemListener;
    }

    @Override
    public PostRecyclerView.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_post,parent,false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostRecyclerView.PostViewHolder holder, int position) {
        holder.subjectTextView.setText(postModelArrayList.get(position).getSubject());
        holder.tagTextView.setText(postModelArrayList.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subjectTextView;
        TextView tagTextView;
        public PostViewHolder(View itemView) {
            super(itemView);
            subjectTextView= (TextView) itemView.findViewById(R.id.subjectTextView);
            tagTextView= (TextView) itemView.findViewById(R.id.tagTextView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewItemClicked(v,postModelArrayList.get(getAdapterPosition()).getId());
        }
    }
}
