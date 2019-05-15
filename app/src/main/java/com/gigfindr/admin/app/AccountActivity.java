

package com.gigfindr.admin.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dyhdyh.instagram.login.InstagramAuthDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class AccountActivity extends AppCompatActivity implements OnClickListener {

    private UserDetails userDetails;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private  StorageReference storageReference;

    private String uid;
    private RelativeLayout relativeLayoutLoading, relativeLayoutSaveEditLoader1;
    private LinearLayout linearLayoutSaveEdit;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String instagramName = "";
    private String prevAct;

    private RelativeLayout relativeLayoutBandPic, relativeLayoutChangePic;
    private LinearLayout linearLayoutIG;
    private ImageView imageViewBandPicEdit, ivBandPicEmpty;
    private EditText editTextName, editTextAbout;
    private TextView tvIGstatus;
    private Button btnSave;
    private RelativeLayout relativeLayoutLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        relativeLayoutBandPic = findViewById(R.id.relativeLayoutImage);
        relativeLayoutChangePic = findViewById(R.id.relativeLayoutChangePic);
        ivBandPicEmpty = findViewById(R.id.ivBandPicEmpty);
        relativeLayoutChangePic.setVisibility(View.GONE);
        btnSave = findViewById(R.id.btnSave);
        linearLayoutIG = findViewById(R.id.iconInstagramEdit);
        imageViewBandPicEdit = findViewById(R.id.ivBandPic);
        editTextName = findViewById(R.id.etName);
        editTextAbout = findViewById(R.id.etAbout);
        tvIGstatus = findViewById(R.id.tvIGstatus);
        relativeLayoutLogout = findViewById(R.id.ivLogout);
        btnSave.setOnClickListener(this);
        relativeLayoutLogout.setOnClickListener(this);
        linearLayoutIG.setOnClickListener(this);

        Intent mIntent = getIntent();
        prevAct = mIntent.getStringExtra("FROM_ACTIVITY");


        editTextAbout.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() ==       KeyEvent.KEYCODE_ENTER) {
                    Log.i("event", "captured");
                    return true;
                }

                return false;
            }
        });

        relativeLayoutBandPic.setOnClickListener(this);
        relativeLayoutChangePic.setOnClickListener(this);

        relativeLayoutSaveEditLoader1 = findViewById(R.id.saveditloader1);
        linearLayoutSaveEdit = findViewById(R.id.saveditloader2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), MapActivity.class);
                    startActivity(intent);

            }
        });

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        relativeLayoutLoading = findViewById(R.id.spinnerkitloader);
        relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
        linearLayoutSaveEdit.setVisibility(View.GONE);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(3000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread

                        if(imageViewBandPicEdit.getDrawable() == null){

                            //run another loop
                            final Handler handler1 = new Handler();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                    }
                                    catch (Exception e){}

                                    handler1.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            relativeLayoutLoading.setVisibility(View.INVISIBLE);
                                            if(imageViewBandPicEdit.getDrawable() != null){
                                                relativeLayoutChangePic.setVisibility(View.VISIBLE);
                                                ivBandPicEmpty.setVisibility(View.INVISIBLE);
                                            }
                                            else{
                                                relativeLayoutChangePic.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }
                        else{
                            relativeLayoutLoading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }).start();

        read();
        clearGlideCache();
    }

    public void read(){

        //reading of details
        databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userDetails = dataSnapshot.getValue(UserDetails.class);
                if(userDetails != null) {

                    editTextName.setText(userDetails.getName());
                    editTextAbout.setText(userDetails.getAbout());


                    if(userDetails.getinstagramName() != null){
                        if(!userDetails.getinstagramName().equals("")) {
                            tvIGstatus.setText(userDetails.getinstagramName());
                        }
                    }

                    databaseReference.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        reading of image
        final StorageReference pathref = storageReference.child("userid"+ uid +"/pic.jpg");
        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplication().getApplicationContext())
                        .load(uri.toString())
                        .into(imageViewBandPicEdit);

                relativeLayoutLoading.setVisibility(View.INVISIBLE);
                relativeLayoutChangePic.setVisibility(View.VISIBLE);
                ivBandPicEmpty.setVisibility(View.INVISIBLE);


            }

        });
    }

    @Override
    public void onClick(View view) {

        //card2
        if(view.equals(relativeLayoutChangePic) || view.equals(relativeLayoutBandPic)){
            showFileChooser();
        }
        else if(view == btnSave){
            saveAndEdit();
        }
        else if(view.equals(linearLayoutIG)){

            if(tvIGstatus.getText().toString().equalsIgnoreCase("connect")) {

                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.instagramdialog);

                // set the custom dialog components - text, image and button
                final EditText etIGname = dialog.findViewById(R.id.etIGName);
                RelativeLayout tvOk = dialog.findViewById(R.id.tvconfirm);
                RelativeLayout tvCancel = dialog.findViewById(R.id.tvcancel);

                tvOk.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!etIGname.getText().toString().equals("")){
                            tvIGstatus.setText(etIGname.getText().toString());
                            String name, about, igName;
                            name = editTextName.getText().toString();
                            about = editTextAbout.getText().toString();
                            igName = tvIGstatus.getText().toString();

                            userDetails = new UserDetails(name, about, igName);

                            databaseReference.child("User").child(uid).setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
                                    linearLayoutSaveEdit.setVisibility(View.GONE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    dialog.dismiss();
                                    Toasty.success(AccountActivity.this, "Instagram saved", Toast.LENGTH_SHORT, true).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.error(AccountActivity.this, "Save unsuccessful, " + e.getMessage().toString(), Toast.LENGTH_SHORT, true).show();

                                }
                            });
                        }
                        else{
                            Toasty.error(AccountActivity.this, "Please input Instagram handler", Toast.LENGTH_SHORT, true).show();
                        }


                    }
                });

                tvCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            else{
                removeFBandINSTA();
            }
        }
        else if(view.equals(relativeLayoutLogout)){

            final Dialog dialog = new Dialog(AccountActivity.this);
            dialog.setContentView(R.layout.logoutdialog);
            dialog.show();

            final RelativeLayout btnYes = dialog.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), SignInSignUpActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("logout", "logout");
                    intent.putExtras(extras);
                    startActivity(intent);

                    Toasty.success(AccountActivity.this, "Logout Successful", Toast.LENGTH_LONG, true).show();
                    dialog.dismiss();
                }
            });

            final RelativeLayout btnNo = dialog.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

    }

    private void removeFBandINSTA(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accountactivity_logoutsocialmedia);

        RelativeLayout relativeLayoutRemove = dialog.findViewById(R.id.relativeLayoutRemove);
        RelativeLayout relativeLayoutCancel = dialog.findViewById(R.id.relativeLayoutCancel);


        relativeLayoutRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new InstagramAuthDialog(AccountActivity.this).clearAuthCookie();
                instagramName = "";

                UserDetails userDetails = new UserDetails(editTextName.getText().toString(), editTextAbout.getText().toString(), "");
                databaseReference.child("User").child(uid).setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        tvIGstatus.setText("CONNECT");
                    }
                });


            }
        });

        relativeLayoutCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Glide.with(getApplication().getApplicationContext())
                        .load(bitmap)
                        .into(imageViewBandPicEdit);

