package materialtest.example.user.materialtest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import materialtest.example.user.materialtest.R;
import materialtest.example.user.materialtest.extra.SortListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPartTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVolunteer extends Fragment implements SortListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static FragmentVolunteer newInstance(String param1, String param2) {
        FragmentVolunteer fragment = new FragmentVolunteer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentVolunteer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }


    @Override
    public void onSortByLocation() {
        Toast.makeText(getActivity(), "location",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSortByCategory() {
        Toast.makeText(getActivity(),"cat", Toast.LENGTH_SHORT).show();
    }
}

