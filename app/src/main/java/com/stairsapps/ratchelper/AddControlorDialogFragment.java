package com.stairsapps.ratchelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stairsapps.ratchelper.models.Controlor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;

/**
 * Created by filip on 6/28/2016.
 */
public class AddControlorDialogFragment extends DialogFragment {

    private EditText mEditText;
    private Switch mSwitch;
    private Spinner mSpinner;
    private Button mSumbit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public AddControlorDialogFragment(){

    }

    public static AddControlorDialogFragment newInstance() {

        Bundle args = new Bundle();
        AddControlorDialogFragment fragment = new AddControlorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_add_controlor,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Raporteaza controlor");
        ButterKnife.findById(view,R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        mSpinner = ButterKnife.findById(view,R.id.liniaChooser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.linii,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d("FIREBASE","onAuthStateChanged:signed_in:" + user.getUid());
                }else
                {
                    Log.d("FIREBASE","onAuthStateChanged:signed_out");
                }
            }
        };

        mAuth.signInAnonymously().addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FIREBASE", "signInAnonymously:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w("FIREBASE", "signInAnonymously", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("controlori");

        ButterKnife.findById(view,R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitch = ButterKnife.findById(getView(),R.id.controlorSwitch);
                Boolean state = mSwitch.isChecked();
                Log.v("test",String.valueOf(state));
                EditText et = ButterKnife.findById(getView(),R.id.editText);
                String numar = et.getText().toString();
                if (numar==null || numar.equals("")){
                    submitControlor(mSpinner.getSelectedItem().toString(),state);
                }else {
                    submitControlor(mSpinner.getSelectedItem().toString(),numar,state);
                }

                getDialog().dismiss();
            }
        });

    }

    private void submitControlor(String linia,String numar,Boolean state){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy, HH:mm");
        Controlor controlor = new Controlor(linia,numar,df.format(c.getTime()),state);
        reference.push().setValue(controlor);

    }

    private void submitControlor(String linia,Boolean state){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy, HH:mm");
        Controlor controlor = new Controlor(linia,df.format(c.getTime()),state);
        reference.push().setValue(controlor);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
