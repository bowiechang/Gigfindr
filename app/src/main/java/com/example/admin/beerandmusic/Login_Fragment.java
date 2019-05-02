package com.example.admin.beerandmusic;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Login_Fragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText emailid, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;

	private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
	private FirebaseAuth.AuthStateListener authStateListener;

	public Login_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initiate Views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		emailid = view.findViewById(R.id.login_emailid);
		password = view.findViewById(R.id.login_password);
		loginButton = view.findViewById(R.id.loginBtn);
		forgotPassword = view.findViewById(R.id.forgot_password);
		signUp = view.findViewById(R.id.createAccount);
		show_hide_password = view
				.findViewById(R.id.show_hide_password);
		loginLayout = view.findViewById(R.id.login_layout);

		show_hide_password.setTypeface(ResourcesCompat.getFont(getContext(), R.font.roboto_condensed_bold_test_download));

		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);
	}

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		//listens to auth on changes, if not null proceed
		authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
			}
		};
		//add the listener its important!
		firebaseAuth.addAuthStateListener(authStateListener);


		// Set check listener over checkbox for showing and hiding password
		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						// If it is checkec then show password else hide
						// password
						if (isChecked) {

							show_hide_password.setText(R.string.hide_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());// show password
						} else {
							show_hide_password.setText(R.string.show_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password

						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			checkValidation();
			break;

		case R.id.forgot_password:

			// Replace forgot password fragment with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer,
							new ForgotPassword_Fragment(),
							Utils.ForgotPassword_Fragment).commit();
			break;
		case R.id.createAccount:

			// Replace signup frgament with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer, new SignUp_Fragment(),
							Utils.SignUp_Fragment).commit();
			break;
		}

	}

	// Check Validation before login
	private void checkValidation() {
		// Get email id and password
		String getEmailId = emailid.getText().toString();
		String getPassword = password.getText().toString();

		// Check patter for email id
		Pattern p = Pattern.compile(Utils.regEx);

		Matcher m = p.matcher(getEmailId);

		// Check for both field is empty or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
//			new CustomToast().Show_Toast(getActivity(), view,
//					"Enter both credentials.");

			Toasty.Config.getInstance()
					.setInfoColor(getResources().getColor(R.color.yellow1))
					.apply(); // required
			Toasty.info(getActivity().getBaseContext(), "Enter both credentials", Toast.LENGTH_LONG, true).show();

		}
		// Check if email id is valid or not
		else if (!m.find())
//			new CustomToast().Show_Toast(getActivity(), view,
//					"Your Email Id is Invalid.");

		Toasty.error(getActivity().getBaseContext(), "Your Email is invalid", Toast.LENGTH_LONG, true).show();


		// Else do login and do your stuff
		else
			login();

	}

	public void login() {

		final String getemail = emailid.getText().toString().trim();
		final String getpassword = password.getText().toString().trim();

		final Dialog dialog = new Dialog(Login_Fragment.this.getContext());
		dialog.setContentView(R.layout.logindialog);
		dialog.show();

		firebaseAuth.signInWithEmailAndPassword(getemail, getpassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()){

					dialog.dismiss();
					Toasty.success(getActivity().getBaseContext(), "Successful", Toast.LENGTH_SHORT, true).show();
					Intent intent = new Intent(view.getContext(), ViewMyShowsActivity.class);
					startActivity(intent);
				}
				else{
					dialog.dismiss();
					Toasty.error(getActivity().getBaseContext(), "Unsuccessful", Toast.LENGTH_SHORT, true).show();
				}
			}
		});
	}

}
