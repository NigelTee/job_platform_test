package materialtest.example.user.materialtest.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import materialtest.example.user.materialtest.R;
import materialtest.example.user.materialtest.adapter.AdapterBoxOffice;
import materialtest.example.user.materialtest.extra.Constants;
import materialtest.example.user.materialtest.extra.FilterInterface;
import materialtest.example.user.materialtest.extra.SearchInterface;
import materialtest.example.user.materialtest.extra.SortListener;
import materialtest.example.user.materialtest.network.VolleySingleton;
import materialtest.example.user.materialtest.pojo.Plants;

import static materialtest.example.user.materialtest.extra.Keys.EndpointBoxOffice.KEY_COMMON;
import static materialtest.example.user.materialtest.extra.Keys.EndpointBoxOffice.KEY_CULTIVAR;
import static materialtest.example.user.materialtest.extra.Keys.EndpointBoxOffice.KEY_GENUS;
import static materialtest.example.user.materialtest.extra.Keys.EndpointBoxOffice.KEY_ID;
import static materialtest.example.user.materialtest.extra.Keys.EndpointBoxOffice.KEY_PLANTS;
import static materialtest.example.user.materialtest.extra.Keys.EndpointBoxOffice.KEY_SPECIES;
import static materialtest.example.user.materialtest.extra.UrlEndpoints.URL_BOX_OFFICE;
import static materialtest.example.user.materialtest.extra.UrlEndpoints.URL_CHAR_AMPERSAND;
import static materialtest.example.user.materialtest.extra.UrlEndpoints.URL_CHAR_QUESTION;
import static materialtest.example.user.materialtest.extra.UrlEndpoints.URL_PARAM_LIMIT;

//import com.android.volley.toolbox.ImageLoader;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInternship#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInternship extends Fragment implements SortListener, SwipeRefreshLayout.OnRefreshListener, FilterInterface, SearchInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_PLANTS = "state plants";
    private SwipeRefreshLayout swiperefreshlayout;
    //private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    //private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView listMoviesHits;
    private ArrayList<Plants> listPlants = new ArrayList<>();
    private AdapterBoxOffice adapterBoxOffice;
    private TextView textVolleyError;
    private String plantname = "rose";

    public static FragmentInternship newInstance(String param1, String param2) {
        FragmentInternship fragment = new FragmentInternship();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_PLANTS, listPlants);
    }

    public static String getRequestURL(String types) {
        return URL_BOX_OFFICE + URL_CHAR_QUESTION + URL_CHAR_AMPERSAND + URL_PARAM_LIMIT + types;
    }

    public FragmentInternship() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            plantname = getArguments().getString(ARG_PARAM1);
            //String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //initialize volleysingleton
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        //initialize requestqueue
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boxoffice, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        swiperefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.swipePlantsHits);
        swiperefreshlayout.setOnRefreshListener(this);
        listMoviesHits = (RecyclerView) view.findViewById(R.id.listMoviesHits);
        listMoviesHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMoviesHits.setAdapter(adapterBoxOffice);

        //coming back from rotation
        if (savedInstanceState != null) {
            listPlants = savedInstanceState.getParcelableArrayList(STATE_PLANTS);
            adapterBoxOffice.setPlantList(listPlants);
        } else {
            //app starting
            sendJsonRequest();
        }
        return view;
    }


    //SORT LISTENER
    @Override
    public void onSortByLocation() {
        Toast.makeText(getActivity(), "sortlocation", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSortByCategory() {
        Toast.makeText(getActivity(), "cat", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void sendJsonRequest() {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestURL(plantname),
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textVolleyError.setVisibility(View.GONE);
                        listPlants = parseJSONResponse(response);
                        adapterBoxOffice.setPlantList(listPlants);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });
        requestQueue.add(request);
    }

    private void handleVolleyError(VolleyError error) {

        textVolleyError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth_failure);
        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_server_failure);
        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.error_network);
        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_parser);
        }
    }

    private ArrayList<Plants> parseJSONResponse(JSONObject response) {
        ArrayList<Plants> listPlants = new ArrayList<>();
        //check if there is any data inside
        final int res = response.length();

        if (res != 0) {

            try {

                JSONArray arrayPlants = response.getJSONArray(KEY_PLANTS);

                long id = -1;
                String species = Constants.NA;
                String common = Constants.NA;
                String cultivar = Constants.NA;
                String genus = Constants.NA;

                for (int i = 0; i < arrayPlants.length(); i++) {
                    JSONObject currentPlants = arrayPlants.getJSONObject(i);

                    if (currentPlants.has(KEY_ID) && !currentPlants.isNull(KEY_ID)) {
                        id = currentPlants.getLong(KEY_ID);
                    }
                    if (currentPlants.has(KEY_SPECIES) && !currentPlants.isNull(KEY_SPECIES)) {
                        species = currentPlants.getString(KEY_SPECIES);
                    }
                    if (currentPlants.has(KEY_COMMON) && !currentPlants.isNull(KEY_COMMON)) {
                        common = currentPlants.getString(KEY_COMMON);
                    }
                    if (currentPlants.has(KEY_CULTIVAR) && !currentPlants.isNull(KEY_CULTIVAR)) {
                        cultivar = currentPlants.getString(KEY_CULTIVAR);
                    }
                    if (currentPlants.has(KEY_GENUS) && !currentPlants.isNull(KEY_GENUS)) {
                        genus = currentPlants.getString(KEY_GENUS);
                    }

                    Plants plants = new Plants();
                    plants.setId(id);
                    plants.setSpecies(species);
                    plants.setGenus(genus);
                    plants.setCommon(common);
                    plants.setCultivar(cultivar);

                    if (id != -1) {//(id != -1 && !title.equals(Constants.NA))
                        listPlants.add(plants);
                    }
                }

                //L.t(getActivity(), listPlants.toString());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        return listPlants;
    }

    @Override
    public void onRefresh() {
        sendJsonRequest();

        if (swiperefreshlayout.isRefreshing()) {
            swiperefreshlayout.setRefreshing(false);
        }

    }


    //filter by location
    @Override
    public void onFilterByLocation(String text) {
        plantname = text;
        Toast.makeText(getActivity(), "plantname = " + text, Toast.LENGTH_SHORT).show();
        sendJsonRequest();
        listMoviesHits.scrollToPosition(0);//allow list to display from the start
    }

    //filter by job
    @Override
    public void onFilterByJobType() {

    }

    //Search Box
    @Override
    public void onSearchBar(String text) {
        //Toast.makeText(getActivity(), "plantname = " + text, Toast.LENGTH_SHORT).show();
        adapterBoxOffice.getFilter().filter(text);

    }


}
