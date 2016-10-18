package materialtest.example.user.materialtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import materialtest.example.user.materialtest.R;
import materialtest.example.user.materialtest.activity.SavedJobsActivity;
import materialtest.example.user.materialtest.activity.SavedJobsDB;
import materialtest.example.user.materialtest.activity.SubActivity;
import materialtest.example.user.materialtest.pojo.Plants;

public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.MyViewHolder> implements Filterable {

    private ArrayList<Plants> listPlants = new ArrayList<>();
    private ArrayList<Plants> filteredList = new ArrayList<>();
    private LayoutInflater inflater;
    private FriendFilter friendFilter;
    public Context context;
    private SavedJobsDB myDb;

    public AdapterBoxOffice(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public void setPlantList(ArrayList<Plants> listPlants) {
        this.listPlants = listPlants;
        this.filteredList = listPlants;
        getFilter();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.boxoffice_custom_row, parent, false);// inflate the custom row
        return new MyViewHolder(view); //give it to a holder to avoid finding id every time

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {//fill data item

        Plants currentPlant = filteredList.get(position);//get information of the object from the position of the object

        //holder.id.setText(currentPlant.getId().toString());
        holder.genus.setText(currentPlant.getGenus());
        holder.species.setText(currentPlant.getSpecies());
        holder.cultivar.setText(currentPlant.getCultivar());
        //holder.common.setText(currentPlant.getCommon());
        //holder.icon.setImageResource();

        holder.setClickListener(new ClickListener() {

            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if (isLongClick) {

                    myDb = new SavedJobsDB(context);
                    myDb.open();

                    Plants longclicked = filteredList.get(position);
                    //get name by parsing name and pass to variable actor
                    String jobname = "";
                    String companyname = longclicked.getGenus();
                    String qualification = longclicked.getSpecies();
                    String days = longclicked.getCultivar();
                    //dummy results
                    String time = "";
                    String location = "";
                    String salary = "";
                    String deadline = "";
                    String description = "";
                    String requirements = "";
                    String benefits = "";

                    myDb.insertRow(jobname, companyname, qualification, days, time, location, salary, deadline, description, requirements, benefits);

                    Toast.makeText(context, days, Toast.LENGTH_SHORT).show();
                    myDb.close();

                } else {
                    Plants clickedPlant = filteredList.get(position);
                    //String id = (clickedPlant.getId().toString());
                    String genus = (clickedPlant.getGenus());
                    String species = (clickedPlant.getSpecies());
                    String cultivar = (clickedPlant.getCultivar());
                    //String common = (clickedPlant.getCommon());

                    //start new activity
                    Intent intent = new Intent(context, SubActivity.class);
                    Bundle extras = new Bundle();
                    //extras.putString("id", id);
                    extras.putString("city", genus);
                    extras.putString("place", species);
                    extras.putString("station", cultivar);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView id;
        private TextView genus;
        private TextView species;
        private TextView cultivar;
        private TextView common;
        private ImageView icon;
        private ClickListener clickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            //id = (TextView) itemView.findViewById(R.id.tvName);
            genus = (TextView) itemView.findViewById(R.id.tv_JobName);
            species = (TextView) itemView.findViewById(R.id.tv_JobLocation);
            cultivar = (TextView) itemView.findViewById(R.id.tv_JobDate);
            //common = (TextView) itemView.findViewById(R.id.tvDescriptionn);
            icon = (ImageView) itemView.findViewById(R.id.iv_JobLogo);

        }

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public interface ClickListener {
        void onClick(View v, int position, boolean isLongClick);

    }

    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Plants> tempList = new ArrayList<>();

                // search content in friend list
                for (Plants user : listPlants) {
                    if (user.getCommon().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            user.getGenus().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            user.getCultivar().toLowerCase().contains(constraint.toString().toLowerCase())
                            ) {
                        tempList.add(user);

                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
                Log.e("values", filterResults.values.toString());

            } else {
                filterResults.count = listPlants.size();
                filterResults.values = listPlants;
                Log.e("no", filterResults.values.toString());

            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         *
         * @param constraint text
         * @param results    filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Plants>) results.values;
            Log.e("out", filteredList.toString());
            Log.e("out2", listPlants.toString());
            notifyDataSetChanged();
        }
    }

}
