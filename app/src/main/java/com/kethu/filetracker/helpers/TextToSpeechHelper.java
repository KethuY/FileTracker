package com.kethu.filetracker.helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.HashMap;


/**
 * Created by LOGICON on 28-12-2017.
 */

public class TextToSpeechHelper {

    private static TextToSpeech mTextToSpeech;

    public static TextToSpeech getInstance(Context activity, OnInitListener on){
       if(mTextToSpeech==null) {
           mTextToSpeech=new TextToSpeech(activity, on);
       }
       return mTextToSpeech;
    }

    public static void speakOut(Activity activity,String message) {
     //   SharedPreferences sharedPreferences = activity.getSharedPreferences(ANNOUNCEMENTS_SHARED_PREF, MODE_PRIVATE);
      //  boolean announcementStatus = sharedPreferences.getBoolean(ANNOUNCEMENT_SHARED_PREF_KEY, true);
        /*if (!announcementStatus) {
            return;
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(message,activity);
        } else {
            ttsUnder20(message,activity);
        }
    }
    @SuppressWarnings("deprecation")
    private static void ttsUnder20(String text,Activity activity) {
        if (mTextToSpeech!=null) {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void ttsGreater21(String text,Activity activity) {
        if (mTextToSpeech!=null){
            String utteranceId = activity.hashCode() + "";
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }
}
