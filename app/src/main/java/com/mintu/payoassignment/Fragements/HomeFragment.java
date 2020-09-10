package com.mintu.payoassignment.Fragements;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mintu.payoassignment.Adapters.DataAdapter;
import com.mintu.payoassignment.Models.ItemModel;
import com.mintu.payoassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String URL_API_DATA = "https://reqres.in/api/users?page=1";
    private List<ItemModel> list;
    private List<ItemModel> sortedList;
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

        //Fetching Data from API
        fetchData();

        return view;
    }

    private void fetchData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Data...Please Wait!");
        dialog.show();

        //Getting Data from API
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_API_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                ItemModel item = new ItemModel(
                                        object.getString("first_name"),
                                        object.getString("last_name"),
                                        object.getString("email"),
                                        object.getString("avatar")
                                );

                                list.add(item);
                            }

                            //Used for sorting List
                            Collections.sort(list, ItemModel.COMPARE_BY_NAME);

                            DataAdapter adapter = new DataAdapter(list,getContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.d("ERROR", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("VOLLEY", error.toString());
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
