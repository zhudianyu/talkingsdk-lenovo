package com.talkingsdk.lenovo;

import android.app.Activity;
import android.widget.Toast;
import android.os.Looper;
import android.util.Log;
import android.text.TextUtils;
import android.app.Application;
import android.os.Handler;
import android.content.Context;
import android.view.Window;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.talkingsdk.Cocos2dxBaseActivity;
import com.talkingsdk.MainApplication;
import com.talkingsdk.SdkCommonObject;
import com.talkingsdk.models.LoginData;
import com.talkingsdk.models.PayData;

import com.lenovo.lsf.gamesdk.LenovoGameApi;

import com.lenovo.lsf.gamesdk.LenovoGameApi.GamePayRequest;
import com.lenovo.lsf.gamesdk.LenovoGameApi.IPayResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi.IQuitCallback;
public abstract class SdkObject extends SdkCommonObject {
    private boolean _restart = false;
    private boolean _isNewUser = false;
    private  LoginData ld = null;
    private  PayData _payData = null;
    private LoginCode _loginCode = null;

    private String mUserName ;      //登录或注册成功返回的用户名
    private String mToken;          //登录或注册成功返回的Token
    private static final String TAG = SdkObject.class.getSimpleName();
           //调用支付接口使用open appid，申请流程详见文档
    //此参数集成时需要修改
    public static final String appid = "1410232134070.app.ln";
    private static final int MSG_LOGIN_AUTO_ONEKEY = 2;  
    
    //支付密钥，申请流程详见文档
    //此参数集成时需要修改
    //public static final String appkey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMwDhOsReixD9ziX6ZbOjBejeLIkDTDgz+0fLdre5/JufQTgsDpUA99lT0R3nXe8+p/rBAjPTQokxZVW9CqPCrc6hn5Ub8b+tLzkTTSRMjIo6GYtNk+HT9158hxVjv1Ym2EyazTha889YDklYUjfJ5h55m/PSKC1ChCTOp04r/EHAgMBAAECgYAGhYpHNWzRY27QuVJ3Rq0FyG0ATNS7HkSnUNrXZ07E2jFW6ZPYmXzyNDvfdW9sN3dPi2S6n13YGCrUxk3R2na+LAYYYrbkWpNb2FL7+XUCA6Qh4sWwX3T7W1Woj5x2XCIc48Eib9ajU+JKQuhSsLmkZnqAefBX5mndV1VReccNwQJBAP92h9s3lUfmwQARLabpvW5np8Gbtmif2+kwX9JTWDZwVxUmmEPEc+64dwzxSCcMD3UCg3jqOjqFUAUUjs8FdGECQQDMcU2AB069UO0lBOFY309hKPFjOW9PpQgAzvI+7DisUE5wHR6DX1z0RvYgbldZnBHdEQTYSOneYZb8C1Gb0t5nAkA/ANmirAOqFvP0c0giCTWJ3BCYhDGpfft7eE25joqL6orfkYQt+m5pKp9Z1svnrWbGgdcw4/t2YViJ2DccQYkhAkACVvGaG3tNM1XjbJDfoX6ZEAzjI88gTDUxPS0Mh0uGvUyIf+zPeBHqQb2jVV/uiJizvPQfPYZfoyJFNCP08o7VAkEAwQjgfogLU7PYqznr+Sg9xqA0+vuIYRnaYzNRWEb6T6XdqDY0EF/fw7zEGNLJWeN3grbfcAUAmzt/VDCmKyEU3A==";
    public static final String appkey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIOH4o9cE0zxHr/IvD7fdoeM/1oamSBwQjS61OqlmG6jhoZ1gmFMbdlH+HCO/EZ8KOQ9JSux9YhEGK+KVhxGdZ0lN/1YhRH0Ynk0zJooocXLq87S2scPem3GWAESlKsg605PutnoDyKwQvdns67nPZvwRRMeh7nfXMiFBWdXtwEzAgMBAAECgYA9mhjMF82aTZufKv6vW62B0tGNe8OX47u+QnqR4zi/KKtKsiJ8O3V/PCvpW65fvKrSKqkMC+75ARumq12lJILUhBIVj8Ygcn8BYy1BtNeCTwAxCwJtOEyaiFr0sglJbJRLN3OVJNFfO6zgmiqY1/ni6Lx/d2t6fUVU8qZu5TNP0QJBAOe5lCbbha7rHQxcFmEgOnDe3yYf7K8V7ZiM9qWzkEjuxpig9sZPu272R2eGzck+mkls3vqjNcNKl7BF4U2VTNUCQQCRT0pvkTm6yWnrCDxeOTT17XrmMxpXXqfN7tQ0WSk0+/Kw9Lesz6IhJ57YGRqtgvGu2zRblPdVkjsKMtMw9nnnAkA8q6stjVZwGODvJoE5ht2mRcQ5UCyBHwWpZmcBtYT2g4X92k8iVyflAphpc7MXmMt+pAGxr9/YtQQIRBOcY5XNAkBYoGfiDE2No3M6qtdHENVAegvPg7O5Pj5S2CwNkaQUcObhDyFIAYv9dNDpNMaUtZz67S/N+9mvE3V3DvDImExZAkBq6iJqFZx82UR1w+a+l56dlyPTX6It2px5sw2aW/QFaLHO1SClakXKvUHL4/xyo/gVHzDQ1M9JD9mKQAJ6vynb";
    private static final String CONTENTTYPE = "application/x-www-form-urlencoded";
    private static final int MSG_CHECK_PAY_SUCCESS = 10;
    private static final int MSG_GET_ORDER_ID_SUCCESS = 11;
    private static final int MAX_REQUEST_COUNT = 50;
    private String mlpsustdata;
    
