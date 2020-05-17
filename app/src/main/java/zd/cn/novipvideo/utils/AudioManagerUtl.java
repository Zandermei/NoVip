package zd.cn.novipvideo.utils;

import android.content.Context;
import android.media.AudioManager;

import zd.cn.novipvideo.NoVipApplication;

public class AudioManagerUtl {

    private AudioManagerUtl audioManagerUtl;

    public static final Context context = NoVipApplication.getApplicationContex();

    public AudioManagerUtl getAudioManagerUtl() {
        if(audioManagerUtl == null){
            audioManagerUtl = new AudioManagerUtl();
        }
        return audioManagerUtl;
    }

    public static AudioManager getAudioManager(){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager;
    }
}
