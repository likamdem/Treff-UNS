package com.example.treffuns.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.treffuns.R;
import com.example.treffuns.adapter.SucheTreffenRecyclerAdapter;
import com.example.treffuns.model.TreffenModel;
import com.example.treffuns.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SucheFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SucheFragment extends Fragment {



    EditText searchInput;
    ImageButton searchButton;

    RecyclerView recyclerView;

    SucheTreffenRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SucheFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SucheFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SucheFragment newInstance(String param1, String param2) {
        SucheFragment fragment = new SucheFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_suche, container, false);
        searchInput = rootView.findViewById(R.id.suche_treffen_input);
        searchButton = rootView.findViewById(R.id.suche_treffen_btn);
        recyclerView = rootView.findViewById(R.id.suche_treffen_recycler_view);
        searchInput.requestFocus();
        searchButton.setOnClickListener(v->{
            String searchTerm = searchInput.getText().toString();
            if (searchTerm.isBlank()){
                searchInput.setError("Ungültige Treffenname");
                return;
            }
            setupSearchRecyclerView(searchTerm);
        });
        // Inflate the layout for this fragment
        return rootView;
    }


    void setupSearchRecyclerView(String searchTerm){
        Query query = FirebaseUtil.allTreffenCollectionReference()
                .whereGreaterThanOrEqualTo("treffenName", searchTerm);
        FirestoreRecyclerOptions<TreffenModel> options =  new FirestoreRecyclerOptions.Builder<TreffenModel>()
                .setQuery(query, TreffenModel.class).build();
        adapter = new SucheTreffenRecyclerAdapter(options, getActivity().getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart(){
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}