    private static final String hostUrl = "http://cashier.lenovomm.com:8085/";//"http://uss.test.lenovomm.cn/";//游戏服务器url
    
    @Override
    public void onActivityCreate(Activity parentActivity) {
        super.onActivityCreate(parentActivity);
        //如有初始化 可以写这里
 
        //SDK初始化
    LenovoGameApi.doInit(getParentActivity(),appid);
    
    // 调用快速登录接口
    /**
       * 什么是快速登录：
       * 如果用户已经在系统中登录，则本接口会通过SSO机制直接返回登录结果;
       * 如果用户尚未在系统中登录，则本接口会通过后台短信方式进行自动登录尝试，通常需要10~20s左右时间;
       * 由于自动登录需要发短信，因此请使用装有SIM卡的手机进行开发测试;
       * 登录期间，会弹出一个提示框，提示用户正在尝试自动登录;
       * 如果自动登录失败，会提示用户重试，以及引导用户使用其他方式登录。
       */
    //getTokenByQuickLogin();
        
    login();
    }

    @Override
    public void login() {
        //登陆逻辑写这里
        //请不要在回调函数里进行UI操作，如需进行UI操作请使用handler将UI操作抛到主线程
        LenovoGameApi.doAutoLogin(getParentActivity(), new LenovoGameApi.IAuthResult() {

            @Override
            public void onFinished(boolean ret, String data) {
                final Message msg = new Message();
                msg.what = MSG_LOGIN_AUTO_ONEKEY;
                msg.obj = data;
                if (ret) {
                    myHandler.sendMessage(msg);

                } else {
                    //后台快速登录失败(失败原因开启飞行模式、 网络不通等)
                    Log.i(TAG, "login fail");
                }
            }
        });
    }

