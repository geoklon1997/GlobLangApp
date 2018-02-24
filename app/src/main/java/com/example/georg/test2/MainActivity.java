package com.example.georg.test2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage, welcomeMessage, fromLabel, toLabel, translatedText, selectLanguageLabel;
    public  EditText inputText;
    TranslationActivity translateTextObj;
    private Spinner spinner, spinner2,spinner3;
    private Button translateTextButton,translateCameraButton,reloadButton;
    private SurfaceView cameraView;
    private TextView cameraTextResult;
    private CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    public ArrayList<String> languagesList;
    public String fromLanguage,toLanguage;
    public ImageView imageLogo;
    public static boolean pictureText = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Here is a history of all your translations");
                    setTextToTextElementsVisibility(View.GONE);
                    setCameraToTextElementsVisibility(View.GONE);
                    setTranslationHistoryVisibility(View.VISIBLE);
                    pictureText=false;
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Please take a picture of some text and the app will automatically translate into another language");
                    setTextToTextElementsVisibility(View.GONE);
                    setTranslationHistoryVisibility(View.GONE);
                    setCameraToTextElementsVisibility(View.VISIBLE);
                    pictureText=true;
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText("Enter some text and translate it to another language");
                    setCameraToTextElementsVisibility(View.GONE);
                    setTranslationHistoryVisibility(View.GONE);
                    setTextToTextElementsVisibility(View.VISIBLE);
                    pictureText=false;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case RequestCameraPermissionID:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
;       languagesList = new ArrayList<>();
        languagesList.add("el");
        languagesList.add("it");
        languagesList.add("es");
        languagesList.add("bg");
        languagesList.add("en");
        languagesList.add("fr");
        languagesList.add("de");
        languagesList.add("ru");

        loadTextToTextElements();
       //TranslationActivity.fromLanguage=fromLanguage;
     //   TranslationActivity.toLanguage=toLanguage;
      //  TranslationActivity.translateedittext.setText(inputText.getText());
       // translatedText.setText(TranslationActivity.translated());
        loadCameraToTextElements();
        setCameraToTextElementsVisibility(View.GONE);

        welcomeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadCameraToTextElements() {
        cameraView = (SurfaceView) findViewById(R.id.surfaceView);
        cameraTextResult= (TextView) findViewById(R.id.text_camera);

        translateCameraButton = (Button) findViewById(R.id.button3);
       // selectLanguageLabel = (TextView) findViewById(R.id.textView5);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.availableLanguages, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                toLanguage = languagesList.get(pos);
                TranslationActivity.toLanguage=toLanguage;
                TranslationActivity.fromLanguage="en";

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies not available yet");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size()!=0) {
                        cameraTextResult.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                    cameraTextResult.setText(stringBuilder.toString());
                                }
                            }
                        });
                    }
                }
            });

        }
        translateCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TranslationActivity.class);
                inputText.setText(cameraTextResult.getText());
                intent.putExtra("translateTxt", inputText.getText().toString());
                intent.putExtra("isPictureToText", pictureText);
                startActivity(intent);
            }
        });

    }

    private void loadTextToTextElements() {
       // fromLabel = (TextView) findViewById(R.id.textView3);
        //toLabel = (TextView) findViewById(R.id.textView4);

        reloadButton = (Button) findViewById(R.id.button5);
        imageLogo = (ImageView) findViewById(R.id.imageView);
        mTextMessage = (TextView) findViewById(R.id.message);
        welcomeMessage = (TextView) findViewById(R.id.textView);

        translatedText = (TextView) findViewById(R.id.textView2);
        inputText = (EditText) findViewById(R.id.editText);

       // translatedText.setText(inputText.getText().toString());
        translateTextButton = (Button) findViewById(R.id.button);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.availableLanguages, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.availableLanguages, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        ;
      //  toLanguage = spinner2.getSelectedItem().toString();

       // translatedText.setText(inputText.getText().toString());

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
               toLanguage = languagesList.get(pos);
               TranslationActivity.toLanguage=toLanguage;

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                fromLanguage = languagesList.get(pos);
                TranslationActivity.fromLanguage=fromLanguage;

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        translateTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  TranslationActivity.translateedittext.setText(inputText.getText().toString());
                Intent intent = new Intent(MainActivity.this, TranslationActivity.class);
                intent.putExtra("translateTxt", inputText.getText().toString());
                intent.putExtra("isPictureToText", pictureText);
                startActivity(intent);
            }
        });

    }



    private void setTextToTextElementsVisibility(int visibility) {
        //fromLabel.setVisibility(visibility);
        inputText.setVisibility(visibility);
//        toLabel.setVisibility(visibility);
        translatedText.setVisibility(visibility);
        spinner.setVisibility(visibility);
        spinner2.setVisibility(visibility);
        translateTextButton.setVisibility(visibility);
        reloadButton.setVisibility(visibility);
        // translateTextButton.setVisibility(visibility);
        welcomeMessage.setVisibility(visibility);
        mTextMessage.setVisibility(visibility);
        //imageLogo.setVisibility(visibility);
    }

    private void setCameraToTextElementsVisibility(int visibility) {
        cameraTextResult.setVisibility(visibility);
        cameraView.setVisibility(visibility);
        translateCameraButton.setVisibility(visibility);
        spinner3.setVisibility(visibility);
     //   translateTextButton.setVisibility(visibility);
       // selectLanguageLabel.setVisibility(visibility);

    }

    private void setTranslationHistoryVisibility(int visibility) {
        welcomeMessage.setVisibility(visibility);
        mTextMessage.setVisibility(visibility);
    }
}
