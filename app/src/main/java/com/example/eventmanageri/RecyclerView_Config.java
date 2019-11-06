package com.example.eventmanageri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.content.Intent;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerView_Config {
    private Context mContext;
    private EventsAdapter mEventsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Event> events, List<String> keys) {
        mContext = context;
        mEventsAdapter = new EventsAdapter(events, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mEventsAdapter);
    }

    class EventItemView extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mType;
        private TextView mDate;
        private TextView mMemo;
        private TextView mPhoto;
        private TextView mVideo;
        private TextView mPhotoUrl;
        private TextView mLocation;
        private TextView mShare;

        private String key;

        public EventItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false));

            mTitle = (TextView) itemView.findViewById(R.id.title_textView);
            mType = (TextView) itemView.findViewById(R.id.type_txtView);
            mDate = (TextView) itemView.findViewById(R.id.date_viewTxt);
            mMemo = (TextView) itemView.findViewById(R.id.memo_txtView);
            //mPhotoUrl = (TextView) itemView.findViewById(R.id.Photo_txtView);
            mPhoto = (TextView) itemView.findViewById(R.id.Photo_txtView);
            mVideo = (TextView) itemView.findViewById(R.id.Video_txtView);
            mLocation = (TextView) itemView.findViewById(R.id.Location_txtView);
            mShare = (TextView) itemView.findViewById(R.id.Share_textView);

            // Update Event
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, EventModActivity.class);

                    intent.putExtra("key",key);
                    intent.putExtra("title",mTitle.getText().toString());
                    intent.putExtra("title",mDate.getText().toString());
                    intent.putExtra("memo",mMemo.getText().toString());
                    intent.putExtra("photo",mPhoto.getText().toString());
                    intent.putExtra("video",mVideo.getText().toString());
                    intent.putExtra("location",mLocation.getText().toString());
                    intent.putExtra("share",mShare.getText().toString());
                    intent.putExtra("type",mType.getText().toString());

                    mContext.startActivity(intent);
                }
            });

             */
        }

        // List event items (for EventListActivity)
        public void bind(Event event, String key) {
            mTitle.setText(event.getTitle());
            mType.setText(event.getType());
            mDate.setText(event.getDate());
            mMemo.setText(event.getMemo());
            /*
            mPhoto.setText(event.getPhoto());
            mVideo.setText(event.getVideo());
            mLocation.setText(event.getLocation());
            mShare.setText(event.getShare());
            */
        }
    }
    class EventsAdapter extends RecyclerView.Adapter<EventItemView> {
        private List<Event> mEventList;
        private List<String> mKeys;

        public EventsAdapter(List<Event> mEventList, List<String> mKeys) {
            this.mEventList = mEventList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public EventItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EventItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EventItemView holder, int position) {
            holder.bind(mEventList.get(position), mKeys.get(position));
            // picasso
        }

        @Override
        public int getItemCount() {
            return mEventList.size();
        }
    }

}
