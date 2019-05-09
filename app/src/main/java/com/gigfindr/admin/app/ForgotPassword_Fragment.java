package com.gigfindr.admin.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgotPassword_Fragment extends Fragment implements
		OnClickListener {
	private static View view;

	private static EditText emailId;
	private static Button submit;
	private static FragmentManager fragmentManager;


	private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

	public ForgotPassword_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.forgotpassword_layout, container,
				false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize the views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		emailId = view.findViewById(R.id.registered_emailid);
		submit = view.findViewById(R.id.forgot_button);
	}

	// Set Listeners over buttons
	private void setListeners() {
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
			submitButtonTask();
	}

	private void submitButtonTask() {
		String getEmailId = emailId.getText().toString();

		Toasty.Config.getInstance()
				.setInfoColor(getResources().getColor(R.color.yellow1))
				.apply(); // required

		// First check if email id is not null else show error toast
		if (getEmailId.equals("") || getEmailId.length() == 0)
		Toasty.info(getActivity().getBaseContext(), "Please enter your Email", Toast.LENGTH_LONG, true).show();

		firebaseAuth.sendPasswordResetEmail(getEmailId.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if(task.isSuccessful()){
					Toasty.success(getActivity().getBaseContext(), "Reset is done, please check your email", Toast.LENGTH_LONG, true).show();

					fragmentManager
							.beginTransaction()
							.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
							.replace(R.id.frameContainer,
									new Login_Fragment(),
									Utils.Login_Fragment).commit();

				}
				else if(task.isCanceled()){
					Toasty.error(getActivity().getBaseContext(), "Please enter registered email only", Toast.LENGTH_LONG, true).show();

				}
			}
		});
	}
}