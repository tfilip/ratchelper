package com.stairsapps.ratchelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FakeSMS extends Fragment {


    String linia;
    public int numarBilete;
    static final String pool = "EMRCKLPTHYJN39";
    String sender,body,defaultSmsApp;
    String formattedDate;
    String formattedTime;
    String code;
    ContentValues values;
    Button goButton;

    public FakeSMS() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fake_sm, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.findById(view,R.id.RL).requestFocus();

        //Populate the spinner
        Spinner spinner = ButterKnife.findById(view,R.id.chooser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.linii,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Get the line
        //Default value
        linia = (String) adapter.getItem(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                linia = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Numar bilete
        final NumberPicker numberPicker = ButterKnife.findById(view,R.id.np);
        numarBilete = 1;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d("DEBUG","Selected number picker "+i1);
                numarBilete=i1;
            }
        });

        defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(getContext());

        Button goButton = ButterKnife.findById(view,R.id.GoSMS);
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                final String myPackageName = getActivity().getPackageName();
                Log.v("TEST","OnClick");
                if (!Telephony.Sms.getDefaultSmsPackage(getActivity()).equals(myPackageName)) {
                    Log.v("TEST","Clicked");
                    Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
                    startActivityForResult(intent, 15);


                }
            }
        });



    }

    private void WriteSMS(String message, String phoneNumber, int i){


        Log.v("TEST","SMS");
        ContentValues valuesAux = new ContentValues();
        valuesAux.put(Telephony.Sms.ADDRESS,phoneNumber);
        valuesAux.put(Telephony.Sms.DATE,System.currentTimeMillis());
        valuesAux.put(Telephony.Sms.BODY,message);

        ContentValues values = new ContentValues();
        values.put(Telephony.Sms.ADDRESS,7424);
        values.put(Telephony.Sms.DATE,System.currentTimeMillis());
        values.put(Telephony.Sms.BODY, String.format("L%s",linia));

        getContext().getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI, valuesAux);
        getContext().getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI,values);
        Log.v("TEST",String.valueOf(i));
        if(i==numarBilete){
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
            startActivity(intent);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("TEST",String.valueOf(requestCode));
        Log.v("TEST",String.valueOf(resultCode));
        if(requestCode == 15){
            if(resultCode == Activity.RESULT_OK){
                Log.v("TEST","RESULT_OK");
                final String myPackageName = getContext().getPackageName();
                for(int i=1;i<=numarBilete;i++) {
                    Log.v("TEST",Telephony.Sms.getDefaultSmsPackage(getActivity()));
                    if (Telephony.Sms.getDefaultSmsPackage(getContext()).equals(myPackageName)) {
                        class BackgroundSMS extends AsyncTask<Void,Void,Void>{

                            private int number;
                            public BackgroundSMS(int i){
                                this.number=i;
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                WriteSMS(updateSMS(generateCode()), sender,number);
                                return null;
                            }
                        }

                        new BackgroundSMS(i).execute();

                    }
                }
            }
        }
    }

    public String generateCode(){

        String cod = "";
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE,40);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        df = new SimpleDateFormat("HH:mm");
        formattedTime = df.format(c.getTime());
        Random rnd = new Random();
        do{
            StringBuilder stringBuilder = new StringBuilder(6);
            for(int i=0;i<6;i++)
                stringBuilder.append(pool.charAt(rnd.nextInt(pool.length())));
            cod = stringBuilder.toString();
            Log.v("TEST",cod);
        }while (!cod.contains("3")||cod.contains("9"));
        Log.v("TEST","EXITED");
        return cod;
    }

    public String updateSMS(String code){
        sender = "24148";
        body = String.format("Cod plata: %s - 1 calatorie valabila pe linia %s pana la %s, %s,\ntarif 0.4E+TVA. Info 0316903903 (tarif normal). Pastreaza mesajul.",code,linia,formattedTime,formattedDate);
        return body;
    }

}
