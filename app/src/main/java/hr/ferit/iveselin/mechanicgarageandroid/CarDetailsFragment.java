package hr.ferit.iveselin.mechanicgarageandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.iveselin.mechanicgarageandroid.model.Car;
import hr.ferit.iveselin.mechanicgarageandroid.utils.StringUtils;

public class CarDetailsFragment extends Fragment {

    private static final String TAG = "CarDetailsFragment";


    @BindView(R.id.car_make_input)
    EditText carMakeInput;
    @BindView(R.id.car_model_input)
    EditText carModelInput;
    @BindView(R.id.car_year_input)
    Spinner carYearInput;
    @BindView(R.id.car_engine_input)
    EditText carEngineInput;
    @BindView(R.id.car_vin_input)
    EditText carVinInput;
    @BindView(R.id.submit_car)
    Button carSubmit;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String carMake;
    private String carModel;
    private String carEngine;
    private String carVIN;
    private int carYear;


    public static CarDetailsFragment newInstance() {
        return new CarDetailsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        setUi();
        init();
    }

    private void setUi() {
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1980; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, years);
        carYearInput.setAdapter(yearAdapter);
    }

    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @OnClick(R.id.submit_car)
    void onCarSubmitClicked() {
        if (!inputsValidated()) {
            return;
        }
        Car carToSave = new Car(user.getUid(), carMake, carModel, carYear, carEngine, carVIN);
        // TODO: 30.8.2018. save or update car in DB
        Log.d(TAG, "onCarSubmitClicked: saving a car: " + carToSave.toString());
    }

    private boolean inputsValidated() {
        boolean error = false;
        carMake = carMakeInput.getText().toString().trim();
        carModel = carModelInput.getText().toString().trim();
        //this is safe because the array is populated by integers
        carYear = Integer.parseInt(carYearInput.getSelectedItem().toString());
        carEngine = carEngineInput.getText().toString().trim();
        carVIN = carVinInput.getText().toString().trim();
        if (carMake.isEmpty()) {
            error = true;
            carMakeInput.setError(getString(R.string.empty_input_field_error));
        }
        if (carModel.isEmpty()) {
            error = true;
            carModelInput.setError(getString(R.string.empty_input_field_error));
        }
        if (carEngine.isEmpty()) {
            error = true;
            carEngineInput.setError(getString(R.string.empty_input_field_error));
        }
        if (carVIN.isEmpty()) {
            error = true;
            carVinInput.setError(getString(R.string.empty_input_field_error));
        }
        if (carVIN.length() < 17) {
            error = true;
            carVinInput.setError(getString(R.string.wrong_VIN_error));
        }
        return !error;
    }
}