    /**
     * 功能： 该方法的作用是Handler对传递过来的不同消息，进行不同的处理。 当传递过来的消息是MSG_LOGIN时，则对登录进行相应的处理；
     */
    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_LOGIN_AUTO_ONEKEY:
                // 登录成功
                // setContentView(R.layout.game_main);
                // showView((String)msg.obj);
                toastMakeText("登录成功");
                Intent i2 = new Intent( getParentActivity(), GameActivity.class );
                i2.putExtra( "from", "login" );
                Log.d(TAG, "======登录成功=========");
                getParentActivity().startActivity( i2 );
                break;
            }
        };
    };

    @Override
    public void onActivityDestroy() {
    }
    
    protected void doFinishLoginProcess(int code) {
        String tip = "登录失败，错误代码：" + code;
        toastMakeText(tip);
    }
    
    public void changeAccount() {
        getParentActivity().runOnUiThread(new Runnable() {
            public void run() {
                //切换账号
            }
        });
    }
   
    /**
     * 登出逻辑
     * */
    @Override
    public void logout() {
        getParentActivity().runOnUiThread(new Runnable() {
            public void run() {
                //登出逻辑写这里
            }
        });
        
    }

    protected void toastMakeText(final String text) {
        getParentActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getParentActivity(), text, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    protected void doFinishPayProcess(int code) {
        Log.d(TAG, "支付结束");
        PayCode payCode = PayCode.Success;
        onPaidRequest(_payData, payCode.ordinal());
        MainApplication.getInstance().notifyGamePaid(_payData,
                payCode.ordinal());
    }

    public void pay(PayData payData) {
        _payData = payData;
        //支付逻辑写这里

        /***********
         *  支付LenovoGameApi.doPay（） 接口 调用
         */
        GamePayRequest payRequest = new GamePayRequest();
        // 请填写商品自己的参数
        payRequest.addParam("notifyurl", "");//当前版本暂时不用，传空String
        payRequest.addParam("appid", appid);
        payRequest.addParam("waresid", 295);
        payRequest.addParam("exorderno", "sample" + System.currentTimeMillis());
        payRequest.addParam("price",1);
        payRequest.addParam("cpprivateinfo", "123456");


        LenovoGameApi.doPay(getParentActivity(),appkey, payRequest, new IPayResult() {
            @Override
            public void onPayResult(int resultCode, String signValue,
                    String resultInfo) {// resultInfo = 应用编号&商品编号&外部订单号
                
                if (LenovoGameApi.PAY_SUCCESS == resultCode) {
                    //支付成功
                    Toast.makeText(getParentActivity(), "sample:支付成功",Toast.LENGTH_SHORT).show();
                } else if (LenovoGameApi.PAY_CANCEL == resultCode) {
                    Toast.makeText(getParentActivity(), "sample:取消支付",Toast.LENGTH_SHORT).show();
                    // 取消支付处理，默认采用finish()，请根据需要修改
                    Log.e(TAG, "return cancel");
                } else {
                    Toast.makeText(getParentActivity(), "sample:支付失败",Toast.LENGTH_SHORT).show();
                    // 计费失败处理，默认采用finish()，请根据需要修改
                    Log.e(TAG, "return Error");
                }

            }
        });
    }


    private double floatToDouble(float f) {
        return Double.parseDouble(String.valueOf(f));
    }

    @Override
    public void onApplicationCreate(Application obj) {
    }

    @Override
    public void onAppTerminate() {
    }

    @Override
    public void setRestartWhenSwitchAccount(boolean restart) {
        _restart = restart;
    }

     @Override
    public LoginCode getLoginCode() {
        return LoginCode.Success;
    }

    private boolean isAppForeground = true;

    @Override
    public void onGameResume() {
        if (!isAppForeground) {
            isAppForeground = true;
        }
    }

    @Override
    public void onGameFade() {
        if (!isAppOnForeground()) {
            isAppForeground = false;
        }
    }

    public void onKeyBack() {
        Cocos2dxBaseActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LenovoGameApi.doQuit(getParentActivity(),  new IQuitCallback() {

                    @Override
                    public void onFinished(String data) {
                        getParentActivity().finish();
                    }

                });
            }
        });
    }
}
