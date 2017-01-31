package com.example.achuan.iflytek_test.ui.main.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achuan.iflytek_test.R;
import com.example.achuan.iflytek_test.app.Constants;
import com.example.achuan.iflytek_test.base.SimpleFragment;
import com.example.achuan.iflytek_test.util.iflytek.ApkInstaller;
import com.example.achuan.iflytek_test.util.iflytek.AudioToTextUtils;
import com.example.achuan.iflytek_test.util.iflytek.FucUtil;
import com.example.achuan.iflytek_test.util.LogUtil;
import com.example.achuan.iflytek_test.util.iflytek.TextToAudioUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by achuan on 17-1-30.
 */

public class SpeechFragment extends SimpleFragment {


    @BindView(R.id.et_inputInfo)
    EditText mEtInputInfo;
    @BindView(R.id.bt_tts)
    Button mBtTts;
    @BindView(R.id.tv_getInfo)
    TextView mTvGetInfo;
    @BindView(R.id.bt_its)
    Button mBtIts;

    // 语记安装助手类
    ApkInstaller mInstaller;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_speech;
    }
    @Override
    protected void initEventAndData() {
        //初始化引擎操作
        TextToAudioUtils.init(getContext(),mTtsInitListener);
        AudioToTextUtils.init(getContext(),mItsInitListener);
        //语记安装实例对象
        mInstaller = new ApkInstaller(getActivity());
        /**
         * 选择本地听写 判断是否安装语记,未安装则跳转到提示安装页面
         */
        if (!SpeechUtility.getUtility().checkServiceInstalled()) {
            mInstaller.install();
        } else {
            //获取语记是否包含听写资源
            String result = FucUtil.checkLocalResource();
            if (!TextUtils.isEmpty(result)) {
                //如果有,则

            }
        }

        // 里面有三个函数可以实现对所有text的监听。
        TextChange textChange = new TextChange();
        mEtInputInfo.addTextChangedListener(textChange);


    }

    //自定义一个多editext的输入监听类
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mEtInputInfo.length() > 0) {
                mBtTts.setEnabled(true);//设置按钮可以点击使用
            } else {
                mBtTts.setEnabled(false);
            }
        }
    }

    @OnClick({R.id.bt_tts,R.id.bt_its})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tts:
                //文字转语音
                TextToAudioUtils.setParam();
                int code = TextToAudioUtils.speakText(
                        mEtInputInfo.getText().toString(),mSynthesizerListener);
                if (code != ErrorCode.SUCCESS) {
                    if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                        //未安装则跳转到提示安装页面
                        mInstaller.install();
                    } else {
                        Toast.makeText(getContext(),
                                "语音合成失败,错误码: " + code,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.bt_its:
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(getContext(), "iat_recognize");
                mTvGetInfo.setText(null);//清空显示内容
                mEtInputInfo.setText(null);
                //开始语音识别
                AudioToTextUtils.setParam();
                AudioToTextUtils.showDialog(mRecoDialogListener);
                Toast.makeText(getContext(),
                        "请开始说话...",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /***1-初始化监听器***/
    /*1-1语音合成的初始化监听器*/
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            LogUtil.d(Constants.SPEECH_SYNTHESIZER_TAG,
                    "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(getContext(),
                        "初始化失败，错误码：" + code,
                        Toast.LENGTH_SHORT).show();
            }else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里

            }
        }
    };
    /*1-2语音识别的初始化监听器*/
    private InitListener mItsInitListener =new InitListener() {
        @Override
        public void onInit(int code) {
            LogUtil.d(Constants.SPEECH_RECOGNIZER_TAG,
                    "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(getContext(),
                        "初始化失败，错误码：" + code,
                        Toast.LENGTH_SHORT).show();
            }else {

            }
        }
    };

    /***2-语音合成监听器***/
    private SynthesizerListener mSynthesizerListener=new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    /*----------------3-听写监听器------------------------*/
    /***3-1普通听写监听器***/
    private RecognizerListener mRecoListener=new RecognizerListener() {
        @Override//当前用户输入的音量的大小和声音字节流数据
        public void onVolumeChanged(int i, byte[] bytes) {

        }
        @Override
        public void onBeginOfSpeech() {

        }
        @Override
        public void onEndOfSpeech() {

        }
        @Override//声音识别结束
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            printResult(recognizerResult);
        }
        @Override//声音识别失败
        public void onError(SpeechError speechError) {
            Toast.makeText(getContext(),
                    speechError.getPlainDescription(true),
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
    /***3-2听写UI监听器***/
    private RecognizerDialogListener mRecoDialogListener=new RecognizerDialogListener() {
        @Override//声音识别结束
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            printResult(recognizerResult);
        }
        @Override//声音识别失败
        public void onError(SpeechError speechError) {
            Toast.makeText(getContext(),
                    speechError.getPlainDescription(true),
                    Toast.LENGTH_SHORT).show();
        }
    };

    //4-输出听写的结果到界面
    private void printResult(RecognizerResult recognizerResult){
        String text=AudioToTextUtils.changeResultToText(recognizerResult);
        mTvGetInfo.setText(text);
        mEtInputInfo.setText(text);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
