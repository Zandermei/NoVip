package zd.cn.novipvideo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 网络请求工具类
 */

public class OkHttpUtils {

    private static Context mContex;
    private final static long CONNECT_TIMEOUT = 10000;
    private final static long READ_TIMEOUT = 10000;
    private final static long WRITE_TIMEPUT = 10000;

    public static final int INTENT_CODE = 1001;//
    public static final int HTTP_REQUESTSUCESS = 50001;//成功
    public static final int HTTP_REQUESTFAIL = 50002;//失败
    public static final int HTTP_REQUEST_PARAMSFAIL = 5003;//请求参数错误
    public static final int HTTP_RESPONSE_PHONEFAIL = 5004;//手机号有误
    public static final int HTTP_RESPONSE_YANZFAIL = 5005;//验证码错误
    public static final int HTTP_RESPONSE_UUIDFAIL = 5006;//UUID错误
    public static final int HTTP_RESPONSEFAIL = 50007;//请求回来但没有成功
    public static final int HTTP_RESPONSE_EXCEPTION = 50008;//请求回来但没有成功

    private static OkHttpClient mOkHttpClient;

    /**
     * 因为我们请求数据一般都是子线程中请求，在这里我们使用了handler
     */
    private static Handler mHandler;


    //创建 单例模式（OkHttp官方建议如此操作）
    public static OkHttpClient getInstance() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();

        }
        return mOkHttpClient;
    }

    public static void initEvent(Context context) {
        mContex = context;
        /**
         * 在这里直接设置连接超时.读取超时，写入超时
         */
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时
                .writeTimeout(WRITE_TIMEPUT, TimeUnit.SECONDS)//设置写超时
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时
                .build();
        /**
         * 初始化handler
         */
        mHandler = new Handler(Looper.getMainLooper());

    }
    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responsecallback
     */
    public static void enqueue(Request request, Callback responsecallback) {

        try {
            getInstance().newCall(request).enqueue(responsecallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送get请求 通用---------
     * params json
     */
    public static void sendGetRequest(String url, String params, final Handler handler) {
        url += params;
        AppLog.e("GET请求=="+url);
        Request request = new Request.Builder().url(url).build();
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message httpMsg = Message.obtain();
                httpMsg.what = HTTP_REQUESTFAIL;
                httpMsg.obj = e;
                if (handler != null) {
                    handler.sendMessage(httpMsg);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onRequestResponse(call, response, handler);
            }
        });
    }




    /**
     * 发送get请求 通用---------
     * params json
     */
    public static void sendGetRequestMov(String url, Map<String,String> params, final Handler handler) {
        url += setUrlParamss(params);
        AppLog.e("GET请求=="+url);
        Request request = new Request.Builder().url(url).build();
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message httpMsg = Message.obtain();
                httpMsg.what = HTTP_REQUESTFAIL;
                httpMsg.obj = e;
                if (handler != null) {
                    handler.sendMessage(httpMsg);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onRequestResponse(call, response, handler);
            }
        });
    }



    /**
     * 发送get返回  1  2  成功的请求
     * params json
     */

    public static void sendGetRequestKeyInfo(String url, String params, final Handler handler) {
        //url += UrlUtils.getUrlParams();
        Request request = new Request.Builder().url(url).build();
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message httpMsg = Message.obtain();
                httpMsg.what = HTTP_REQUESTFAIL;
                httpMsg.obj = e;
                if (handler != null) {
                    handler.sendMessage(httpMsg);
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onRequestResponseKeyInfo(call, response, handler);
            }
        });
    }

    /**
     * 返回  1  2  成功的请求
     * @param call
     * @param response
     * @param handler
     */
    private static void onRequestResponseKeyInfo(Call call, Response response, Handler handler) {
        Message httpMsg = Message.obtain();
        if (response.isSuccessful()) {
            try {
                String result = response.body().string();
                JSONObject obj = String2JsonObject(result);
                AppLog.e("按键信息返回=="+obj.toString());
                if (obj.optString("status").equals("1")) {
                    httpMsg.what = HTTP_REQUESTSUCESS;
                    httpMsg.obj = obj;
                } else if (obj.optString("status").equals("-1")) {
                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
                    httpMsg.obj = obj.optString("status");
                } else {
                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
                    httpMsg.obj = obj.optString("status");
                }
            } catch (Exception e) {
                httpMsg.what = HTTP_RESPONSE_EXCEPTION;
                httpMsg.obj = e.getMessage();
            }
        } else {
            httpMsg.what = HTTP_RESPONSEFAIL;
            httpMsg.arg1 = response.code();
        }
        if (handler != null && httpMsg != null) {
            handler.sendMessage(httpMsg);
        }
    }
    /**
     * 装锁师傅审核
     * 可添加图片
     */
    public static void sendPoastMaster(String url, Map<String,String> params, final Handler handler) {
        FormBody.Builder builder = new FormBody.Builder();
        for(String keys : params.keySet()){
            builder.add(keys,params.get(keys));
        }
        try {
            final Request request = new Request.Builder().url(url).post(builder.build()).build();
            AppLog.e("post=="+request.toString());
            AppLog.e("post=="+url+"="+builder.build());
            enqueue(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message httpMsg = Message.obtain();
                    httpMsg.what = HTTP_REQUESTFAIL;
                    httpMsg.obj = e.getMessage();
                    if (handler != null) {
                        handler.sendMessage(httpMsg);
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    onRequestResponseMaster(call, response, handler);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void onRequestResponseMaster(Call call, Response response, Handler handler) {
        Message httpMsg = Message.obtain();
        if (response.isSuccessful()) {
            try {
                String result = response.body().string();
                JSONObject object = String2JsonObject(result);
                AppLog.e("服务器返回=》"+object.toString());
                AppLog.e("服务器返回string=》"+result);

                if (object.getInt("status") > 0) {
                    httpMsg.what = HTTP_REQUESTSUCESS;
                    httpMsg.obj = object;
                } else{
                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
                    httpMsg.obj = object.getString("status");
                }
            } catch (Exception e) {
                if (httpMsg != null) {
                    httpMsg.what = HTTP_RESPONSE_EXCEPTION;
                    httpMsg.obj = e;
                }
            }
        } else {
            httpMsg.what = HTTP_RESPONSEFAIL;
            httpMsg.arg1 = response.code();
        }
        if (handler != null && httpMsg != null) {
            handler.sendMessage(httpMsg);
        }
    }





    /**
     * 发送post请求 json字符串
     * 可添加图片
     */
    public static void sendPoastInstallRequest(String url, Map<String,String> params, final Handler handler) {

        FormBody.Builder builder = new FormBody.Builder();
        for(String keys : params.keySet()){
            builder.add(keys,params.get(keys));
        }
        try {
            final Request request = new Request.Builder().url(url).post(builder.build()).build();
            AppLog.e("post=="+request.toString());
            AppLog.e("post=="+url+"="+builder.build());
            enqueue(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message httpMsg = Message.obtain();
                    httpMsg.what = HTTP_REQUESTFAIL;
                    httpMsg.obj = e.getMessage();
                    if (handler != null) {
                        handler.sendMessage(httpMsg);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    onRequestInstallResponse(call, response, handler);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送post请求 json字符串
     * 可添加图片
     */
    public static void sendPoastRequest(String url, Map<String,String> params, final Handler handler) {

        FormBody.Builder builder = new FormBody.Builder();
        for(String keys : params.keySet()){
            builder.add(keys,params.get(keys));
        }
        try {
            final Request request = new Request.Builder().url(url).post(builder.build()).build();
            AppLog.e("post=="+request.toString());
            AppLog.e("post=="+url+"="+builder.build());
            enqueue(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message httpMsg = Message.obtain();
                    httpMsg.what = HTTP_REQUESTFAIL;
                    httpMsg.obj = e.getMessage();
                    if (handler != null) {
                        handler.sendMessage(httpMsg);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    onRequestResponse(call, response, handler);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void onRequestResponse(Call call, Response response, Handler handler) {
        Message httpMsg = Message.obtain();
        if (response.isSuccessful()) {
            try {
                String result = response.body().string();
                AppLog.e("服务器返回string=》"+result);
                httpMsg.what = HTTP_REQUESTSUCESS;
                httpMsg.obj = result;
            } catch (Exception e) {

            }
        } else {
            httpMsg.what = HTTP_RESPONSEFAIL;
            httpMsg.obj = response.message();
        }
        if (handler != null && httpMsg != null) {
            handler.sendMessage(httpMsg);
        }
    }



    //安装审核
    private static void onRequestInstallResponse(Call call, Response response, Handler handler) {
        Message httpMsg = Message.obtain();
        if (response.isSuccessful()) {
            try {
                String result = response.body().string();
                JSONObject object = String2JsonObject(result);
                AppLog.e("服务器返回=》"+object.toString());
                AppLog.e("服务器返回string=》"+result);

                    httpMsg.what = HTTP_REQUESTSUCESS;
                    httpMsg.obj = result;


            } catch (Exception e) {
                if (httpMsg != null) {
                    httpMsg.what = HTTP_RESPONSE_EXCEPTION;
                    httpMsg.obj = e;
                }
            }
        } else {
            httpMsg.what = HTTP_RESPONSEFAIL;
            httpMsg.arg1 = response.code();
        }
        if (handler != null && httpMsg != null) {
            handler.sendMessage(httpMsg);
        }
    }




    /**
     * 构造一个图片post请求的request
     *
     * @param params
     * @return
     */
    private static RequestBody structurePostParams(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        try {
            for (String name : params.keySet()) {
                Object value = params.get(name);
                if (params.get(name) instanceof File) {// 文件
                    File file = (File) value;
                    builder.addFormDataPart(name, file.getName(),
                            RequestBody.create(null, file));
                    Log.i("======file", file.getAbsolutePath());

                } else {
                    if (value == null)
                        value = "";
                    builder.addFormDataPart(name,
                            URLEncoder.encode(value.toString(), "UTF-8"));
                }
            }
        } catch (Exception e) {
        }
        return builder.build();
    }


    private static String structureGetParams(Map<String, Object> params) {
        String str = "";
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (str.length() == 0) {
                    str += "";
                } else {
                    str += "&";
                    String value = "";
                    if (entry.getValue() != null) {
                        value = entry.getValue().toString();
                        str += entry.getKey() + "=" + URLEncoder.encode(value, "UTF-8");
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AppLog.e("utf-8::编码"+str);
        return str;
    }

    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    public static String setUrlParams(Map<String, Object> mapParams) {
        String strParams = "";
        if (mapParams != null) {
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                strParams += "&" + key + "=" + mapParams.get(key);
            }
        }

        return strParams;
    }


    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    public static String setUrlParamss(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams != null) {
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                try {
                    strParams += "&" + key + "=" + URLEncoder.encode(mapParams.get(key),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }



        return strParams;
    }


    /**
     * post请求 返回 1  2 值
     * @param url
     * @param json
     * @param handler
     */
    public static void sendHttpPostConnect(final String url, final String json, final Handler handler) {
        final Message httpMsg = Message.obtain();


        new Thread(){
            @Override
            public void run() {
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setInstanceFollowRedirects(true);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setRequestProperty("ContentType", "application/x-www-form-urlencode");
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.connect();
                    DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                    outputStream.write(json.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    //获得响应状态
                    if (urlConnection.getResponseCode() == 200) {
                        StringBuffer sb=new StringBuffer();
                        String readLine=new String();
                        BufferedReader responseReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
                        while((readLine=responseReader.readLine())!=null){
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        String params = sb.toString();
                        try {
                            JSONObject object = String2JsonObject(params);
                            AppLog.e("服务器返回结果》"+object);
                            if(object.getString("status").equals("1") ||object.getString("status").equals("2")){
                                httpMsg.what = HTTP_REQUESTSUCESS;
                                httpMsg.obj = object;
                            }else if(object.getString("status").equals("-1")){
                                httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
                                httpMsg.obj =object.getString("status");
                            }else {
                                httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
                                httpMsg.obj =object.getString("status");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }else {
                        httpMsg.what = HTTP_REQUESTFAIL;
//                        httpMsg.arg1 = urlConnection.getResponseCode();
                    }
                    if(httpMsg != null && handler != null){
                        handler.sendMessage(httpMsg);
                    }

                }
                catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }.start();



    }



    /**
     * post请求 返回 1  2 值
     * @param url
     * @param json
     * @param handler
//     */
//    public static void sendHttpPostConnect2(final String url, final String json, final Handler handler) {
//        final Message httpMsg = Message.obtain();
//        if(!NetUtils.getNetWordState()){
//            httpMsg.what = HTTP_REQUESTFAIL;
//            if (handler != null) {
//                handler.sendMessage(httpMsg);
//            }
//        }else {
//
//            new Thread(){
//                @Override
//                public void run() {
//                    try {
//                        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
//                        urlConnection.setDoInput(true);
//                        urlConnection.setDoOutput(true);
//                        urlConnection.setRequestMethod("POST");
//                        urlConnection.setUseCaches(false);
//                        urlConnection.setInstanceFollowRedirects(true);
//                        urlConnection.setConnectTimeout(NetworkURL.TimeoutTime);
//                        urlConnection.setRequestProperty("ContentType", "application/x-www-form-urlencode");
//                        urlConnection.setRequestProperty("Charset", "UTF-8");
//                        urlConnection.connect();
//                        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
//                        outputStream.write(json.getBytes("UTF-8"));
//                        AppLog.e("故障发送数据="+json.getBytes("UTF-8"));
//                        outputStream.flush();
//                        outputStream.close();
//                        //获得响应状态
//                        if (urlConnection.getResponseCode() == 200) {
//                            StringBuffer sb=new StringBuffer();
//                            String readLine=new String();
//                            BufferedReader responseReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
//                            while((readLine=responseReader.readLine())!=null){
//                                sb.append(readLine).append("\n");
//                            }
//                            responseReader.close();
//                            String params = sb.toString();
//                            try {
//                                JSONObject object = CommentUtils.String2JsonObject(params);
//                                AppLog.e("服务器返回-sendHttpPostConnect2-----"+object);
//                                if(object.getString("status").equals("1")){
//                                    httpMsg.what = HTTP_REQUESTSUCESS;
//                                    httpMsg.obj = object;
//                                }else if(object.getString("status").equals("-1")){
//                                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
//                                    httpMsg.obj =object.getString("status");
//                                }else {
//                                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
//                                    httpMsg.obj =object.getString("status");
//                                }
//                            } catch (JSONException e) {
//
//                                e.printStackTrace();
//                            }
//                        }else {
//                            httpMsg.what = HTTP_REQUESTFAIL;
////                        httpMsg.arg1 = urlConnection.getResponseCode();
//                        }
//                        if(httpMsg != null && handler != null){
//                            handler.sendMessage(httpMsg);
//                        }
//
//                    }
//                    catch (IOException e) {
//
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//
//        }//
//    }
//
//
//    public static void sendHttpPostConnectCallPhone(final String url, final String json, final Handler handler) {
//        final Message httpMsg = Message.obtain();
//        if(!NetUtils.getNetWordState()){
//            httpMsg.what = HTTP_REQUESTFAIL;
//            if (handler != null) {
//                handler.sendMessage(httpMsg);
//            }
//            return;
//        }
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
//                    urlConnection.setDoInput(true);
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setRequestMethod("POST");
//                    urlConnection.setUseCaches(false);
//                    urlConnection.setInstanceFollowRedirects(true);
//                    urlConnection.setConnectTimeout(NetworkURL.TimeoutTime);
//                    urlConnection.setRequestProperty("ContentType", "application/x-www-form-urlencode");
//                    urlConnection.setRequestProperty("Charset", "UTF-8");
//                    urlConnection.connect();
//                    DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
//                    outputStream.write(json.getBytes("UTF-8"));
//                    outputStream.flush();
//                    outputStream.close();
//                    //获得响应状态
//                    if (urlConnection.getResponseCode() == 200) {
//                        StringBuffer sb=new StringBuffer();
//                        String readLine=new String();
//                        BufferedReader responseReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
//                        while((readLine=responseReader.readLine())!=null){
//                            sb.append(readLine).append("\n");
//                        }
//                        responseReader.close();
//                        String params = sb.toString();
////                            try {
//                        JSONObject object = CommentUtils.String2JsonObject(params);
//                        AppLog.e("服务器返回-sendHttpPostConnect2-----"+object+"params="+params);
////                                if(object.getString("status").equals("1")){
//                        httpMsg.what = HTTP_REQUESTSUCESS;
//                        httpMsg.obj = params;
////                                }else if(object.getString("status").equals("-1")){
////                                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
////                                    httpMsg.obj =object.getString("status");
////                                }else {
////                                    httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
////                                    httpMsg.obj =object.getString("status");
////                                }
////                            } catch (JSONException e) {
////
////                                e.printStackTrace();
////                            }
//                    }else {
//                        httpMsg.what = HTTP_REQUESTFAIL;
////                        httpMsg.arg1 = urlConnection.getResponseCode();
//                    }
//                    if(httpMsg != null && handler != null){
//                        handler.sendMessage(httpMsg);
//                    }
//
//                }
//                catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
//
//



    /**
     * post请求 返回 1
     * @param url
     * @param json
     * @param handler
//     */
//    public static void sendPostCheckAccount(final String url, final String json, final Handler handler) {
//
//        AppLog.e("检测地址="+url);
//        final Message httpMsg = Message.obtain();
//        if(!NetUtils.getNetWordState()){
//            httpMsg.what = HTTP_REQUESTFAIL;
//            if (handler != null) {
//                handler.sendMessage(httpMsg);
//            }
//            return;
//        }
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
//                    urlConnection.setDoInput(true);
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setRequestMethod("POST");
//                    urlConnection.setUseCaches(false);
//                    urlConnection.setInstanceFollowRedirects(true);
//                    urlConnection.setConnectTimeout(10000);
//                    urlConnection.setRequestProperty("ContentType", "application/x-www-form-urlencode");
//                    urlConnection.setRequestProperty("Charset", "UTF-8");
//                    urlConnection.connect();
//                    DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
//                    outputStream.write(json.getBytes("UTF-8"));
//                    outputStream.flush();
//                    outputStream.close();
//                    //获得响应状态
//                    if (urlConnection.getResponseCode() == 200) {
//                        StringBuffer sb=new StringBuffer();
//                        String readLine=new String();
//                        BufferedReader responseReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
//                        while((readLine=responseReader.readLine())!=null){
//                            sb.append(readLine).append("\n");
//                        }
//                        responseReader.close();
//                        String params = sb.toString();
//                        try {
//                            JSONObject object = String2JsonObject(params);
//                            AppLog.e("返回结果"+object.toString());
//                            if(object.getString("status").equals("1")){
//                                httpMsg.what = HTTP_REQUESTSUCESS;
//                                httpMsg.obj = object;
//                            }else if(object.getString("status").equals("-1")){
//                                httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
//                                httpMsg.obj =object.getString("status");
//                            }else {
//                                httpMsg.what = HTTP_REQUEST_PARAMSFAIL;
//                                httpMsg.obj =object.getString("status");
//                            }
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                        }
//                    }else {
//                        httpMsg.what = HTTP_REQUESTFAIL;
////                        httpMsg.arg1 = urlConnection.getResponseCode();
//                    }
//                    if(httpMsg != null && handler != null){
//                        handler.sendMessage(httpMsg);
//                    }
//
//                }
//                catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }


    /**
     * 发送post请求
     * @param url			请求的url
     * @param params		请求的参数
     * @param handler		请求结果的回调函数
     * @param
     */
    public static void sendPostRequestVideo(String url, Map<String, String> params,
                                       final Handler handler) {
//        if(params == null)
//            params = new HashMap<>();

        RequestBody formBody = structurePostRequest(params);


        try{

            Request request;
                request = new Request.Builder()
                        .url(url)
                        .post(formBody).build();
            enqueue(request, new Callback() {

                @Override
                public void onResponse(Call arg0, Response arg1) throws IOException {
                    // TODO Auto-generated method stub
                    onRequestResponse(arg0, arg1, handler);
                }

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    // TODO Auto-generated method stub
                    Message httpMsg = Message.obtain();
                    httpMsg.what = HTTP_REQUESTFAIL;
                    httpMsg.obj = arg1;
                    AppLog.e("请求失败："+arg1.getMessage());
                    if(handler != null){
                        handler.sendMessage(httpMsg);
                    }
                }
            });
        } catch (Exception e){
        }
    }

    /**
     * 构造带一个图片的post请求的request
     *
     * @param "url
     * @param params
     * @return
     */
    private static RequestBody structurePostRequest(Map<String, String> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        try {
            for (String name : params.keySet()) {
                Object value = params.get(name);
//                if (params.get(name) instanceof File) {// 文件
//                    File file = (File) value;
//                    builder.addFormDataPart(name, file.getName(),
//                            RequestBody.create(null, file));
//                    AppLog.e("======file"+file.getAbsolutePath());
//
//                } else {
                    if (value == null)
                        value = "";
                    builder.addFormDataPart(name,
                            URLEncoder.encode(value.toString(), "UTF-8"));
              //  }
            }
        } catch (Exception e) {

        }
        return builder.build();
    }

    /**
     * 将服务器传来的字符串解析成jsonObject
     */

    public static JSONObject String2JsonObject(String str) {

        if (str == null)
            return null;

        try {
            JSONTokener jsonParser = new JSONTokener(str);
            JSONObject obj = (JSONObject) jsonParser.nextValue();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
}
