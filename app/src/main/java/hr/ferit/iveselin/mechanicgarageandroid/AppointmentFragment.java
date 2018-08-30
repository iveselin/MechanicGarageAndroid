package hr.ferit.iveselin.mechanicgarageandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import hr.ferit.iveselin.mechanicgarageandroid.model.AppointmentRequest;

public class AppointmentFragment extends Fragment {

    private static final String TAG = "AppointmentFragment";


    @BindView(R.id.extra_note_input)
    EditText extraNoteInput;


    private Set<String> markedCheckboxes = new HashSet<>();

    public static AppointmentFragment newInstance() {
        return new AppointmentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setUi();
        init();
    }

    private void setUi() {

    }

    private void init() {

    }

    @OnCheckedChanged({R.id.required_service_big, R.id.required_service_brakes, R.id.required_service_small, R.id.required_service_diagnostics, R.id.required_service_other, R.id.required_service_tyres})
    void onCheckedChange(CompoundButton button, boolean checked) {
        if (checked) {
            markedCheckboxes.add(button.getText().toString());
        } else {
            markedCheckboxes.remove(button.getText().toString());
        }
    }

    @OnClick(R.id.submit_service_request)
    void onSubmitRequestClicked() {
        if (markedCheckboxes.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.no_service_checked_error, Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentRequest request = new AppointmentRequest(markedCheckboxes.toArray(new String[markedCheckboxes.size()]), extraNoteInput.getText().toString().trim());
        // TODO: 30.8.2018. save request to DB
        Log.d(TAG, "onSubmitRequestClicked: saving request: " + request.toString());
    }
}
