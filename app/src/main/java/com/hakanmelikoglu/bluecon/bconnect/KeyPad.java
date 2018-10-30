package com.hakanmelikoglu.bluecon.bconnect;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KeyPad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KeyPad#newInstance} factory method to
 * create an instance of this fragment.
 *
 * BU FRAGMENTTA sendMessage METOTUNDA VERİLER BT İLE GÖNDERİLİR
 * BU İŞLEM İÇİN  BluetoothChatService ÇALIŞTIRILIR VE DOĞRUDAN BT DONANIMI
 * KULLANILIR VE VERİ BYTE DİZİSİ ŞEKLİNDE GÖNDERİLİR.
 *
 * BUTONLAR LOCAK VERİTABANI İLE İLİŞKİLİDİR VE SONRADAN GÜNCELLENEBİLİR BİR ŞEKİLDE
 * VERİTABANINA KAYDEDİLİR.
 *
 * VERİ OKUMAK İÇİN Handler METOTUNDAKİ MESSAGE.READ DEĞİŞKENİ ALTINDAN VERİ ALABİLİRİZ
 */
public class KeyPad extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Giden iletiler için dize arabellek
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    //BluetoothChatService servis tanımlaması
    private BluetoothChatService mChatService = null;

///////////////////////////////////////////////////////////////////////////////////////////////////////


    // Özel tanımlı değişkenler
    //public char gonderilenVeri;
    public EditText veriGonder;
    private Veritabani verit;
    private static final String VT = "tablo";

    /*
    String degerDizisi[] = {"degerBir", "degerIki", "degerUc", "degerDort", "degerBes", "degerAlti", "degerYedi", "degerSekiz", "degerDokuz"};

    String anahtarDizisi[] = {
            "anahtarBir", "anahtarIki", "anahtarUc", "anahtarDort", "anahtarBes", "anahtarAlti", "anahtarYedi","anahtarSekiz",
            "anahtarDokuz", "anahtarA", "anahtarB", "anahtarC"
    };
*/

    //public boolean br=false, iki=false, uc=false, drt=false, bs=false, alt=false, yd=false, skz=false, dkz=false, aa=false, bb=false, cc=false;

    public String[] sutunlar = {"anahtar","deger",    "anahtar2","deger2",     "anahtar3","deger3",    "anahtar4","deger4",    "anahtar5","deger5",
            "anahtar6","deger6",     "anahtar7","deger7",      "anahtar8","deger8",    "anahtar9","deger9",    "anahtarA",   "anahtarB",   "anahtarC"};

    public String deklare[] = {"anahtar","deger",    "anahtar2","deger2",     "anahtar3","deger3",    "anahtar4","deger4",    "anahtar5","deger5",
            "anahtar6","deger6",     "anahtar7","deger7",      "anahtar8","deger8",    "anahtar9","deger9",    "anahtarA",   "anahtarB",   "anahtarC"};

    Snackbar snackbar;

    private static final String VT3 = "giden";
    public String[] sutunlar3 = {"databitset"};
    private static final String VT4 = "gelen";
    public String[] sutunlar4 = {"databitget"};

    //public String dataBitSet; // giden veri
    //public String dataBitGet; // gelen veri

