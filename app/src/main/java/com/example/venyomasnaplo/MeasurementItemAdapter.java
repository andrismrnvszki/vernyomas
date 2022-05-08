package com.example.venyomasnaplo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class MeasurementItemAdapter extends RecyclerView.Adapter<MeasurementItemAdapter.ViewHolder> implements Filterable {
    private final static String LOG_TAG = MeasurementItemAdapter.class.getName();
    private  ArrayList<MeasurementItem> mMeasurementItemsData = new ArrayList<>();
    private  ArrayList<MeasurementItem> mMeasurementItemsDataAll  = new ArrayList<>();
    private Context mContext;
    private int lastPos = -1;

    MeasurementItemAdapter(Context context, ArrayList<MeasurementItem> itemsData){
        this.mMeasurementItemsData = itemsData;
        this.mMeasurementItemsDataAll= itemsData;
        this.mContext = context;

    }

    @Override
    public MeasurementItemAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "Clicked the shit");
        return new ViewHolder(LayoutInflater.from(mContext)
                                .inflate(R.layout.list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MeasurementItem currentItem = mMeasurementItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPos) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPos = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return this.mMeasurementItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return MeasurementFilter;
    }

    private Filter MeasurementFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<MeasurementItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0){
                results.count = mMeasurementItemsDataAll.size();
                results.values = mMeasurementItemsDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(MeasurementItem item: mMeasurementItemsDataAll){
                    if(String.valueOf(item.getSystole()).equals(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mMeasurementItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mSys;
        private TextView mDias;
        private TextView mPulse;
        private TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);

            mSys = itemView.findViewById(R.id.Sys);
            mDias = itemView.findViewById(R.id.Dias);
            mPulse = itemView.findViewById(R.id.Pulse);
            mDate = itemView.findViewById(R.id.Date);

            itemView.findViewById(R.id.editMeas).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, "Clicked the shit");
                }
            });

        }

        public void bindTo(MeasurementItem currentItem) {

            mSys.setText(String.valueOf(currentItem.getSystole()));
            mDias.setText(String.valueOf(currentItem.getDiastole()));
            mPulse.setText(String.valueOf(currentItem.getPulse()));
            mDate.setText(String.valueOf(currentItem.getDate()));

            //itemView.findViewById(R.id.delete).setOnClickListener(view -> ((MeasurementsListActivity)mContext).deleteItem(currentItem));
            itemView.findViewById(R.id.deleteMeas).setOnClickListener(view -> ((MeasurementsListActivity)mContext).deleteItem(currentItem));
            itemView.findViewById(R.id.editMeas).setOnClickListener(view -> ((MeasurementsListActivity)mContext).editItem(currentItem));


        }
    }
}
