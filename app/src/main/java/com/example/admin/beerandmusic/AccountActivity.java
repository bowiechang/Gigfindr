//package com.example.admin.beerandmusic;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnKeyListener;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.dyhdyh.instagram.login.InstagramAuthDialog;
//import com.dyhdyh.instagram.login.InstagramRequest;
//import com.facebook.CallbackManager;
//import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.google.firebase.storage.UploadTask.TaskSnapshot;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import es.dmoral.toasty.Toasty;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class AccountActivity extends AppCompatActivity implements OnClickListener {
//
////    ImageView imageView;
////    TextView tvBandName, tvAboutBand;
////    Button btnLogout, btnDelete;
//
//    private UserDetails userDetails;
//    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//    private static final int PICK_IMAGE_REQUEST = 234;
//    private Uri filePath;
//    private  StorageReference storageReference;
//
//    private String uid;
//    private RelativeLayout relativeLayoutLoading, relativeLayoutSaveEditLoader1;
////    private RelativeLayout relativeLayoutName, relativeLayoutAbout,;
////    private RelativeLayout relativeLayoutIv1, relativeLayoutIv2, relativeLayoutIv3;
//
//    private LinearLayout linearLayoutSaveEdit;
////    private TextView tvName, tvAbout;
////    private EditText etName, etAbout;
////
////    private ImageView ivBandpicture;
////    private Button btnEditSave;
//
////    private RelativeLayout rvLogout;
//
//    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//
////    private CircleImageView circleImageView;
//
//
//
//    private String instagramUID = "";
//    private String instagramName = "";
//
//    //card1
//    private CardView cardView1;
//    private TextView textViewName;
//    private TextView textViewAbout;
//    private TextView textViewEmpty;
//    private TextView textViewInstagram;
//    private ImageView imageViewBandPic, imageViewBandPicEmpty;
//    private RelativeLayout relativeLayoutLogOut;
//    private RelativeLayout relativeLayoutEdit;
//    private RelativeLayout relativeLayoutInstagram;
//
//
//    //card2
//    private CardView cardView2;
//    private RelativeLayout relativeLayoutBandPic;
//    private RelativeLayout linearLayoutSave;
//    private ImageView imageViewBandPicEdit;
//    private EditText editTextName, editTextAbout;
//    private ImageView imageViewBandPicEditEmpty;
//    private TextView textViewEditEmpty;
//    private RelativeLayout relativeLayoutBlackScreenEdit;
//    private TextView textViewBlackScreenEdit;
//    private RelativeLayout relativeLayoutInstagramEdit;
//    private TextView textViewInstagramEdit;
//    private LoginButton loginButton;
//
//    private CallbackManager callbackManager;
//    private static final String EMAIL = "email";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account2);
//
//        //new
//
//        //cardview1
//
//        cardView1 = findViewById(R.id.cardview1);
//        textViewName = findViewById(R.id.tvName);
//        textViewAbout = findViewById(R.id.tvAbout);
//        textViewEmpty = findViewById(R.id.ivBandPicEmptyTextView);
//        textViewInstagram = findViewById(R.id.tvInstagram);
//        imageViewBandPic = findViewById(R.id.ivBandPic);
//        imageViewBandPicEmpty = findViewById(R.id.ivBandPicEmpty);
//        relativeLayoutLogOut = findViewById(R.id.relativelLayoutSignOut);
//        relativeLayoutEdit = findViewById(R.id.relativelayoutEdit);
//        relativeLayoutInstagram = findViewById(R.id.iconInstagram);
//
//        relativeLayoutLogOut.setOnClickListener(this);
//        relativeLayoutEdit.setOnClickListener(this);
//        cardView1.setOnClickListener(this);
//
//        //cardview2
//
//        cardView2 = findViewById(R.id.cardview2);
//        relativeLayoutBandPic = findViewById(R.id.relativeLayoutBandPic);
//        linearLayoutSave = findViewById(R.id.linearLayoutSave);
//        imageViewBandPicEdit = findViewById(R.id.ivBandPicEdit);
//        imageViewBandPicEditEmpty = findViewById(R.id.ivBandPicEditEmpty);
//        editTextName = findViewById(R.id.etName);
//        editTextAbout = findViewById(R.id.etAbout);
//        textViewEditEmpty = findViewById(R.id.ivBandPicEditEmptyTextView);
//        textViewBlackScreenEdit = findViewById(R.id.textViewBandPicBlackScreenEdit);
//        relativeLayoutBlackScreenEdit = findViewById(R.id.relativeLayoutBandPicBlackScreenEdit);
//        relativeLayoutInstagramEdit = findViewById(R.id.iconInstagramEdit);
//        textViewInstagramEdit = findViewById(R.id.tvInstagramEdit);
//
//        editTextAbout.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_DOWN
//                        && event.getKeyCode() ==       KeyEvent.KEYCODE_ENTER) {
//                    Log.i("event", "captured");
//                    return true;
//                }
//
//                return false;
//            }
//        });
//
//        linearLayoutSave.setOnClickListener(this);
//        relativeLayoutBandPic.setOnClickListener(this);
//        relativeLayoutInstagramEdit.setOnClickListener(this);
//
//        //new
//
//        relativeLayoutSaveEditLoader1 = findViewById(R.id.saveditloader1);
//        linearLayoutSaveEdit = findViewById(R.id.saveditloader2);
//
////        rvLogout = findViewById(R.id.relativeLayoutLogout);
//
////        relativeLayoutName.setOnClickListener(this);
////        relativeLayoutAbout.setOnClickListener(this);
////        ivBandpicture.setOnClickListener(this);
////        rvLogout.setOnClickListener(this);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//
//        toolbar.setNavigationOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent myIntent = new Intent(AccountActivity.this, MapActivity.class);
//                AccountActivity.this.startActivity(myIntent);
//
//            }
//        });
//
//        Window window = getWindow();
//        window.setStatusBarColor(Color.BLACK);
//
//        storageReference = FirebaseStorage.getInstance().getReference();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
//
//        relativeLayoutLoading = findViewById(R.id.spinnerkitloader);
//        relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
//        linearLayoutSaveEdit.setVisibility(View.GONE);
//        cardView2.setVisibility(View.GONE);
//        imageViewBandPic.setVisibility(View.GONE);
////        relativeLayoutIv1.setVisibility(View.GONE);
////        relativeLayoutIv2.setVisibility(View.GONE);
//
//        final Handler handler = new Handler();
//        new Thread(new Runnable() {
//            public void run() {
//                try{
//                    Thread.sleep(4000);
//                }
//                catch (Exception e) { } // Just catch the InterruptedException
//
//                // Now we use the Handler to post back to the main thread
//                handler.post(new Runnable() {
//                    public void run() {
//                        // Set the View's visibility back on the main UI Thread
//
//                        if(imageViewBandPic.getDrawable() == null){
//
//                            //run another loop
//                            final Handler handler1 = new Handler();
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        Thread.sleep(4000);
//                                    }
//                                    catch (Exception e){}
//
//                                    handler1.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            relativeLayoutLoading.setVisibility(View.INVISIBLE);
//                                            if(imageViewBandPic.getDrawable() != null){
//
//                                                imageViewBandPic.setVisibility(View.VISIBLE);
//                                                imageViewBandPicEmpty.setVisibility(View.GONE);
//                                                imageViewBandPicEditEmpty.setVisibility(View.GONE);
//                                                textViewEditEmpty.setVisibility(View.GONE);
//                                                textViewEmpty.setVisibility(View.GONE);
//
//                                            }
//                                            else{
//                                                relativeLayoutBlackScreenEdit.setVisibility(View.GONE);
//                                                textViewBlackScreenEdit.setVisibility(View.GONE);
//                                            }
//
//                                        }
//                                    });
//                                }
//                            }).start();
//
//
//                        }
//                        else{
//                            relativeLayoutLoading.setVisibility(View.INVISIBLE);
//
////                            imageViewBandPic.setVisibility(View.VISIBLE);
////                            imageViewBandPicEmpty.setVisibility(View.GONE);
////                            imageViewBandPicEditEmpty.setVisibility(View.GONE);
////                            textViewEditEmpty.setVisibility(View.GONE);
////                            textViewEmpty.setVisibility(View.GONE);
//                        }
//
//                    }
//                });
//            }
//        }).start();
//
//        read();
//        clearGlideCache();
//    }
//
//    public void read(){
//
//        //reading of details
//        databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                userDetails = dataSnapshot.getValue(UserDetails.class);
//                if(userDetails != null) {
//                    textViewAbout.setText(userDetails.getAbout());
//                    textViewName.setText(userDetails.getName());
//
//                    editTextName.setText(userDetails.getName());
//                    editTextAbout.setText(userDetails.getAbout());
//
//                    if(userDetails.getInstagramUID() != null){
//                        if(!userDetails.getInstagramUID().equals("")) {
//                            textViewInstagram.setText(R.string.accountactivity_remove);
//                            textViewInstagramEdit.setText(R.string.accountactivity_remove);
//
//                            instagramUID = userDetails.getInstagramUID();
//                        }
//                    }
//
//                    databaseReference.removeEventListener(this);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
////        reading of image
//        final StorageReference pathref = storageReference.child("userid"+ uid +"/pic.jpg");
//        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("success uri", uri.toString());
//
//                // Load the image using Glide
//                Glide.with(getApplication().getApplicationContext())
//                        .load(uri.toString())
////                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                        .into(imageViewBandPic);
//
//                Glide.with(getApplication().getApplicationContext())
//                        .load(uri.toString())
////                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                        .into(imageViewBandPicEdit);
//
//                relativeLayoutLoading.setVisibility(View.INVISIBLE);
//
//                imageViewBandPic.setVisibility(View.VISIBLE);
//                imageViewBandPicEmpty.setVisibility(View.GONE);
//                imageViewBandPicEditEmpty.setVisibility(View.GONE);
//                textViewEditEmpty.setVisibility(View.GONE);
//                textViewEmpty.setVisibility(View.GONE);
//
////                relativeLayoutIv3.setVisibility(View.GONE);
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("failed uri", "failed: " + e.toString());
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        //card1
//        if(view == relativeLayoutLogOut){
//
//            final Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.dialog_accountactivity_logoutaccount);
//
//            // set the custom dialog components - text, image and button
//            RelativeLayout relativeLayoutLogout = dialog.findViewById(R.id.relativeLayoutRemove);
//            RelativeLayout relativeLayoutCancel = dialog.findViewById(R.id.relativeLayoutCancel);
//
//            relativeLayoutLogout.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    firebaseAuth.signOut();
//                    Intent intent = new Intent(getApplicationContext(), SignInSignUpActivity.class);
//                    Bundle extras = new Bundle();
//                    extras.putString("logout", "logout");
//                    intent.putExtras(extras);
//                    startActivity(intent);
//
//                    Toasty.success(AccountActivity.this, "Logout Successful", Toast.LENGTH_LONG, true).show();
//
//                    dialog.dismiss();
//                }
//            });
//
//            relativeLayoutCancel.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//
//            dialog.show();
//
//        }
//        else if(view == relativeLayoutEdit || view == cardView1){
//
//            cardView1.setVisibility(View.GONE);
//            cardView2.setVisibility(View.VISIBLE);
//        }
//
//
//        //card2
//        else if(view == relativeLayoutBandPic){
//
//            showFileChooser();
//        }
//        else if(view == linearLayoutSave){
//            saveAndEdit();
//        }
//        else if(view == relativeLayoutInstagramEdit){
//            if(!instagramUID.equals("")){
//
//                removeFBandINSTA();
//            }
//            else {
//                clickAccessToken();
//            }
//        }
//    }
//
////        if(view == btnEditSave) {
////
////            if (btnEditSave.getText().equals("SAVE")) {
////                btnEditSave.setText("EDIT");
////                saveAndEdit();
////            } else {
////
////                btnEditSave.setText("SAVE");
////                tvName.setVisibility(View.GONE);
////                tvAbout.setVisibility(View.GONE);
////
////                etName.setVisibility(View.VISIBLE);
////                etAbout.setVisibility(View.VISIBLE);
////
////                if (!tvName.getText().equals(" ") || !tvAbout.getText().equals(" ")) {
////                    etName.setText(tvName.getText());
////                    etAbout.setText(tvAbout.getText());
////                }
////
//////                if (ivBandpicture.getDrawable() != null) {
//////                    relativeLayoutIv1.setVisibility(View.VISIBLE);
//////                    relativeLayoutIv2.setVisibility(View.VISIBLE);
//////                } else if (ivBandpicture.getDrawable() == null) {
//////                    relativeLayoutIv3.setVisibility(View.VISIBLE);
//////                }
////
//////                if (circleImageView.getDrawable() != null) {
//////                    relativeLayoutIv1.setVisibility(View.VISIBLE);
//////                    relativeLayoutIv2.setVisibility(View.VISIBLE);
//////                } else if (circleImageView.getDrawable() == null) {
//////                    relativeLayoutIv3.setVisibility(View.VISIBLE);
//////                }
////            }
////        }
////        else if(view == circleImageView){
//////        else if(view == ivBandpicture){
////            showFileChooser();
////
////            btnEditSave.setText("SAVE");
////            tvName.setVisibility(View.GONE);
////            tvAbout.setVisibility(View.GONE);
////
////            etName.setVisibility(View.VISIBLE);
////            etAbout.setVisibility(View.VISIBLE);
////
//////            if (ivBandpicture.getDrawable() != null) {
//////                relativeLayoutIv1.setVisibility(View.VISIBLE);
//////                relativeLayoutIv2.setVisibility(View.VISIBLE);
//////            } else if (ivBandpicture.getDrawable() == null) {
//////                relativeLayoutIv3.setVisibility(View.VISIBLE);
//////            }
////
//////            if (circleImageView.getDrawable() != null) {
//////                relativeLayoutIv1.setVisibility(View.VISIBLE);
//////                relativeLayoutIv2.setVisibility(View.VISIBLE);
//////            } else if (circleImageView.getDrawable() == null) {
//////                relativeLayoutIv3.setVisibility(View.VISIBLE);
//////            }
////        }
////        else if(view == rvLogout){
////
////            System.out.println("logout1 touched");
////
////            final Dialog dialog = new Dialog(this);
////            dialog.setContentView(R.layout.logoutdialog);
////
////            // set the custom dialog components - text, image and button
////            Button btnYes = dialog.findViewById(R.id.btnYes);
////            TextView btnNo = dialog.findViewById(R.id.btnNo);
////
////            btnYes.setOnClickListener(new OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    System.out.println("logout2 touched");
////                    firebaseAuth.signOut();
////
////
////                    Intent intent = new Intent(getApplicationContext(), SignInSignUpActivity.class);
////                    startActivity(intent);
////
////
////                dialog.dismiss();
////                }
////            });
////
////            btnNo.setOnClickListener(new OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    dialog.dismiss();
////                }
////            });
////
////            dialog.show();
////        }
//
//    private void removeFBandINSTA(){
//
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_accountactivity_logoutsocialmedia);
//
//        // set the custom dialog components - text, image and button
////        Button btnYes = dialog.findViewById(R.id.btnYes);
////        TextView btnNo = dialog.findViewById(R.id.btnNo);
//
//        RelativeLayout relativeLayoutRemove = dialog.findViewById(R.id.relativeLayoutRemove);
//        RelativeLayout relativeLayoutCancel = dialog.findViewById(R.id.relativeLayoutCancel);
//
//
//        relativeLayoutRemove.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    textViewInstagram.setText(R.string.accountactivity_connect);
//                    textViewInstagramEdit.setText(R.string.accountactivity_connect);
//
//                    new InstagramAuthDialog(AccountActivity.this).clearAuthCookie();
//                    instagramUID = "";
//
//                    dialog.dismiss();
//            }
//        });
//
//        relativeLayoutCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }
//
//    //method to show file chooser
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//        Log.d("showFileChooser", "reached");
//    }
//
//    private void requestInstagramAccessToken(String tokenUrl, Map<String, String> params) {
//
//        Set<Entry<String, String>> entries = params.entrySet();
//        FormBody.Builder builder = new FormBody.Builder();
//        for (Map.Entry<String, String> entry : entries) {
//            builder.add(entry.getKey(), entry.getValue());
//        }
//        Request post = new Request.Builder().url(tokenUrl).post(builder.build()).build();
//        new OkHttpClient.Builder().build().newCall(post).enqueue(new Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, final IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                final String json = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        JSONObject reader = null;
//                        try {
//                            reader = new JSONObject(json);
//                            JSONObject sys  = reader.getJSONObject("user");
//                            instagramUID = sys.getString("id"); //this is the id
//                            instagramName = sys.getString("username");
//
//                            textViewInstagram.setText(R.string.accountactivity_remove);
//                            textViewInstagramEdit.setText(R.string.accountactivity_remove);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//        });
//    }
//
//    public void clickAccessToken() {
//        new InstagramAuthDialog(this)
//                .setup("bc07fae9b64447c0bd5f0abe3a21628e", "52131709dcb24109b7c25ba71bfdbcc7", "http://com.example.admin.beerandmusic")
//                .setInstagramRequest(new InstagramRequest() {
//                    @Override
//                    public void requestAccessToken(String tokenUrl, Map<String, String> params) {
//                        requestInstagramAccessToken(tokenUrl, params);
//                    }
//                })
//                .show();
//    }
//
//    //handling the image chooser activity result
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageViewBandPic.setImageBitmap(bitmap);
//                imageViewBandPicEdit.setImageBitmap(bitmap);
//
//                relativeLayoutBlackScreenEdit.setVisibility(View.VISIBLE);
//                textViewBlackScreenEdit.setVisibility(View.VISIBLE);
//
//                imageViewBandPicEditEmpty.setVisibility(View.GONE);
//                textViewEditEmpty.setVisibility(View.GONE);
//
//                imageViewBandPic.setVisibility(View.VISIBLE);
//                imageViewBandPicEmpty.setVisibility(View.GONE);
//                imageViewBandPicEditEmpty.setVisibility(View.GONE);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //this method will upload the file
//    private void saveAndEdit() {
//        //if there is a file to upload
////        if (filePath != null) {
////
////            //displaying a progress dialog while upload is going on
////            final ProgressDialog progressDialog = new ProgressDialog(this);
////            progressDialog.setTitle("SAVING");
////            progressDialog.show();
////
////            StorageReference riversRef = storageReference.child("userid" + uid + "/pic.jpg");
////            riversRef.putFile(filePath)
////                    .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
////                        @Override
////                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                            //if the upload is successfull
////                            //hiding the progress dialog
////                            progressDialog.dismiss();
////
////                            //and displaying a success toast
////                            Toast.makeText(getApplicationContext(), "Saved ", Toast.LENGTH_LONG).show();
//////                            finish();
////                        }
////                    })
////                    .addOnFailureListener(new OnFailureListener() {
////                        @Override
////                        public void onFailure(@NonNull Exception exception) {
////                            //if the upload is not successfull
////                            //hiding the progress dialog
////                            progressDialog.dismiss();
////
////                            //and displaying error message
////                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
////                        }
////                    })
////                    .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
////                        @Override
////                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
////
////
////                            //calculating progress percentage
////                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
////
////                            //displaying percentage in progress dialog
////                            progressDialog.setMessage("Saving " + ((int) progress) + "%");
////                        }
////                    });
////
////            String name, about;
////
////            if(tvName.getText().equals(" ") || tvAbout.getText().equals(" ")) {
////                name = etName.getText().toString();
////                about = etAbout.getText().toString();
////                userDetails = new UserDetails(name, about);
////            }
////            else{
////                name = tvName.getText().toString();
////                about = tvAbout.getText().toString();
////                userDetails = new UserDetails(name, about);
////            }
////
////            databaseReference.child("User").child(uid).setValue(userDetails);
////
////            Toast.makeText(getApplicationContext(), "Saved ", Toast.LENGTH_LONG).show();
////            tvName.setText(name);
////            tvAbout.setText(about);
////
////            etName.setVisibility(View.GONE);
////            etAbout.setVisibility(View.GONE);
////            tvName.setVisibility(View.VISIBLE);
////            tvAbout.setVisibility(View.VISIBLE);
////            relativeLayoutIv1.setVisibility(View.GONE);
////            relativeLayoutIv2.setVisibility(View.GONE);
////            relativeLayoutIv3.setVisibility(View.GONE);
////            btnEditSave.setText("EDIT");
////
////        }
//
//
//
//        if (filePath != null) {
//
//            relativeLayoutSaveEditLoader1.setVisibility(View.VISIBLE);
//            linearLayoutSaveEdit.setVisibility(View.VISIBLE);
//
//            StorageReference riversRef = storageReference.child("userid" + uid + "/pic.jpg");
//            riversRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(TaskSnapshot taskSnapshot) {
//                            //if the upload is successfull
//                            //hiding the progress dialog
//
//                            read();
//
//                            String name, about;
//                            name = editTextName.getText().toString();
//                            about = editTextAbout.getText().toString();
//
//
//                            userDetails = new UserDetails(name, about, instagramUID, instagramName);
//                            databaseReference.child("User").child(uid).setValue(userDetails);
//
//                            final Handler handler = new Handler();
//                            new Thread(new Runnable() {
//                                public void run() {
//                                    try {
//                                        Thread.sleep(3000);
//                                    } catch (Exception e) {
//                                    } // Just catch the InterruptedException
//
//                                    // Now we use the Handler to post back to the main thread
//                                    handler.post(new Runnable() {
//                                        public void run() {
//                                            // Set the View's visibility back on the main UI Thread
//                                            relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
//                                            linearLayoutSaveEdit.setVisibility(View.GONE);
//                                            cardView2.setVisibility(View.GONE);
//                                            cardView1.setVisibility(View.VISIBLE);
//
////                                            Toast.makeText(getApplicationContext(), "Save Successful ", Toast.LENGTH_LONG).show();
//                                            Toasty.success(AccountActivity.this, "Save Successful", Toast.LENGTH_SHORT, true).show();
//                                        }
//                                    });
//                                }
//                            }).start();
//
//
//                            //and displaying a success toast
//
////                            finish();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            //if the upload is not successfull
//                            //hiding the progress dialog
//                            relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
//                            linearLayoutSaveEdit.setVisibility(View.GONE);
//
//                            //and displaying error message
////                            Toast.makeText(getApplicationContext(), "Saved Failed, Try Again", Toast.LENGTH_LONG).show();
//                            Toasty.error(AccountActivity.this, "Saved failed, please try again", Toast.LENGTH_LONG, true).show();
//
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//
//                            //calculating progress percentage
//                            @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                            //displaying percentage in progress dialog
////                            progressDialog.setMessage("Saving " + ((int) progress) + "%");
//                        }
//                    });
//
//
//
////            tvName.setText(name);
////            tvAbout.setText(about);
////
////            etName.setVisibility(View.GONE);
////            etAbout.setVisibility(View.GONE);
////            tvName.setVisibility(View.VISIBLE);
////            tvAbout.setVisibility(View.VISIBLE);
//////            relativeLayoutIv1.setVisibility(View.GONE);
//////            relativeLayoutIv2.setVisibility(View.GONE);
//////            relativeLayoutIv3.setVisibility(View.GONE);
////            btnEditSave.setText("EDIT");
//
//        }
//        //filePath is empty
//        else{
//
//            relativeLayoutSaveEditLoader1.setVisibility(View.VISIBLE);
//            linearLayoutSaveEdit.setVisibility(View.VISIBLE);
//
//            final String name2 = editTextName.getText().toString();
//            final String about2 = editTextAbout.getText().toString();
//            userDetails = new UserDetails(name2, about2, instagramUID, instagramName);
//            databaseReference.child("User").child(uid).setValue(userDetails)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            read();
//
//                            String name, about;
//                            name = editTextName.getText().toString();
//                            about = editTextAbout.getText().toString();
//                            userDetails = new UserDetails(name, about, instagramUID, instagramName);
//                            databaseReference.child("User").child(uid).setValue(userDetails);
//
//                            final Handler handler = new Handler();
//                            new Thread(new Runnable() {
//                                public void run() {
//                                    try {
//                                        Thread.sleep(3000);
//                                    } catch (Exception e) {
//                                    } // Just catch the InterruptedException
//
//                                    // Now we use the Handler to post back to the main thread
//                                    handler.post(new Runnable() {
//                                        public void run() {
//                                            // Set the View's visibility back on the main UI Thread
//                                            relativeLayoutSaveEditLoader1.setVisibility(View.GONE);
//                                            linearLayoutSaveEdit.setVisibility(View.GONE);
//                                            cardView2.setVisibility(View.GONE);
//                                            cardView1.setVisibility(View.VISIBLE);
//
////                                            Toast.makeText(getApplicationContext(), "Save Successful ", Toast.LENGTH_LONG).show();
//                                            Toasty.success(AccountActivity.this, "Save Successful", Toast.LENGTH_SHORT, true).show();
//
//                                        }
//                                    });
//                                }
//                            }).start();
//
//
////                            finish();
//                        }
//                    });
//
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Intent intent = new Intent(AccountActivity.this, MapActivity.class);
//                startActivity(intent);
//                finish();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public void clearGlideCache() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(AccountActivity.this).clearMemory();
//            }}, 0);
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(AccountActivity.this).clearDiskCache();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//}




