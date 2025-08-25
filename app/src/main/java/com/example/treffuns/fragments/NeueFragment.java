package com.example.treffuns.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.treffuns.Activities.NeuTreffenActivity;
import com.example.treffuns.R;
import com.example.treffuns.model.TreffenModel;
import com.example.treffuns.utils.FirebaseUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NeueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NeueFragment extends Fragment {

    private TextInputEditText etTreffenName;
    private AutoCompleteTextView actvCategory;
    private RadioGroup rgTreffenType;
    private RadioButton rbPrivate;
    private RadioButton rbPublic;
    private TextInputEditText etLocation;
    private Button btnCreateTreffen;
    private Button btnViewTreffen;
    private TreffenModel currentTreffen;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NeueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NeueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NeueFragment newInstance(String param1, String param2) {
        NeueFragment fragment = new NeueFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_neue, container, false);
        etTreffenName = rootView.findViewById(R.id.et_meeting_name);
        actvCategory = rootView.findViewById(R.id.actv_category);
        rgTreffenType = rootView.findViewById(R.id.rg_meeting_type);
        rbPrivate = rootView.findViewById(R.id.rb_private);
        rbPublic = rootView.findViewById(R.id.rb_public);
        etLocation = rootView.findViewById(R.id.et_location);
        btnCreateTreffen = rootView.findViewById(R.id.btn_create_meeting);
        btnViewTreffen = rootView.findViewById(R.id.btn_view_meeting);
        btnViewTreffen.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NeuTreffenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        String[] categories = {"Lernen", "Sport", "Gaming", "Musik", "Kultur", "Diskussion", "Essen", "Sonstiges"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), // Context (für Fragment: requireContext())
                android.R.layout.simple_dropdown_item_1line, // Layout für Dropdown-Items
                categories
        );
        actvCategory.setAdapter(adapter);
        btnCreateTreffen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTreffen();
            }
        });
        return rootView;
    }


    private void createTreffen() {
        String treffenName = etTreffenName.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        // Überprüfe den ausgewählten Meeting-Typ
        int selectedTypeId = rgTreffenType.getCheckedRadioButtonId();
        String treffenType;
        if (selectedTypeId == R.id.rb_private) {
            treffenType = "Present";
        } else if (selectedTypeId == R.id.rb_public) {
            treffenType = "Online";
        } else {
            treffenType = "Nicht ausgewählt"; // Sollte nicht passieren, wenn ein RadioButton ausgewählt ist
        }

        // Grundlegende Validierung
        if (treffenName.isEmpty()) {
            etTreffenName.setError("Name des Treffens ist erforderlich");
            etTreffenName.requestFocus();
            return;
        }
        if (category.isEmpty()) {
            actvCategory.setError("Kategorie ist erforderlich");
            actvCategory.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            etLocation.setError("Treffpunkt ist erforderlich");
            etLocation.requestFocus();
            return;
        }

        // Beispiel: Meeting-Daten an Firebase senden
        TreffenModel neuTreffen = new TreffenModel(treffenName, category, treffenType, location, FirebaseUtil.currentUserId());

        FirebaseUtil.currentUserTreffen()
                .get().addOnCompleteListener(task -> { // <-- Lambda-Ausdruck: task -> { ... }
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult(); // Hole das Ergebnis
                        if (documentSnapshot.exists()) {
                            currentTreffen = documentSnapshot.toObject(TreffenModel.class);
                            if (currentTreffen != null) {
                                Toast.makeText(getContext(), "Sie können nur ein Meeting erstellen und haben schon ein erstellt", Toast.LENGTH_LONG).show();
                            } else {
                                FirebaseUtil.currentUserTreffen().set(neuTreffen)
                                        .addOnSuccessListener(documentReference -> {
                                            etTreffenName.getText().clear();
                                            etLocation.getText().clear();
                                            actvCategory.getText().clear();
                                            rgTreffenType.check(R.id.rb_private);
                                            Toast.makeText(getContext(), "Meeting erfolgreich erstellt!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getContext(), NeuTreffenActivity.class));
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getContext(), "Fehler beim Erstellen des Meetings: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            }
                        } else {
                            FirebaseUtil.currentUserTreffen().set(neuTreffen)
                                    .addOnSuccessListener(documentReference -> {
                                        etTreffenName.getText().clear();
                                        etLocation.getText().clear();
                                        actvCategory.getText().clear();
                                        rgTreffenType.check(R.id.rb_private);
                                        Toast.makeText(getContext(), "Meeting erfolgreich erstellt!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), NeuTreffenActivity.class));
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Fehler beim Erstellen des Meetings: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    }
                });

    }
}