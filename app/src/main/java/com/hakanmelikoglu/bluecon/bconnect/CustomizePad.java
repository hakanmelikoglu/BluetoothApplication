package com.hakanmelikoglu.bluecon.bconnect;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomizePad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomizePad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomizePad extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Özel değişkenler
    private static final String VT3 = "giden";
    public String[] sutunlar3 = {"databitset"};
    private static final String VT4 = "gelen";
    public String[] sutunlar4 = {"databitget"};

    public String dataBitSet; // giden veri
    public String dataBitGet; // gelen veri


    /////////////////////////////////////////
    public CustomizePad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomizePad.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomizePad newInstance(String param1, String param2) {
        CustomizePad fragment = new CustomizePad();
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
        View view = inflater.inflate(R.layout.fragment_customize_pad, container, false);

        // Veritabanından gelen bit verisini alır. databitset ve databitget değişkenlerine atar
        vt3al();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.customize_menu));

        TextView yazi = (TextView)view.findViewById(R.id.textView16);
        yazi.setText(dataBitSet);

        return view;
    }


    private void vt3al(){
        Veritabani veritabani = new Veritabani(getActivity());
        try {

            // SQL verileri alır
            SQLiteDatabase db1 = veritabani.getReadableDatabase();
            Cursor okunanlar = db1.query(VT3, sutunlar3, null, null, null, null, null);
            while (okunanlar.moveToNext()) {
                dataBitSet = okunanlar.getString(okunanlar.getColumnIndex("databitset"));
            }
        }finally {
            veritabani.close();
        }
    }

    private void vt3gonder(String databits){
        Veritabani vt = new Veritabani(getActivity());
        try{
            SQLiteDatabase db2 = vt.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("databitset", databits);
            db2.insertOrThrow(VT3, null, values);
        }finally {
            vt.close();
        }
    }

    private void vt4al(){
        Veritabani veritabani = new Veritabani(getActivity());
        try {

            // SQL verileri alır
            SQLiteDatabase db1 = veritabani.getReadableDatabase();
            Cursor okunanlar = db1.query(VT4, sutunlar4, null, null, null, null, null);
            while (okunanlar.moveToNext()) {
                dataBitGet = okunanlar.getString(okunanlar.getColumnIndex("databitget"));
            }
        }finally {
            veritabani.close();
        }
    }

    private void vt4gonder(String databitg){
        Veritabani vt = new Veritabani(getActivity());
        try{
            SQLiteDatabase db2 = vt.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("databitget", databitg);

            db2.insertOrThrow(VT4, null, values);
        }finally {
            vt.close();
        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //       + " must implement OnFragmentInteractionListener");
        }
    }
*/
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

    @Override
    public void onActivityCreated(Bundle savedInstaceState){
        super.onActivityCreated(savedInstaceState);

    }
}