package com.example.admin.beerandmusic;

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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private ImageView imageViewBandPicEdit;
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

//                Intent myIntent = new Intent(AccountActivity.this, MapActivity.class);
//                AccountActivity.this.startActivity(myIntent);
                if(prevAct.equalsIgnoreCase("signupact")){
                    Intent intent = new Intent(view.getContext(), MapActivity.class);
                    startActivity(intent);
                }
                else {
                    finish();
                }

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
                    Thread.sleep(4000);
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
                                        Thread.sleep(4000);
                                    }
                                    catch (Exception e){}

                                    handler1.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            relativeLayoutLoading.setVisibility(View.INVISIBLE);
                                            if(imageViewBandPicEdit.getDrawable() != null){

                                                relativeLayoutChangePic.setVisibility(View.VISIBLE);

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

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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
                        tvIGstatus.setText(etIGname.getText().toString());
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        dialog.dismiss();
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
                imageViewBandPicEdit.setImageBitmap(bitmap);

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

        if(!editTextName.getText().toString().equalsIgnoreCase("") || !editTextAbout.getText().toString().equalsIgnoreCase("")) {


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
                                        Toasty.success(AccountActivity.this, "Save Successful", Toast.LENGTH_SHORT, true).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(AccountActivity.this, "Save Unsuccessful, " + e.getMessage().toString(), Toast.LENGTH_SHORT, true).show();

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
                                Toasty.error(AccountActivity.this, "Saved failed, please try again", Toast.LENGTH_LONG, true).show();

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
                                } else {
                                    userDetails = new UserDetails(name, about, "");
                                }
                                databaseReference.child("User").child(uid).setValue(userDetails);

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

                                                Toasty.success(AccountActivity.this, "Save Successful", Toast.LENGTH_SHORT, true).show();

                                            }
                                        });
                                    }
                                }).start();
                            }
                        });

            }
        }
        else{
            Toasty.error(AccountActivity.this, "Please input Name and Introduction", Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AccountActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
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
        finish();
    }
}