/////////////////////////////////////////////////////////////////////////////////////////////
    public KeyPad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KeyPad.
     */
    // TODO: Rename and change types and number of parameters
    public static KeyPad newInstance(String param1, String param2) {
        KeyPad fragment = new KeyPad();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth kullanılamıyor", Toast.LENGTH_LONG).show();
            activity.finish();
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_key_pad, container, false);

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //toolbar.setTitle(getResources().getString(R.string.keypad_mode));

        //Toast.makeText(getActivity().getApplicationContext(),R.string.keypad_first, Toast.LENGTH_SHORT).show();

        //Snackbar.make(getActivity(), R.string.keypad_first, Snackbar.LENGTH_SHORT).show();

        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Titreşim ayarları. butona uzun basınca çalışır
        final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // Butonlar
        final Button bir = (Button) view.findViewById(R.id.button2);
        final Button iki = (Button) view.findViewById(R.id.button6);
        final Button uc = (Button) view.findViewById(R.id.button5);
        final Button butonA = (Button) view.findViewById(R.id.button11);
        final Button dort = (Button) view.findViewById(R.id.button3);
        final Button bes = (Button) view.findViewById(R.id.button7);
        final Button alti = (Button) view.findViewById(R.id.button8);
        final Button butonB = (Button) view.findViewById(R.id.button12);
        final Button yedi = (Button) view.findViewById(R.id.button4);
        final Button sekiz = (Button) view.findViewById(R.id.button10);
        final Button dokuz = (Button) view.findViewById(R.id.button9);
        final Button butonC = (Button) view.findViewById(R.id.button13);

        // Buton TextViewleri
        final TextView birTX = (TextView) view.findViewById(R.id.textView4);
        final TextView ikiTX = (TextView) view.findViewById(R.id.textView5);
        final TextView ucTX = (TextView) view.findViewById(R.id.textView6);
        final TextView dortTX = (TextView) view.findViewById(R.id.textView7);
        final TextView besTX = (TextView) view.findViewById(R.id.textView8);
        final TextView altiTX = (TextView) view.findViewById(R.id.textView9);
        final TextView yediTX = (TextView) view.findViewById(R.id.textView10);
        final TextView sekizTX = (TextView) view.findViewById(R.id.textView11);
        final TextView dokuzTX = (TextView) view.findViewById(R.id.textView12);

        // Gönderilen veri edittexti
        veriGonder = (EditText) view.findViewById(R.id.editText);

        // Veritabanından verileri çeker
        vtGoster();

        // Bluetooth üzerinden gönderilecek veriler dataBitGet değişkeninde tutulur.

        // Buton değerleri
        bir.setText(deklare[0]);
        iki.setText(deklare[2]);
        uc.setText(deklare[4]);
        dort.setText(deklare[6]);
        bes.setText(deklare[8]);
        alti.setText(deklare[10]);
        yedi.setText(deklare[12]);
        sekiz.setText(deklare[14]);
        dokuz.setText(deklare[16]);

        butonA.setText(deklare[18]);
        butonB.setText(deklare[19]);
        butonC.setText(deklare[20]);

        // Buton altı yazıları
        birTX.setText(deklare[1]);
        ikiTX.setText(deklare[3]);
        ucTX.setText(deklare[5]);
        dortTX.setText(deklare[7]);
        besTX.setText(deklare[9]);
        altiTX.setText(deklare[11]);
        yediTX.setText(deklare[13]);
        sekizTX.setText(deklare[15]);
        dokuzTX.setText(deklare[17]);


        // Buton tek tıklama
        bir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veriGonder.setText(bir.getText().toString());
                //vt3gonder(deklare[0]); // veritabanına String 1 bilgisi gider
                sendMessage(deklare[0]); // BT İLE VERİ GÖNDERİR
            }
        });

        // Buton çift tıklama
        bir.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(bir.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(birTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                    bir.setText(input1.getText());
                                    birTX.setText(input2.getText());
                                    hideKeyboard(textEntryView);
                                    fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();

                return false;
            }
        });


        iki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veriGonder.setText(iki.getText().toString());
                //vt3gonder(deklare[2]); // veritabanına String 2 bilgisi gider
                sendMessage(deklare[2]); // BT İLE VERİ GÖNDERİR
            }
        });

        iki.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(iki.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(ikiTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                    iki.setText(input1.getText());
                                    ikiTX.setText(input2.getText());
                                    hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);

                            }
                        });
                alert.show();
                return false;
            }
        });

        uc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[4]); // veritabanına String 3 bilgisi gider
                veriGonder.setText(uc.getText().toString());
                sendMessage(deklare[4]); // BT İLE VERİ GÖNDERİR
            }
        });

        uc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(uc.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(ucTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                uc.setText(input1.getText());
                                ucTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        butonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[18]); // veritabanına String A bilgisi gider
                veriGonder.setText(butonA.getText().toString());
                sendMessage(deklare[18]); // BT İLE VERİ GÖNDERİR
            }
        });

        butonA.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(butonA.getText().toString(), TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                butonA.setText(input1.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        dort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[6]); // veritabanına String 4 bilgisi gider
                veriGonder.setText(dort.getText().toString());
                sendMessage(deklare[6]); // BT İLE VERİ GÖNDERİR
            }
        });

        dort.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(dort.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(dortTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dort.setText(input1.getText());
                                dortTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        bes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[8]); // veritabanına String 5 bilgisi gider
                veriGonder.setText(bes.getText().toString());
                sendMessage(deklare[8]); // BT İLE VERİ GÖNDERİR
            }
        });

        bes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(bes.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(besTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                bes.setText(input1.getText());
                                besTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        alti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[10]); // veritabanına String 6 bilgisi gider
                veriGonder.setText(alti.getText().toString());
                sendMessage(deklare[10]); // BT İLE VERİ GÖNDERİR

            }
        });

        alti.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(alti.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(altiTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                alti.setText(input1.getText());
                                altiTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        butonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[19]); // veritabanına String B bilgisi gider
                veriGonder.setText(butonB.getText().toString());
                sendMessage(deklare[19]); // BT İLE VERİ GÖNDERİR
            }
        });

        butonB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(butonB.getText().toString(), TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                butonB.setText(input1.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        yedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[12]); // veritabanına String 7 bilgisi gider
                veriGonder.setText(yedi.getText().toString());
                sendMessage(deklare[12]); // BT İLE VERİ GÖNDERİR
            }
        });

        yedi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(yedi.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(yediTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                yedi.setText(input1.getText());
                                yediTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        sekiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[14]); // veritabanına String 8 bilgisi gider
                veriGonder.setText(sekiz.getText().toString());
                sendMessage(deklare[14]); // BT İLE VERİ GÖNDERİR
            }
        });

        sekiz.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(sekiz.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(sekizTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                sekiz.setText(input1.getText());
                                sekizTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        dokuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[16]); // veritabanına String 9 bilgisi gider
                veriGonder.setText(dokuz.getText().toString());
                sendMessage(deklare[16]); // BT İLE VERİ GÖNDERİR
            }
        });

        dokuz.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(dokuz.getText().toString(), TextView.BufferType.EDITABLE);
                input2.setText(dokuzTX.getText().toString(),TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dokuz.setText(input1.getText());
                                dokuzTX.setText(input2.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });

        butonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vt3gonder(deklare[20]); // veritabanına String C bilgisi gider
                veriGonder.setText(butonC.getText().toString());
                sendMessage(deklare[20]); // BT İLE VERİ GÖNDERİR
            }
        });

        butonC.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                vibe.vibrate(20);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.text_entry, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText5);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText6);

                input1.setText(butonC.getText().toString(), TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.duzenle_text).setView(textEntryView).setPositiveButton(R.string.kaydet,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                butonC.setText(input1.getText());
                                hideKeyboard(textEntryView);
                                fab.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                fab.setVisibility(View.INVISIBLE);
                            }
                        });
                alert.show();
                return false;
            }
        });



        //fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_save_black_48dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                snackbar = Snackbar.make(view, R.string.kaydedildi, Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_sari5));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                snackbar.show();


                btnKaydet(bir.getText().toString(), birTX.getText().toString(), iki.getText().toString(), ikiTX.getText().toString(),
                        uc.getText().toString(), ucTX.getText().toString(), dort.getText().toString(), dortTX.getText().toString(),
                        bes.getText().toString(), besTX.getText().toString(), alti.getText().toString(), altiTX.getText().toString(),
                        yedi.getText().toString(), yediTX.getText().toString(), sekiz.getText().toString(), sekizTX.getText().toString(),
                        dokuz.getText().toString(), dokuzTX.getText().toString(), butonA.getText().toString(), butonB.getText().toString(), butonC.getText().toString());

                hideKeyboard(view);
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return view;
    }



    private void setupChat() {

        //TextView baglanDurum =(TextView)getActivity().findViewById(R.id.textView13);
        //baglanDurum.setText(R.string.connect_true);

        //Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        //mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        /*
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    //TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    //String message = textView.getText().toString();
                    //sendMessage(message);
                }
            }
        });
         */


        // Initialize the BluetoothChatService to perform bluetooth connections
        //mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        //mOutStringBuffer = new StringBuffer("");

        /*
        VERİ GÖNDERME METODU. sendMessage("String") ile tek karakterlik String veriler gönderilir.

        ör: String mesaj = "Hakan";
            sendMessage(mesaj);
        */
        //sendMessage(dataBitSet);
    }

    // BLUETOOTH CHAT SERVİCE CLASS'IINDAKİ sendMessage PARAMETRESİNE VERİYİ GÖNDERİRİZ
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
            //veriGonder.setText(mOutStringBuffer);
        }
    }

    /* The Handler that gets information back from the BluetoothChatService
    */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add(R.string.benn +":  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);

                    /*
                            MESAJ OKUMA MOTODU. OKUNAN MESAJLAR BURAYA DÜŞER

                            ör: byte[] readBuf = (byte[]) msg.obj;

                                String[] mesaj = readMessage.split(","); // gelen verileri virgüle göre ayırır

                                sicaklikTX.setText(mesaj[3]);

                    */

                    // BT İLE GELEN VERİ dataBitGet DEĞİŞKENİNE AKTARILIR
                    //dataBitGet = readMessage;

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void vtGoster(){
        Veritabani veritabani = new Veritabani(getActivity());
        try {

            // SQL verileri alır ve butonlar ile yazıları günceller
            SQLiteDatabase db1 = veritabani.getReadableDatabase();
            Cursor okunanlar = db1.query(VT, sutunlar, null, null, null, null, null);
            while (okunanlar.moveToNext()) {
                deklare[0] = okunanlar.getString(okunanlar.getColumnIndex("anahtar"));
                deklare[1] = okunanlar.getString(okunanlar.getColumnIndex("deger"));
                deklare[2] = okunanlar.getString(okunanlar.getColumnIndex("anahtar2"));
                deklare[3] = okunanlar.getString(okunanlar.getColumnIndex("deger2"));
                deklare[4] = okunanlar.getString(okunanlar.getColumnIndex("anahtar3"));
                deklare[5] = okunanlar.getString(okunanlar.getColumnIndex("deger3"));
                deklare[6] = okunanlar.getString(okunanlar.getColumnIndex("anahtar4"));
                deklare[7] = okunanlar.getString(okunanlar.getColumnIndex("deger4"));
                deklare[8] = okunanlar.getString(okunanlar.getColumnIndex("anahtar5"));
                deklare[9] = okunanlar.getString(okunanlar.getColumnIndex("deger5"));
                deklare[10] = okunanlar.getString(okunanlar.getColumnIndex("anahtar6"));
                deklare[11] = okunanlar.getString(okunanlar.getColumnIndex("deger6"));
                deklare[12] = okunanlar.getString(okunanlar.getColumnIndex("anahtar7"));
                deklare[13] = okunanlar.getString(okunanlar.getColumnIndex("deger7"));
                deklare[14] = okunanlar.getString(okunanlar.getColumnIndex("anahtar8"));
                deklare[15] = okunanlar.getString(okunanlar.getColumnIndex("deger8"));
                deklare[16] = okunanlar.getString(okunanlar.getColumnIndex("anahtar9"));
                deklare[17] = okunanlar.getString(okunanlar.getColumnIndex("deger9"));
                deklare[18] = okunanlar.getString(okunanlar.getColumnIndex("anahtarA"));
                deklare[19] = okunanlar.getString(okunanlar.getColumnIndex("anahtarB"));
                deklare[20] = okunanlar.getString(okunanlar.getColumnIndex("anahtarC"));


            }
        }finally {
            veritabani.close();
        }
    }

    // Veritabanına verileri kaydeder
    private void btnKaydet(String anahtar, String deger,
                           String anahtar2, String deger2,
                           String anahtar3, String deger3,
                           String anahtar4, String deger4,
                           String anahtar5, String deger5,
                           String anahtar6, String deger6,
                           String anahtar7, String deger7,
                           String anahtar8, String deger8,
                           String anahtar9, String deger9,
                           String anahtarA,
                           String anahtarB,
                           String anahtarC ){

        verit = new Veritabani(getActivity());
                try {
                    SQLiteDatabase db = verit.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put("anahtar",anahtar);
                    values.put("deger",deger);
                    values.put("anahtar2",anahtar2);
                    values.put("deger2",deger2);
                    values.put("anahtar3",anahtar3);
                    values.put("deger3",deger3);
                    values.put("anahtar4",anahtar4);
                    values.put("deger4",deger4);
                    values.put("anahtar5",anahtar5);
                    values.put("deger5",deger5);
                    values.put("anahtar6",anahtar6);
                    values.put("deger6",deger6);
                    values.put("anahtar7",anahtar7);
                    values.put("deger7",deger7);
                    values.put("anahtar8",anahtar8);
                    values.put("deger8",deger8);
                    values.put("anahtar9",anahtar9);
                    values.put("deger9",deger9);
                    values.put("anahtarA",anahtarA);
                    values.put("anahtarB",anahtarB);
                    values.put("anahtarC",anahtarC);

                    db.insertOrThrow(VT,null,values);
                }finally {
                    verit.close();
                }
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.keypad_menu, menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.customize_menu: {
                // Launch the DeviceListActivity to see devices and do scan
                /*
                Snackbar.make(getView(), "Buraya Fragment ekle", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                KeyPadCustom fragment = new KeyPadCustom();
                transaction.replace(R.id.sample_content_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                */
                return true;
            }

        }
        return false;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        //benitikla=(Button) getActivity().findViewById(R.id.bBeniTikla);
        //benitikla.setOnClickListener(this);
        //comm=(Communicator) getActivity();


        if (mChatService == null) {
            //setupChat();
        }

        // Activity yüklendiğinde snackbar çalışır
        snackbar = Snackbar.make(getView(), R.string.keypad_first, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_sari5));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
        snackbar.show();
    }
}
