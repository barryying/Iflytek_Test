package com.example.achuan.iflytek_test.util.iflytek;

import android.content.Context;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by achuan on 17-1-26.
 * 功能：语音识别(语音转文字)工具类
 */

public class AudioToTextUtils {

    //SpeechRecognizer(语音识别)引用变量
    private static SpeechRecognizer mIat;
    // 语音听写UI
    private static RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private static HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    //2-初始化配置
    public static void init(Context context,InitListener initListener){
        //创建语音识别实例对象
        mIat= SpeechRecognizer.createRecognizer(context, initListener);
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(context, initListener);
    }
    //3-设置听写参数
    public static void setParam(){
        //清空缓存的语音数据
        mIatResults.clear();
        //设置听写引擎(本地)
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");// 设置返回结果格式
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");//应用领域
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");//语言区域
    }

    /*4-不显示听写对话框*/
    public static void noDialog(RecognizerListener mRecoListener){
        //方式一：
        mIat.startListening(mRecoListener);//普通监听器
    }
    /*5-显示听写对话框*/
    public static void showDialog(RecognizerDialogListener mRecoDialogListener){
        //方式二：
        mIatDialog.setListener(mRecoDialogListener);//听写UI监听器
        mIatDialog.show();
    }

    /*6-将监听方法中获取到的数据转换成文字*/
    public static String changeResultToText(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());//获取字符数据
        String sn = null;
        // 读取json结果中的sn字段(需要提前数据的关键字)
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //获取对应关键字部分的数据
        mIatResults.put(sn, text);//参数1:关键字,参数2:键值
        //创建一个字符串操作实例
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();//最后获得转换后的文字
        //mResultText.setSelection(mResultText.length());
    }



}



