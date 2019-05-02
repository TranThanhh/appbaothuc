package com.example.appbaothuc.appsetting;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.appbaothuc.MainActivity;
import com.example.appbaothuc.R;
import com.peanut.androidlib.filemanager.InternalFileReader;
import com.peanut.androidlib.filemanager.InternalFileWriter;

import java.util.ArrayList;
import java.util.List;

public class AppSettingFragment extends Fragment {
    public static final int HOUR_MODE_24 = 1;
    public static final int HOUR_MODE_12 = 2;
    private static final String fileName = "setting.txt";
    private Context context;
    private ImageButton imageButtonBack;
    static TextView textViewMuteAlarmFor;
    static TextView textViewCanMuteAlarmFor;
    static TextView textViewAutoDismissAfter;
    private CheckBox checkBoxGraduallyIncreaseVolume;
    private CheckBox checkBoxPreventTurnOffPhone;
    private RadioButton radioButton24Hour;
    private RadioButton radioButton12Hour;

    public static int muteAlarmIn;
    public static int canMuteAlarmFor;
    public static int autoDismissAfter;
    public static boolean graduallyIncreaseVolume;
    public static boolean preventTurnOffPhone;
    public static int hourMode;
    public static List<String> listRingtoneDirectory; // TODO

    private MuteAlarmInDialogFragment muteAlarmInDialogFragment;
    private CanMuteAlarmForDialogFragment canMuteAlarmForDialogFragment;
    private AutoDismissAfterDialogFragment autoDismissAfterDialogFragment;
    private InternalFileReader internalFileReader;
    private InternalFileWriter internalFileWriter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initializeSetting(context);
        this.context = context;
        this.muteAlarmInDialogFragment = new MuteAlarmInDialogFragment();
        this.canMuteAlarmForDialogFragment = new CanMuteAlarmForDialogFragment();
        this.autoDismissAfterDialogFragment = new AutoDismissAfterDialogFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity)this.context).appSettingFragmentIsAdded = false;
        this.internalFileWriter = new InternalFileWriter(this.context, fileName);
        this.internalFileWriter.writeLine(String.valueOf(muteAlarmIn), false);
        this.internalFileWriter.writeLine(String.valueOf(canMuteAlarmFor), true);
        this.internalFileWriter.writeLine(String.valueOf(autoDismissAfter), true);
        this.internalFileWriter.writeLine(String.valueOf(graduallyIncreaseVolume), true);
        this.internalFileWriter.writeLine(String.valueOf(preventTurnOffPhone), true);
        this.internalFileWriter.writeLine(String.valueOf(hourMode), true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        textViewMuteAlarmFor.setText("Báo thức im lặng trong: " + muteAlarmIn + " giây");
        textViewCanMuteAlarmFor.setText("Có thể im lặng báo thức: " + canMuteAlarmFor + " lần");
        textViewAutoDismissAfter.setText("Tự động hủy báo thức sau: " + autoDismissAfter + " phút");
        this.checkBoxGraduallyIncreaseVolume.setChecked(graduallyIncreaseVolume);
        this.checkBoxPreventTurnOffPhone.setChecked(preventTurnOffPhone);
        if(hourMode == HOUR_MODE_24){
            this.radioButton24Hour.setChecked(true);
        }
        else{
            this.radioButton12Hour.setChecked(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_setting, container, false);
        this.imageButtonBack = view.findViewById(R.id.image_button_back);
        textViewMuteAlarmFor = view.findViewById(R.id.text_view_mute_alarm_for);
        textViewCanMuteAlarmFor = view.findViewById(R.id.text_view_can_mute_alarm_for);
        textViewAutoDismissAfter = view.findViewById(R.id.text_view_auto_dismiss_after);
        this.checkBoxGraduallyIncreaseVolume = view.findViewById(R.id.check_box_gradually_increase_volume);
        this.checkBoxPreventTurnOffPhone = view.findViewById(R.id.check_box_prevent_turn_off_phone);
        this.radioButton24Hour = view.findViewById(R.id.radio_button_24_hour);
        this.radioButton12Hour = view.findViewById(R.id.radio_button_12_hour);


        this.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AppSettingFragment.this).commit();
            }
        });

        textViewMuteAlarmFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteAlarmInDialogFragment.show(getFragmentManager(), null);
            }
        });
        textViewCanMuteAlarmFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canMuteAlarmForDialogFragment.show(getFragmentManager(), null);
            }
        });
        textViewAutoDismissAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoDismissAfterDialogFragment.show(getFragmentManager(), null);
            }
        });
        this.checkBoxGraduallyIncreaseVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                graduallyIncreaseVolume = isChecked;
            }
        });
        this.checkBoxPreventTurnOffPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preventTurnOffPhone = isChecked;
            }
        });
        this.radioButton24Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    hourMode = HOUR_MODE_24;
                }
            }
        });
        this.radioButton12Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    hourMode = HOUR_MODE_12;
                }
            }
        });

        return view;
    }
    private static void initializeDefaultSetting(){
        muteAlarmIn = 30;
        canMuteAlarmFor = 3;
        autoDismissAfter = 1;
        graduallyIncreaseVolume = true;
        preventTurnOffPhone = true;
        hourMode = HOUR_MODE_24;
    }

    private void initializeSetting(Context context){
        this.internalFileReader = new InternalFileReader(context, fileName);
        if(!internalFileReader.exists(fileName)){
            initializeDefaultSetting();
        }
        else{
            muteAlarmIn = Integer.parseInt(internalFileReader.readLine());
            canMuteAlarmFor = Integer.parseInt(internalFileReader.readLine());
            autoDismissAfter = Integer.parseInt(internalFileReader.readLine());
            graduallyIncreaseVolume = Boolean.parseBoolean(internalFileReader.readLine());
            preventTurnOffPhone = Boolean.parseBoolean(internalFileReader.readLine());
            hourMode = Integer.parseInt(internalFileReader.readLine());
        }
        listRingtoneDirectory = new ArrayList<>();
        listRingtoneDirectory.add("/sdcard/music");
        listRingtoneDirectory.add("/sdcard/download");
    }

    public static void loadAppSetting(Context context){
        InternalFileReader internalFileReader = new InternalFileReader(context, fileName);
        if(!internalFileReader.exists(fileName)){
            initializeDefaultSetting();
        }
        else{
            muteAlarmIn = Integer.parseInt(internalFileReader.readLine());
            canMuteAlarmFor = Integer.parseInt(internalFileReader.readLine());
            autoDismissAfter = Integer.parseInt(internalFileReader.readLine());
            graduallyIncreaseVolume = Boolean.parseBoolean(internalFileReader.readLine());
            preventTurnOffPhone = Boolean.parseBoolean(internalFileReader.readLine());
            hourMode = Integer.parseInt(internalFileReader.readLine());
        }
        listRingtoneDirectory = new ArrayList<>();
        listRingtoneDirectory.add("/sdcard/music");
        listRingtoneDirectory.add("/sdcard/download");
    }
}