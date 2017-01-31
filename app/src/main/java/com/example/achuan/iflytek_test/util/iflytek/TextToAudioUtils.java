package com.example.achuan.iflytek_test.util.iflytek;

import android.content.Context;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by achuan on 17-1-25.
 * 功能：语音合成(文字转语音)工具类
 */

public class TextToAudioUtils {

    //SpeechSynthesizer(语音合成)引用变量
    private static SpeechSynthesizer mTts;

    //2-初始化语音配置
    public static void init(Context context,InitListener initListener){
        //创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts= SpeechSynthesizer.createSynthesizer(context, initListener);
    }
    //3-设置语音合成参数
    public static void setParam(){
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        //mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);//设置本地
    }
    //4-开始合成语音
    public static int speakText(String content,SynthesizerListener mSynListener){
        //执行语音合成后获取结果码来判断处理的情况
        int code=mTts.startSpeaking(content, mSynListener);
        return code;
    }

}
