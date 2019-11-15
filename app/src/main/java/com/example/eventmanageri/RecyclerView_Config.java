package com.example.eventmanageri;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;


public class RecyclerView_Config {
    private Context mContext;
    public EventsAdapter mEventsAdapter;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public void setConfig(RecyclerView recyclerView, Context context, List<Event> events, List<String> keys) {
        mContext = context;
        mEventsAdapter = new EventsAdapter(events, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mEventsAdapter);
    }

    class EventItemView extends RecyclerView.ViewHolder{
        private TextView mTitle, mType, mDate, mMemo, mPhoto, mVideo, mLocation, mShare;
        private String key, mEventId, mUserId;

        public EventItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false));

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
                    Intent intent = new Intent(mContext, EventViewActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("eventid",mEventId);
                    intent.putExtra("userid",mUserId);
                    intent.putExtra("title",mTitle.getText().toString());
                    intent.putExtra("type",mType.getText().toString());
                    intent.putExtra("date",mDate.getText().toString());
                    intent.putExtra("memo",mMemo.getText().toString());
                    intent.putExtra("photo",mPhoto.getText().toString());
                    intent.putExtra("video",mVideo.getText().toString());
                    intent.putExtra("location",mLocation.getText().toString());
                    intent.putExtra("share",mShare.getText().toString());

                    mContext.startActivity(intent);
                }
            });
        }

        // Event items (for EventListActivity)
        public void bind(Event event, String key) {
                mEventId = event.getEventId();
                mUserId = event.getUserId();
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

    class EventsAdapter extends RecyclerView.Adapter<EventItemView> implements Filterable {
        private List<Event> mEventList;
        private List<String> mKeys;
        // add
        private List<Event> mEventListFull;

        public EventsAdapter(List<Event> mEventList, List<String> mKeys) {
            this.mEventList = mEventList;
            this.mKeys = mKeys;
            // add
            mEventListFull = new ArrayList<>(mEventList);
        }

        @NonNull
        @Override
        public EventItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EventItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EventItemView holder, int position) {
            holder.bind(mEventList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mEventList.size();
        }

        // add
        @Override
        public Filter getFilter() {
            return eventFilter;
        }

        private Filter eventFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Event> filterList = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filterList.addAll(mEventListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Event event : mEventListFull) {
                        if (event.getTitle().toLowerCase().contains(filterPattern)) {
                            filterList.add(event);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filterList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mEventListFull.clear();
                mEventListFull.addAll((Collection<? extends Event>) results.values);
                notifyDataSetChanged();

            }
        };
    }
}
