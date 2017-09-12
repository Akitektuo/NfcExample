package com.akitektuo.nfctest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private NfcAdapter nfcAdapter;
    private TextView textNfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textNfc = (TextView) findViewById(R.id.text_nfc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            //Use other method to send data
            Toast.makeText(this, "Your phone does not have NFC.", Toast.LENGTH_SHORT).show();
        } else if (nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC turned on.", Toast.LENGTH_SHORT).show();
            //do the transaction
        } else {
            Toast.makeText(this, "NFC turned off.", Toast.LENGTH_SHORT).show();
            //turn nfc on programmatically
        }
        nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            textNfc.setText(new String(message.getRecords()[0].getPayload()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = "Beam me up, Android!";
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        return new NdefMessage(ndefRecord);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String secondTest = "SecondTest";
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            List<Parcelable> rawMessages = intent.getParcelableArrayListExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                List<NdefMessage> messages = new ArrayList<>();
                for (Parcelable rawMessage : rawMessages) {
                    messages.add((NdefMessage) rawMessage);
                }
                for (NdefMessage message : messages) {
                    System.out.println(message.toString());
                    secondTest += "\n" + message.toString();
                    System.out.println(new String(message.getRecords()[0].getPayload()));
                    secondTest += "\n" + new String(message.getRecords()[0].getPayload());
                }
            }
        }
        Toast.makeText(this, secondTest, Toast.LENGTH_LONG).show();
        textNfc.setText(secondTest);
    }
}
