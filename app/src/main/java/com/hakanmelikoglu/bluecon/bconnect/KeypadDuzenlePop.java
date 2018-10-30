package com.hakanmelikoglu.bluecon.bconnect;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hakanmelikoglu.bluecon.bconnect.KeyPad;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KeypadDuzenlePop.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KeypadDuzenlePop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeypadDuzenlePop extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ///////////////////////////////////////////////////////////////////////
    // Özel değilkenler
    private  Veritabani veritabani;
    private static final String VT = "tablo";
    public String kimlik1;


    public String degerDizisi[] = {"degerBir", "degerIki", "degerUc", "degerDort", "degerBes", "degerAlti", "degerYedi", "degerSekiz", "degerDokuz"};

    public String anahtarDizisi[] = {
            "anahtarBir", "anahtarIki", "anahtarUc", "anahtarDort", "anahtarBes", "anahtarAlti", "anahtarYedi","anahtarSekiz",
            "anahtarDokuz", "anahtarA", "anahtarB", "anahtarC"
    };

    public String kume[] = {"1","2","3","4","5","6","7","8","9","A","B","C"};
//////////////////////////////////////////////////////////////////////////////////////////

    public KeypadDuzenlePop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KeypadDuzenlePop.
     */
    // TODO: Rename and change types and number of parameters
    public static KeypadDuzenlePop newInstance(String param1, String param2) {
        KeypadDuzenlePop fragment = new KeypadDuzenlePop();
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
        View view = inflater.inflate(R.layout.fragment_keypad_duzenle_pop, container, false);
        //////////////////////////////////////////////////////////////////////////////

        final EditText keyDD = (EditText)view.findViewById(R.id.editText3);
        final EditText stringDD = (EditText)view.findViewById(R.id.editText4);

 /*
        // Veritabanı Okuma
        veritabani = new Veritabani(getActivity());

        try {

            String[] sutunlar = {"anahtar","deger",    "anahtar2","deger2",     "anahtar3","deger3",    "anahtar4","deger4",    "anahtar5","deger5",
                    "anahtar6","deger6",     "anahtar7","deger7",      "anahtar8","deger8",    "anahtar9","deger9",    "anahtarA",   "anahtarB",   "anahtarC", "kimlik"};

            SQLiteDatabase db1 = veritabani.getReadableDatabase();
            Cursor okunanlar = db1.query(VT,sutunlar,null,null,null,null,null );
            while (okunanlar.moveToNext()){
                anahtarDizisi[0] = okunanlar.getString(okunanlar.getColumnIndex("anahtar"));
                degerDizisi[0] = okunanlar.getString(okunanlar.getColumnIndex("deger"));
                anahtarDizisi[1] = okunanlar.getString(okunanlar.getColumnIndex("anahtar2"));
                degerDizisi[1] = okunanlar.getString(okunanlar.getColumnIndex("deger2"));
                anahtarDizisi[2] = okunanlar.getString(okunanlar.getColumnIndex("anahtar3"));
                degerDizisi[2] = okunanlar.getString(okunanlar.getColumnIndex("deger3"));
                anahtarDizisi[3] = okunanlar.getString(okunanlar.getColumnIndex("anahtar4"));
                degerDizisi[3] = okunanlar.getString(okunanlar.getColumnIndex("deger4"));
                anahtarDizisi[4] = okunanlar.getString(okunanlar.getColumnIndex("anahtar5"));
                degerDizisi[4] = okunanlar.getString(okunanlar.getColumnIndex("deger5"));
                anahtarDizisi[5] = okunanlar.getString(okunanlar.getColumnIndex("anahtar6"));
                degerDizisi[5] = okunanlar.getString(okunanlar.getColumnIndex("deger6"));
                anahtarDizisi[6] = okunanlar.getString(okunanlar.getColumnIndex("anahtar7"));
                degerDizisi[6] = okunanlar.getString(okunanlar.getColumnIndex("deger7"));
                anahtarDizisi[7] = okunanlar.getString(okunanlar.getColumnIndex("anahtar8"));
                degerDizisi[7] = okunanlar.getString(okunanlar.getColumnIndex("deger8"));
                anahtarDizisi[8] = okunanlar.getString(okunanlar.getColumnIndex("anahtar9"));
                degerDizisi[8] = okunanlar.getString(okunanlar.getColumnIndex("deger9"));
                anahtarDizisi[9] = okunanlar.getString(okunanlar.getColumnIndex("anahtarA"));
                anahtarDizisi[10] = okunanlar.getString(okunanlar.getColumnIndex("anahtarB"));
                anahtarDizisi[11] = okunanlar.getString(okunanlar.getColumnIndex("anahtarC"));
                kimlik1 = okunanlar.getString(okunanlar.getColumnIndex("kimlik"));

            }

        }finally {
            veritabani.close();
        }


                if(kimlik1.equals(kume[0])) {
                    keyDD.setText(anahtarDizisi[0]);
                    stringDD.setText(degerDizisi[0]);
                }
                if(kimlik1.equals(kume[1])) {
                    keyDD.setText(anahtarDizisi[1]);
                    stringDD.setText(degerDizisi[1]);
                }
                if(kimlik1.equals(kume[2])) {
                    keyDD.setText(anahtarDizisi[2]);
                    stringDD.setText(degerDizisi[2]);
                }
                if(kimlik1.equals(kume[3])) {
                    keyDD.setText(anahtarDizisi[3]);
                    stringDD.setText(degerDizisi[3]);
                }
                if(kimlik1.equals(kume[4])) {
                    keyDD.setText(anahtarDizisi[4]);
                    stringDD.setText(degerDizisi[4]);
                }
                if(kimlik1.equals(kume[5])) {
                    keyDD.setText(anahtarDizisi[5]);
                    stringDD.setText(degerDizisi[5]);
                }
                if(kimlik1.equals(kume[6])) {
                    keyDD.setText(anahtarDizisi[6]);
                    stringDD.setText(degerDizisi[6]);
                }
                if(kimlik1.equals(kume[7])) {
                    keyDD.setText(anahtarDizisi[7]);
                    stringDD.setText(degerDizisi[7]);
                }
                if(kimlik1.equals(kume[8])) {
                    keyDD.setText(anahtarDizisi[8]);
                    stringDD.setText(degerDizisi[8]);
                }
                if(kimlik1.equals(kume[9])) {
                    keyDD.setText(anahtarDizisi[9]);
                }
                if(kimlik1.equals(kume[10])) {
                    keyDD.setText(anahtarDizisi[10]);
                }
                if(kimlik1.equals(kume[11])) {
                    keyDD.setText(anahtarDizisi[11]);
                }


*/
        Button btnKaydet =(Button)view.findViewById(R.id.button19);
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (kimlik1.equals("1")) {
                    kayitEkleBir(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("2")) {
                    kayitEkleIki(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("3")) {
                    kayitEkleUc(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("4")) {
                    kayitEkleDort(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("5")) {
                    kayitEkleBes(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("6")) {
                    kayitEkleAlti(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("7")) {
                    kayitEkleYedi(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("8")) {
                    kayitEkleSekiz(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("9")) {
                    kayitEkleDokuz(keyDD.getText().toString(), stringDD.getText().toString());
                }
                if (kimlik1.equals("A")) {
                    kayitEkleA(keyDD.getText().toString());
                }
                if (kimlik1.equals("B")) {
                    kayitEkleB(keyDD.getText().toString());
                }
                if (kimlik1.equals("C")) {
                    kayitEkleC(keyDD.getText().toString());
                }

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                KeyPad keyPad = new KeyPad();
                transaction.add(R.id.sample_content_fragment, keyPad);
                transaction.commit();

            }
        });


        return view;
    }

    private void kayitEkleBir(String anahtar, String deger){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar",anahtar);
            contentValues.put("deger", deger);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleIki(String anahtar2, String deger2){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar2",anahtar2);
            contentValues.put("deger2", deger2);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleUc(String anahtar3, String deger3){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar3",anahtar3);
            contentValues.put("deger3", deger3);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleDort(String anahtar4, String deger4){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar4",anahtar4);
            contentValues.put("deger4", deger4);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleBes(String anahtar5, String deger5){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar5",anahtar5);
            contentValues.put("deger5", deger5);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleAlti(String anahtar6, String deger6){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar6",anahtar6);
            contentValues.put("deger6", deger6);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleYedi(String anahtar7, String deger7){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar7",anahtar7);
            contentValues.put("deger7", deger7);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleSekiz(String anahtar8, String deger8){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar8",anahtar8);
            contentValues.put("deger8", deger8);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleDokuz(String anahtar9, String deger9){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtar9",anahtar9);
            contentValues.put("deger9", deger9);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleA(String anahtarA){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtarA",anahtarA);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }
    }

    private void kayitEkleB(String anahtarB){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtarB",anahtarB);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }

    }

    private void kayitEkleC(String anahtarC){
        veritabani = new Veritabani(getActivity());
        try{
            SQLiteDatabase db = veritabani.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("anahtarC",anahtarC);
            db.insertOrThrow(VT,null,contentValues);
        }finally {
            veritabani.close();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
