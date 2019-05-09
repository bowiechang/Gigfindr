package com.gigfindr.admin.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private View view;
	private EditText emailId, password, confirmPassword;
	private TextView login;
	private Button signUpButton;
	private CheckBox terms_conditions;

//	private LinearLayout linearLayoutSaveEdit;
	private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.redesign_signup_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {

//		linearLayoutSaveEdit = view.findViewById(R.id.saveditloader2);
//		linearLayoutSaveEdit.setVisibility(View.GONE);

		emailId = view.findViewById(R.id.userEmailId);
		password = view.findViewById(R.id.password);
		confirmPassword = view.findViewById(R.id.confirmPassword);
		signUpButton = view.findViewById(R.id.signUpBtn);
		login = view.findViewById(R.id.already_user);
		terms_conditions = view.findViewById(R.id.terms_conditions);

		terms_conditions.setTypeface(ResourcesCompat.getFont(getContext(), R.font.roboto_condensed_bold_test_download));
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);

		//listens to auth on changes, if not null proceed
		FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
			}
		};
		//add the listener its important!
		firebaseAuth.addAuthStateListener(authStateListener);

		terms_conditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			checkValidation();
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			break;

		case R.id.already_user:

			// Replace login fragment
			new SignInSignUpActivity().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts

		String getEmailId = emailId.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		Toasty.Config.getInstance()
				.setInfoColor(getResources().getColor(R.color.yellow1))
				.apply(); // required

		// Check if all strings are null or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0)



//			new CustomToast().Show_Toast(getActivity(), view,
//					"All fields are required.");

			Toasty.info(getActivity().getBaseContext(), "All fields are required", Toast.LENGTH_LONG, true).show();


			// Check if email id valid or not
		else if (!m.find())
//			new CustomToast().Show_Toast(getActivity(), view,
//					"Your Email is invalid.");

		Toasty.info(getActivity().getBaseContext(), "Your Email is invalid", Toast.LENGTH_LONG, true).show();

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword))
//			new CustomToast().Show_Toast(getActivity(), view,
//					"Passwords doesn't match.");
			Toasty.info(getActivity().getBaseContext(), "Passwords doesn't match", Toast.LENGTH_LONG, true).show();

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
		Toasty.info(getActivity().getBaseContext(), "Please check Terms and Conditions", Toast.LENGTH_LONG, true).show();

		// Else do signup or do your stuff
		else
			register();

	}

	public void register() {

		final String getemail = emailId.getText().toString().trim();
		final String getpassword = password.getText().toString().trim();

        final Dialog dialog = new Dialog(SignUp_Fragment.this.getContext());
        dialog.setContentView(R.layout.signupdialog);
        dialog.show();

		firebaseAuth.createUserWithEmailAndPassword(getemail, getpassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()){

				    dialog.dismiss();
					Toasty.success(getActivity().getBaseContext(), "Successful", Toast.LENGTH_SHORT, true).show();
					Intent intent = new Intent(view.getContext(), AccountActivity.class);
					intent.putExtra("FROM_ACTIVITY", "signupact");
					startActivity(intent);
				}
				else{
                    dialog.dismiss();
					Toasty.info(getActivity().getBaseContext(), "Please ensure your password is longer than 6 characters and do not reuse emails", Toast.LENGTH_LONG, true).show();

				}
			}
		});
	}
}
