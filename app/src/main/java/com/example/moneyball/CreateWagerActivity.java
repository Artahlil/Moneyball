package com.example.moneyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateWagerActivity extends AppCompatActivity {
    private final int PICK_IMAGE = 1;
    ImageButton upload;
    Drawable bg;
    Uri selectedImageUri;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    String uriStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wager);
        upload = findViewById(R.id.uploadGroupPic);
        final TextView uploadTV = findViewById(R.id.uploadTV);
        final EditText heading = findViewById(R.id.heading);
        final EditText description = findViewById(R.id.description);
//        final EditText groupName = findViewById(R.id.groupName);
        final EditText betValText = findViewById(R.id.betVal);
        final EditText potentialChallengeText = findViewById(R.id.potentialChallenge);
        Button exit = findViewById(R.id.exit);
        Button done = findViewById(R.id.done);
        Intent groupInfo = getIntent();
        final String groupId = groupInfo.getStringExtra("groupId");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTV.setVisibility(View.GONE);

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });

        final Intent intent = new Intent();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headingText = heading.getText().toString();
                String descriptionText = description.getText().toString();
                String groupIdToPass = groupId;
                String valBet = betValText.getText().toString();
                if(!valBet.contains("."))
                    valBet=valBet+".00";
                double betVal = Double.parseDouble(valBet);
                String potentialChallenge = potentialChallengeText.getText().toString();
                Log.d("tag", groupIdToPass);
                //DatabaseReference ref = database.getReference();
                //DatabaseReference wagerRef = ref.child("wagers").push();
                /*
                groupRef.child("picture").setValue(selectedImageUri.toString());
                groupRef.child("heading").setValue(headingText);
                groupRef.child("description").setValue(descriptionText);
                groupRef.child("groupName").setValue(groupNameText);
                */
//                intent.putExtra("pic", selectedImageUri.toString());
//                intent.putExtra("headingText", headingText);
//                intent.putExtra("descriptionText", descriptionText);
//                intent.putExtra("groupNameText", groupIdToPass);
//                setResult(RESULT_OK, intent);
//                finish();

                if(headingText.equals("")|| descriptionText.equals("")){
                    Toast.makeText(getApplicationContext(),  "Please enter wager information for all text fields", Toast.LENGTH_SHORT).show();
                }

                else if (potentialChallenge.equals(""))
                    Toast.makeText(getApplicationContext(),  "Please enter one potential challenge", Toast.LENGTH_SHORT).show();

                else {
                    if(selectedImageUri==null || selectedImageUri.toString().equals("")){
                        uriStr = ""; //Toast.makeText(getApplicationContext(),  "add pic", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        uriStr = selectedImageUri.toString();
                    }
                    Toast.makeText(getApplicationContext(),  "Done!", Toast.LENGTH_SHORT).show();
                    intent.putExtra("pic", uriStr);
                    intent.putExtra("headingText", headingText);
                    intent.putExtra("descriptionText", descriptionText);
                    intent.putExtra("groupIdToPass", groupIdToPass);
                    intent.putExtra("betVal", betVal);
                    intent.putExtra("potentialChallenge", potentialChallenge);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if(resultCode==RESULT_OK){
                selectedImageUri = data.getData();
                upload.setVisibility(View.GONE);
                RelativeLayout layout = findViewById(R.id.groupPicHolder);
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
                layout.setBackground(bg);
            }
        }
    }
}
