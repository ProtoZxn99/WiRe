package patrick.pramedia.wire;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class STTFragment extends Fragment implements RecognitionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean buttonuse = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton tbrec;

    private static final int REQUEST_RECORD_PERMISSION = 100;

    private OnFragmentInteractionListener mListener;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    public STTFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static STTFragment newInstance(String param1, String param2) {
        STTFragment fragment = new STTFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 9);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stt, container, false);
        tbrec = (ImageButton) v.findViewById(R.id.tbrec);
        Log.d("AAA", "Commencing fragment");
        tbrec.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!buttonuse) {
                     Log.d("AAA", "Asking permission");
                     //ActivityCompat.requestPermissions (getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                     tbrec.setImageResource(R.drawable.ic_option);
                     requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                 } else {
                     tbrec.setImageResource(R.drawable.ic_mic_none_black_24dp);
                     speech.stopListening();
                     buttonuse = false;
                 }
             }
         }
//                new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d("AAA", "Checking");
//                if (isChecked) {
//                    Log.d("AAA", "Asking permission");
//                    //ActivityCompat.requestPermissions (getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
//                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
//                } else {
//                    speech.stopListening();
//                }
//            }
//        }
        );

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("AAA", "Getting result");
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("AAA", "Recording");
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    tbrec.setImageResource(R.drawable.ic_mic_none_black_24dp);
                    speech.stopListening();
                    buttonuse = false;
                }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(String str) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(str);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
        }
        ArrayList<String> result = new ArrayList<>();
        result.add("Stopped");

        tbrec.setImageResource(R.drawable.ic_mic_none_black_24dp);
        speech.stopListening();
        buttonuse = false;

        mListener.onFragmentInteraction(result);
    }


    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        tbrec.setImageResource(R.drawable.ic_mic_none_black_24dp);
        speech.stopListening();
        buttonuse = false;
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);

        ArrayList<String> result = new ArrayList<>();
        result.add("Error");
        tbrec.setImageResource(R.drawable.ic_mic_none_black_24dp);
        speech.stopListening();
        buttonuse = false;
        mListener.onFragmentInteraction(result);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
    }

    @Override
    public void onPartialResults(Bundle arg0) {
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        tbrec.setImageResource(R.drawable.ic_mic_none_black_24dp);
        speech.stopListening();
        buttonuse = false;
        mListener.onFragmentInteraction(matches);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(ArrayList<String> str);
    }
}
