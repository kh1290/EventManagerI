package com.example.eventmanageri.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanageri.Activities.EventViewActivity;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventItemView> {
    private List<Event> mEventList;


    public EventAdapter(List<Event> mEventList) {
        this.mEventList = mEventList;
    }

    class EventItemView extends RecyclerView.ViewHolder{
        private TextView mTitle, mType, mDate, mMemo, mPhoto, mVideo, mLocation, mShare;
        private String key, mEventId, mUserId, mEmail;

        public EventItemView(final ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false));
            mTitle = (TextView) itemView.findViewById(R.id.title_textView);
            mType = (TextView) itemView.findViewById(R.id.type_txtView);
            mDate = (TextView) itemView.findViewById(R.id.date_viewTxt);
            mMemo = (TextView) itemView.findViewById(R.id.memo_txtView);
            mPhoto = (TextView) itemView.findViewById(R.id.Photo_txtView);
            mVideo = (TextView) itemView.findViewById(R.id.Video_txtView);
            mLocation = (TextView) itemView.findViewById(R.id.Location_txtView);
            mShare = (TextView) itemView.findViewById(R.id.Share_textView);

            // View Event Detail
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Put Data to EventViewActivity
                    Intent intent = new Intent(parent.getContext(), EventViewActivity.class);
                    // intent.putExtra("key",key);
                    intent.putExtra("eventid",mEventId);
                    intent.putExtra("userid",mUserId);
                    intent.putExtra("email",mEmail);
                    intent.putExtra("title",mTitle.getText().toString());
                    intent.putExtra("type",mType.getText().toString());
                    intent.putExtra("date",mDate.getText().toString());
                    intent.putExtra("memo",mMemo.getText().toString());
                    intent.putExtra("photo",mPhoto.getText().toString());
                    intent.putExtra("video",mVideo.getText().toString());
                    intent.putExtra("location",mLocation.getText().toString());
                    intent.putExtra("share",mShare.getText().toString());
                    parent.getContext().startActivity(intent);
                }
            });
        }

        // Define event items (for EventListActivity)
        public void bind(Event event) { //, String key
            mEventId = event.getEventId();
            mUserId = event.getUserId();
            mEmail = event.getEmail();
            mTitle.setText(event.getTitle());
            mType.setText(event.getType());
            mDate.setText(event.getDate());
            mMemo.setText(event.getMemo());
            mPhoto.setText(event.getPhoto());
            mVideo.setText(event.getVideo());
            mLocation.setText(event.getLocation());
            mShare.setText(event.getShare());
        }
    }

    @NonNull
    @Override
    public EventItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventItemView(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventItemView holder, int position) {
        holder.bind(mEventList.get(position));
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    // Filter for event search (called by EventListActivity)
    public void setFilter (List<Event> listEvent) {
        mEventList = new ArrayList<>();
        mEventList.addAll(listEvent);
        notifyDataSetChanged();
    }
}