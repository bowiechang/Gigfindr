package com.example.admin.beerandmusic;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccountActivity extends AppCompatActivity implements OnClickListener {

    Button btnDeleteAccount;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {

        //delete account

//        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    view.getContext().startActivity(new Intent(getApplicationContext(), LoginSignUpActivity.class));
//                    Toast.makeText(getApplicationContext(), "Account deleted!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getBaseContext(), "Account deletion unsuccessful, please try again!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
