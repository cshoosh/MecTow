package com.example.mectow.ui.history;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.example.mectow.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>  implements Filterable {
    private List<HistoryModel> modellist;
    Context context;
    private List<HistoryModel> modelListAll;

    public HistoryAdapter(List<HistoryModel> modellist, Context context) {
        this.modellist = modellist;
        this.context = context;
        modelListAll = new ArrayList<>(modellist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_nav_history,parent ,false);
        return new HistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.time.setText(modellist.get(position).getTime());
        holder.catagory.setText(modellist.get(position).getCatagor());
        holder.field.setText(modellist.get(position).getField());
        holder.service.setText(modellist.get(position).getService());
        holder.cash.setText("PKR "+modellist.get(position).getCash() );

    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<HistoryModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty())
            {
                filteredList.addAll(modelListAll);
            }
            else {
                for(HistoryModel fm : modelListAll)
                {
                    if(fm.getCatagor().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        filteredList.add(fm);
                    }
                }
            }
            FilterResults filterResults= new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modellist.clear();
            modellist.addAll((Collection<? extends HistoryModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time , catagory , field , service ,cash;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            catagory = itemView.findViewById(R.id.catagory);
            field = itemView.findViewById(R.id.field);
            service = itemView.findViewById(R.id.service);
            cash = itemView.findViewById(R.id.cash);
        }
    }
}
