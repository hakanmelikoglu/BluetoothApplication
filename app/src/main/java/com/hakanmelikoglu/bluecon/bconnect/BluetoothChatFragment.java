package com.hakanmelikoglu.bluecon.bconnect;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static com.hakanmelikoglu.bluecon.bconnect.R.string.baglandi_yazi;


/**
 * Created by hakan on 21.04.2016.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    /**
     * Bağlı cihazın adı
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * Giden iletiler için dize arabellek
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Chat service için üye nesne
     */
    private BluetoothChatService mChatService = null;

    /*


    Özel deklarasyon
     */
    boolean baglandi = false;

    String alinanVeri;
    String uuidDegeri;
    String secureVeri = "fa87c0d0-afac-11de-8a39-0800200c9a66";
    String insecureVeri = "00001101-0000-1000-8000-00805f9b34fb";

    Snackbar snackbar;

    //Veritabani v2;
    private static final String VT2 = "uuid";
    public String[] sutunlar2 = {"secure", "insecure"};

    private static final String VT3 = "giden";
    public String[] sutunlar3 = {"databitset"};
    private static final String VT4 = "gelen";
    public String[] sutunlar4 = {"databitget"};

    public String dataBitSet; // giden veri
    public String dataBitGet; // gelen veri

    private Chat chat;
    public String okunanVeri;
////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth kullanılamıyor", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final TextView yaziIlk = (TextView)getActivity().findViewById(R.id.textView21);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        final Button btnUuid  =(Button)getActivity().findViewById(R.id.button24);
        final Button btnSecure = (Button)getActivity().findViewById(R.id.button25);
        final Button btnInsecure = (Button) getActivity().findViewById(R.id.button26);
        final Button btnDisco = (Button)getActivity().findViewById(R.id.button27);
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult

        // Floaty Action Bar tıklanınca BT açar
        fab.setImageResource(R.drawable.ic_bluetooth_searching_black_48dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yaziIlk.setText(R.string.ilk_fragment);
                //btnBaglan.setBackgroundResource(R.drawable.bt_icon_close);

                snackbar = Snackbar.make(view, R.string.select_device, Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_sari7));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                snackbar.show();

                if (!mBluetoothAdapter.isEnabled()) {

                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

                    // Otherwise, setup the chat session
                }

                btnSecure.setEnabled(true);
                btnInsecure.setEnabled(true);

                hideKeyboard(view);
            }
        });


        // UUID değiştir
        btnUuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(getActivity());

                final View textEntryView = factory.inflate(R.layout.text_entry_iki, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText12);
                final EditText input2 = (EditText)textEntryView.findViewById(R.id.editText2);

                input1.setText(secureVeri, TextView.BufferType.EDITABLE);
                input2.setText(insecureVeri, TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setIcon(R.drawable.ic_mode_edit_black_48dp).setTitle(R.string.uuid_name).setView(textEntryView).setPositiveButton(R.string.kaydett,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Veritabanına secure ve insecure değerlerini kaydeder
                                vtKaydet(input1.getText().toString(), input2.getText().toString());

                                snackbar = Snackbar.make(getView(), R.string.uuid_save, Snackbar.LENGTH_SHORT);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_sari5));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                                snackbar.show();
                                hideKeyboard(textEntryView);

                            }
                        }).setNegativeButton(R.string.iptal,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        });
                alert.show();
            }
        });

        // Secure cihazlara bağlan
        btnSecure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

            }
        });

        // Insecure cihazlara bağlan
        btnInsecure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);

            }
        });

        // Görünür yap
        btnDisco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureDiscoverable();

            }
        });
    }

/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
////////////////////////////////////////////////////////////////////////////////////////////////////////

        Button btnUuid  =(Button)view.findViewById(R.id.button24);
        Button btnSecure = (Button)view.findViewById(R.id.button25);
        Button btnInsecure = (Button) view.findViewById(R.id.button26);
        Button btnDisco = (Button)view.findViewById(R.id.button27);

        // Veritabanından verileri çeker secureVeri ve insecureVeri değişkenlerine atar
        vtGoster();

        // Veritabanı den verileri alır ve databitset değişkenlerine atar
        //vt3al();

        /*
        // UUID güncelleme
        UUID SECURE = UUID.fromString(secureVeri);
        UUID INSECURE = UUID.fromString(insecureVeri);

        // BluetoothChatService birşey değiştirmek için mutlaka null sorgusu yapılmalıdır
        if (mChatService != null) {
            mChatService.MY_UUID_SECURE = SECURE;
            mChatService.MY_UUID_INSECURE = INSECURE;
        }
*/


        // VT ile gelen veri GET sistemine kaydedilir. Veritabanı
        //vt4gonder(dataBitGet);




////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return view;
    }

    private void vtGoster(){
        Veritabani veritabani = new Veritabani(getActivity());
        try {

            // SQL verileri alır
            SQLiteDatabase db1 = veritabani.getReadableDatabase();
            Cursor okunanlar = db1.query(VT2, sutunlar2, null, null, null, null, null);
            while (okunanlar.moveToNext()) {
                secureVeri = okunanlar.getString(okunanlar.getColumnIndex("secure"));
                insecureVeri = okunanlar.getString(okunanlar.getColumnIndex("insecure"));
            }
        }finally {
            veritabani.close();
        }
    }

    private void vtKaydet(String secureV, String insecureV){
        Veritabani vt = new Veritabani(getActivity());
        try{
            SQLiteDatabase db2 = vt.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("secure", secureV);
            values.put("insecure", insecureV);

            db2.insertOrThrow(VT2, null, values);
        }finally {
            vt.close();
        }
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Bu listView gelen ve giden verileri ekrana dizer. Chat gibi
        //mConversationView = (ListView) view.findViewById(R.id.in);

    }


    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus(getString(baglandi_yazi, mConnectedDeviceName));
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

                // BLUETOOTH İLE VERİ GÖNDERME
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);// GÖNDERİLECEK VERİ

                    mConversationArrayAdapter.add("Ben:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    readMessage = okunanVeri;
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
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


    private void setupChat() {
        //Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        //mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        /*
        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });
*/
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
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
    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
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
    /**
     * The Handler that gets information back from the BluetoothChatService
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();

                } else {
                    // User did not enable Bluetooth or an error occurred
                    //Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }
    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
        //baglandi = true;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }

        return false;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

}
