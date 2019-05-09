package com.gigfindr.admin.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;

public class SignInSignUpActivity extends AppCompatActivity {
	private static FragmentManager fragmentManager;
	private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentManager = getSupportFragmentManager();

		Window window = getWindow();
		window.setStatusBarColor(Color.BLACK);

		Toolbar toolbar = findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

//				Intent myIntent = new Intent(SignInSignUpActivity.this, MapActivity.class);
//				SignInSignUpActivity.this.startActivity(myIntent);

				Intent intent = new Intent(SignInSignUpActivity.this, MapActivity.class);
				startActivity(intent);

			}
		});

		String logout = "not";
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			logout = getIntent().getExtras().getString("logout");
		}

		if(logout.equals("not")) {
			if (savedInstanceState == null) {
				fragmentManager
						.beginTransaction()
						.replace(R.id.frameContainer, new SignUp_Fragment(),
								Utils.SignUp_Fragment).commit();
			}
		}
		else{
			if (savedInstanceState == null) {
				fragmentManager
						.beginTransaction()
						.replace(R.id.frameContainer, new Login_Fragment(),
								Utils.Login_Fragment).commit();
			}
		}
	}

	// Replace Login Fragment with animation
	protected void replaceSignUpFragment() {
		fragmentManager
				.beginTransaction()
				.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
				.replace(R.id.frameContainer, new SignUp_Fragment(),
						Utils.SignUp_Fragment).commit();
	}

	// Replace Login Fragment with animation
	protected void replaceLoginFragment() {
		fragmentManager
				.beginTransaction()
				.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
				.replace(R.id.frameContainer, new Login_Fragment(),
						Utils.Login_Fragment).commit();
	}

	@Override
	public void onBackPressed() {
//
//		// Find the tag of signup and forgot password fragment
//		Fragment SignUp_Fragment = fragmentManager
//				.findFragmentByTag(Utils.SignUp_Fragment);
//		Fragment ForgotPassword_Fragment = fragmentManager
//				.findFragmentByTag(Utils.ForgotPassword_Fragment);
//
//		// Check if both are null or not
//		// If both are not null then replace login fragment else do backpressed
//		// task
//
//		if (SignUp_Fragment != null)
//			replaceLoginFragment();
//		else if (ForgotPassword_Fragment != null)
//			replaceLoginFragment();
//		else

			super.onBackPressed();

			Intent intent = new Intent(SignInSignUpActivity.this, MapActivity.class);
			startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
