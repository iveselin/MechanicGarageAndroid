package hr.ferit.iveselin.mechanicgarageandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.iveselin.mechanicgarageandroid.utils.StringUtils;

public class LoginRegisterFragment extends Fragment {

    private static final String TAG = "LoginRegisterFragment";


    private static final String KEY_FRAGMENT_TYPE = "fragment_type";
    public static final int LOGIN_FRAGMENT = 111;
    public static final int REGISTER_FRAGMENT = 222;


    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.password_input)
    EditText passwordInput;
    @BindView(R.id.password_reinput)
    EditText passwordReinput;
    @BindView(R.id.name_input)
    EditText nameInput;
    @BindView(R.id.submit_login_register)
    Button submitRegisterLogin;
    @BindView(R.id.toogle_register_login)
    Button toggleType;


    private int fragmentTypeFlag;

    private FirebaseAuth firebaseAuth;

    public static LoginRegisterFragment newInstance(int fragmentTypeKey) {
        Bundle args = new Bundle();
        args.putInt(KEY_FRAGMENT_TYPE, fragmentTypeKey);
        LoginRegisterFragment fragment = new LoginRegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        getExtras();
        setUi();
        initAuth();
    }

    private void getExtras() {
        fragmentTypeFlag = getArguments().getInt(KEY_FRAGMENT_TYPE);
        if (fragmentTypeFlag != LOGIN_FRAGMENT && fragmentTypeFlag != REGISTER_FRAGMENT) {
            Log.d(TAG, "getExtras: fragment type not properly set, use available constants");
            getActivity().onBackPressed();
        }
    }

    private void setUi() {
        if (fragmentTypeFlag == REGISTER_FRAGMENT) {
            passwordReinput.setVisibility(View.VISIBLE);
            nameInput.setVisibility(View.VISIBLE);
            toggleType.setText(R.string.login_toogle_text);
        } else {
            toggleType.setText(R.string.register_toogle_text);
        }
    }

    private void initAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @OnClick(R.id.submit_login_register)
    void onSubmitClicked() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String password2 = passwordReinput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();

        boolean error = false;

        if (!StringUtils.isValidEmail(email)) {
            emailInput.setError(getString(R.string.login_email_error));
            error = true;
        }
        if (password.length() < 6) {
            passwordInput.setError(getString(R.string.login_password_error));
            error = true;
        }
        if (fragmentTypeFlag == REGISTER_FRAGMENT) {

            if (!password.equals(password2)) {
                passwordReinput.setError(getString(R.string.login_password_different_error));
                passwordInput.setError(getString(R.string.login_password_different_error));
                error = true;
            }
            if (name.isEmpty()) {
                nameInput.setError(getString(R.string.login_name_error));
                error = true;
            }
        }

        if (error) {
            return;
        }

        if (fragmentTypeFlag == REGISTER_FRAGMENT) {
            createUser(email, password, name);
        } else {
            signInUser(email, password);
        }

    }

    private void createUser(final String email, String password, final String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: created new user with email: " + email);
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    UserProfileChangeRequest userUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    // TODO: 28.8.2018. listener for update maybeee???
                    user.updateProfile(userUpdates);
                    Toast.makeText(getActivity().getApplicationContext(), R.string.registrarion_success_text, Toast.LENGTH_SHORT).show();
                    // TODO: 28.8.2018. go to other fragment, user is signed in
                } else {
                    Log.w(TAG, "onComplete: failure", task.getException());
                    Toast.makeText(getActivity().getApplicationContext(), R.string.registration_error_text, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void signInUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: sign in success");
                    Toast.makeText(getActivity().getApplicationContext(), R.string.sign_in_success_text, Toast.LENGTH_SHORT).show();
                    // TODO: 28.8.2018. go to other fragment, user is signed in
                } else {
                    Log.w(TAG, "onComplete: sign in failed", task.getException());
                    Toast.makeText(getActivity().getApplicationContext(), R.string.sign_in_error_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.toogle_register_login)
    void onToggleClicked() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (fragmentTypeFlag == LoginRegisterFragment.LOGIN_FRAGMENT) {
            fragmentTransaction.replace(R.id.fragment_container, LoginRegisterFragment.newInstance(LoginRegisterFragment.REGISTER_FRAGMENT));
        } else {
            fragmentTransaction.replace(R.id.fragment_container, LoginRegisterFragment.newInstance(LoginRegisterFragment.LOGIN_FRAGMENT));
        }
        fragmentTransaction.commit();
    }
}
