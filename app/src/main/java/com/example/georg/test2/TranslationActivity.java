package com.example.georg.test2;

/**
 * Created by georg on 18/02/2018.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TranslationActivity extends AppCompatActivity{

        public static GoogleTranslateActivity translator;
        public static EditText translateedittext;
        public static TextView translatabletext,translatedPictureText;
        public static String fromLanguage="en";
        public static String toLanguage="el";
        public boolean isPicture=false;
        String text = "";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
           translateedittext = (EditText) findViewById(R.id.editText);
            text = (String) getIntent().getSerializableExtra("translateTxt");
           // translateedittext.setHint("");
            isPicture=(Boolean) getIntent().getSerializableExtra("isPictureToText");
            translateedittext.setText(text);
            Button reloadButton = (Button) findViewById(R.id.button5);
            reloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TranslationActivity.this, MainActivity.class);
//                    MainActivity.inputText.setText("");
                    //translatabletext.setText("Translated text");
                    //intent.putExtra("translateTxt", inputText.getText());
                    startActivity(intent);                }
            });
                    new EnglishToTagalog().execute();
            }


        private class EnglishToTagalog extends AsyncTask<Void, Void, Void> {
            private ProgressDialog progress = null;

            protected void onError(Exception ex) {

            }
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    translator = new GoogleTranslateActivity("AIzaSyARSbBKcVoAg8u8XnZzk4lV_foYSBWZwSg");

                    Thread.sleep(1000);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;

            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onPreExecute() {
                //start the progress dialog
                progress = ProgressDialog.show(TranslationActivity.this, null, "Translating...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Void result) {
                progress.dismiss();

                super.onPostExecute(result);
                translated();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

        }


        public void translated(){


            String translatetotagalog = translateedittext.getText().toString();//get the value of text
            String text = translator.translate(translatetotagalog, fromLanguage, toLanguage);
            translatabletext = (TextView) findViewById(R.id.textView2);
            translatabletext.setText(text);

        }



}
