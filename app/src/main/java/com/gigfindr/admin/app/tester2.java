//package com.example.admin.beerandmusic;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Window;
//import android.widget.ImageView;
//
//import com.google.android.gms.location.places.GeoDataClient;
//
//public class tester2 extends AppCompatActivity {
//
//    protected GeoDataClient mGeoDataClient;
//    protected ImageView imageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tester);
//
////         imageView = findViewById(R.id.ivPlacePic);
//
//        Window window = getWindow();
//        window.setStatusBarColor(Color.BLACK);
//
////        Toolbar toolbar;
////        toolbar = findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setDisplayShowHomeEnabled(true);
////        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        // Construct a GeoDataClient.
////        mGeoDataClient = Places.getGeoDataClient(this, null);
////
////        getPhotos();
//
////        initforredesigndetailedshow();
//
//    }
//
////    private void initforredesigndetailedshow(){
////
////
////        final RelativeLayout relativeLayoutShowTab = findViewById(R.id.relativeLayoutShowTab);
////        final RelativeLayout relativeLayoutShowTabBody = findViewById(R.id.relativeLayoutShow);
////        final RelativeLayout relativeLayoutBandTab = findViewById(R.id.relativeLayoutBandTab);
////        relativeLayoutBandTab.setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                relativeLayoutShowTabBody.setVisibility(View.INVISIBLE);
//////                relativeLayoutBandTab.setVisibility(View.VISIBLE);
////
//////                Toasty.info(tester2.this, "band tab selected", Toast.LENGTH_LONG, true).show();
////
////            }
////        });
////        relativeLayoutShowTab.setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                relativeLayoutShowTabBody.setVisibility(View.VISIBLE);
//////                relativeLayoutBandTab.setVisibility(View.INVISIBLE);
//////                Toasty.info(tester2.this, "show tab selected", Toast.LENGTH_LONG, true).show();
////
////            }
////        });
////    }
////
////    private void getPhotos() {
////        final String placeId = "ChIJ_f7mI6AZ2jERx4zyw9u0KXc";
////        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
////        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
////            @Override
////            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
////                // Get the list of photos.
////                PlacePhotoMetadataResponse photos = task.getResult();
////                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
////                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
////                // Get the first photo in the list.
////                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
////                // Get the attribution text.
//////                CharSequence attribution = photoMetadata.getAttributions();
////                // Get a full-size bitmap for the photo.
////                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
////                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
////                    @Override
////                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
////                        PlacePhotoResponse photo = task.getResult();
////                        Bitmap bitmap = photo.getBitmap();
////
////                        imageView.setImageBitmap(bitmap);
////                    }
////                });
////            }
////        });
////    }
//}
