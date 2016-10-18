package materialtest.example.user.materialtest.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import materialtest.example.user.materialtest.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    List<FilterCustomRow> data = Collections.emptyList();
    public static String location = "All";
    public static String industry = "";
    public static String position = "";

    public FilterAdapter(Context context, List<FilterCustomRow> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.filter_custom_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        FilterCustomRow current = data.get(position);
        if (position ==0) {
            holder.desc.setText(current.desc);
            holder.selected.setText(location);
            holder.photoId.setImageResource(current.photoId);
        }else{
            holder.desc.setText(current.desc);
            holder.selected.setText(current.selected);
            holder.photoId.setImageResource(current.photoId);
        }
        holder.setClickListener(new MyViewHolder.ClickListener() {

            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    locationNameDialog();
                } else if (position == 1) {
                    industryNameDialog();
                } else {
                    positionNameDialog();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView desc;
        TextView selected;
        ImageView photoId;
        private ClickListener clickListener;

        public interface ClickListener {
            void onClick(View v, int position);
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            desc = (TextView) itemView.findViewById(R.id.tv_desc);
            selected = (TextView) itemView.findViewById(R.id.tv_selected);
            photoId = (ImageView) itemView.findViewById(R.id.iv_filter_Logo);
        }

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

    private void locationNameDialog() {

        final ArrayList<Integer> arrayLocation = new ArrayList<>();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final String[] locations = {"All", "Redbud", "Pawpaw", "Maple", "Korean", "Rose", "China", "White"};

        dialogBuilder.setTitle("Select your Location(s)");
        dialogBuilder.setMultiChoiceItems(locations, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    arrayLocation.add(which);
                } else if (arrayLocation.contains(which)) {
                    arrayLocation.remove(Integer.valueOf(which));
                }
            }
        });

        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    location = "";

                for (int i = 0; i < arrayLocation.size(); i++) {
                    location += locations[arrayLocation.get(i)];
                }
                Toast.makeText(context, location, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialogLocation = dialogBuilder.create();
        dialogLocation.show();
    }

    private void industryNameDialog() {

        final ArrayList<Integer> arrayLocation = new ArrayList<>();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final String[] industries = {"All","Redbud", "Pawpaw", "Maple", "Korean", "Rose", "China", "White"};

        dialogBuilder.setTitle("Select your Industry(s)");
        dialogBuilder.setMultiChoiceItems(industries, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    arrayLocation.add(which);
                } else if (arrayLocation.contains(which)) {
                    arrayLocation.remove(Integer.valueOf(which));
                }
            }
        });

        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < arrayLocation.size(); i++) {
                    industry += industries[arrayLocation.get(i)];
                }
                Toast.makeText(context, industry, Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialogLocation = dialogBuilder.create();
        dialogLocation.show();
    }

    private void positionNameDialog() {

        final ArrayList<Integer> arrayLocation = new ArrayList<>();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final String[] positions = {"All", "Redbud", "Pawpaw", "Maple", "Korean", "Rose", "China", "White"};

        dialogBuilder.setTitle("Select your Position(s)");
        dialogBuilder.setMultiChoiceItems(positions, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    arrayLocation.add(which);
                } else if (arrayLocation.contains(which)) {
                    arrayLocation.remove(Integer.valueOf(which));
                }
            }
        });

        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < arrayLocation.size(); i++) {
                    position += positions[arrayLocation.get(i)];
                }
                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialogLocation = dialogBuilder.create();
        dialogLocation.show();
    }
}