//                imageViewBandPicEdit.setImageBitmap(bitmap);

                StorageReference riversRef = storageReference.child("userid" + uid + "/pic.jpg");
                riversRef.putFile(filePath);

                relativeLayoutChangePic.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //this method will upload the file
    private void saveAndEdit() {

        if(!editTextName.getText().toString().equalsIgnoreCase("") && !editTextAbout.getText().toString().equalsIgnoreCase("")) {
            System.out.println("etabout: " + editTextAbout.getText().toString());

            if (filePath != null) {

                relativeLayoutSaveEditLoader1.setVisibility(View.VISIBLE);
                linearLayoutSaveEdit.setVisibility(View.VISIBLE);

                StorageReference riversRef = storageReference.child("userid" + uid + "/pic.jpg");
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                            @Override
                            public void onSuccess(TaskSnapshot taskSnapshot) {
                                read();

                                String name, about, igName;
                                name = editTextName.getText().toString();
                                about = editTextAbout.getText().toString();
                                igName = tvIGstatus.getText().toString();

                                if (!igName.equalsIgnoreCase("connect")) {
                                    userDetails = new UserDetails(name, about, igName);
                                } else {
                                    userDetails = new UserDetails(name, about, "");
                                }

                                databaseReference.child("User").child(uid).setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
                                        linearLayoutSaveEdit.setVisibility(View.GONE);
                                        Toasty.success(AccountActivity.this, "Save successful", Toast.LENGTH_SHORT, true).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(AccountActivity.this, "Save unsuccessful, " + e.getMessage().toString(), Toast.LENGTH_SHORT, true).show();

                                    }
                                });

                                databaseReference.child("Show").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                            Log.d("accact:: child.getkey: ", child.getKey());
                                            ShowDetails showDetails = child.getValue(ShowDetails.class);
                                            Log.d("accact:: showdets ", showDetails.toString());
                                            if (showDetails.getUserid().equals(uid)) {
                                                showDetails.setBandName(name);
                                                databaseReference.child("Show").child(child.getKey()).setValue(showDetails);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
                                linearLayoutSaveEdit.setVisibility(View.GONE);

                                //and displaying error message
                                Toasty.error(AccountActivity.this, "Save failed, please try again", Toast.LENGTH_LONG, true).show();

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                                //calculating progress percentage
                                @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            }
                        });
            }
            //filePath is empty
            else {

                relativeLayoutSaveEditLoader1.setVisibility(View.VISIBLE);
                linearLayoutSaveEdit.setVisibility(View.VISIBLE);

                final String name2 = editTextName.getText().toString();
                final String about2 = editTextAbout.getText().toString();
                userDetails = new UserDetails(name2, about2, instagramName);
                databaseReference.child("User").child(uid).setValue(userDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                read();

                                String name, about, igName;
                                name = editTextName.getText().toString();
                                about = editTextAbout.getText().toString();
                                igName = tvIGstatus.getText().toString();
                                if (!igName.equalsIgnoreCase("Connect")) {
                                    userDetails = new UserDetails(name, about, igName);
                                } else{
                                    userDetails = new UserDetails(name, about, "");
                                }
                                databaseReference.child("User").child(uid).setValue(userDetails);

                                databaseReference.child("Show").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                            Log.d("accact:: child.getkey: ", child.getKey());
                                            ShowDetails showDetails = child.getValue(ShowDetails.class);
                                            Log.d("accact:: showdets ", showDetails.toString());
                                            if (showDetails.getUserid().equals(uid)) {
                                                showDetails.setBandName(name);
                                                databaseReference.child("Show").child(child.getKey()).setValue(showDetails);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                final Handler handler = new Handler();
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (Exception e) {
                                        } // Just catch the InterruptedException

                                        // Now we use the Handler to post back to the main thread
                                        handler.post(new Runnable() {
                                            public void run() {
                                                // Set the View's visibility back on the main UI Thread
                                                relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
                                                linearLayoutSaveEdit.setVisibility(View.GONE);

                                                Toasty.success(AccountActivity.this, "Save successful", Toast.LENGTH_SHORT, true).show();

                                            }
                                        });
                                    }
                                }).start();
                            }
                        });

            }
        }
        else{
            Toasty.error(AccountActivity.this, "Please input Name and Introduction", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AccountActivity.this, MapActivity.class);
                startActivity(intent);
//                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearGlideCache() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.get(AccountActivity.this).clearMemory();
            }}, 0);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Glide.get(AccountActivity.this).clearDiskCache();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AccountActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
