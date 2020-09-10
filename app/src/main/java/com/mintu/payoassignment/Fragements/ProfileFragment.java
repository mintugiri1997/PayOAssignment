package com.mintu.payoassignment.Fragements;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mintu.payoassignment.R;

public class ProfileFragment extends Fragment {

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DocumentReference docRef = FirebaseFirestore.getInstance().collection("PayOUsers")
            .document(currentUser);
    private CircularImageView imageProfile;
    private TextView nameProfile;
    private TextView emailProfile;
    private TextView mobileProfile;
    private TextView addressProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageProfile = (CircularImageView) view.findViewById(R.id.image_profile);
        nameProfile = (TextView) view.findViewById(R.id.name_profile);
        emailProfile = (TextView) view.findViewById(R.id.email_profile);
        mobileProfile = (TextView) view.findViewById(R.id.mobile_profile);
        addressProfile = (TextView) view.findViewById(R.id.address_profile);

        fetchData();

        return view;
    }

    private void fetchData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching Data...");
        dialog.show();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dialog.dismiss();
                nameProfile.setText(documentSnapshot.getString("fname")
                + " " + documentSnapshot.getString("lname"));
                emailProfile.setText(documentSnapshot.getString("email"));
                mobileProfile.setText(documentSnapshot.getString("mobileNumber"));
                addressProfile.setText(documentSnapshot.getString("address"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
