package jcenter.dzm.com.jcenterproject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dzm.jcenter.share.ShareApi;
import com.dzm.jcenter.share.config.ShareConfig;
import com.dzm.jcenter.share.config.ShareType;
import com.dzm.jcenter.share.dao.qq.ShareQQTextImageDao;
import com.dzm.jcenter.share.dao.wb.ShareWbMixDao;
import com.dzm.jcenter.share.dao.wx.ShareWXTextDao;
import com.dzm.jcenter.share.listener.ShareListener;

public class MainActivity extends AppCompatActivity implements ShareListener {

    private ShareApi shareApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareConfig.setQQ("222222");
        ShareConfig.setWeiXin("wxd63f1ad5539ae03f");
        ShareConfig.setWeiBo(Constants.APP_KEY,Constants.SCOPE,Constants.REDIRECT_URL);
        shareApi = ShareApi.getInstance(this);
        findViewById(R.id.qqShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareQQTextImageDao shareTextImageDao = new ShareQQTextImageDao();
                shareTextImageDao.title = "勇哥傻逼";
                shareTextImageDao.target_url = "http://blog.csdn.net/qq_16628781/article/details/49337565";
                shareTextImageDao.summary = "到底是不是傻逼";
                shareTextImageDao.app_name = "测试";
                shareApi.share(shareTextImageDao,null, ShareType.QQ);
            }
        });
        findViewById(R.id.qZoneShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareQQTextImageDao shareTextImageDao = new ShareQQTextImageDao();
                shareTextImageDao.title = ("勇哥傻逼");
                shareTextImageDao.target_url = ("http://blog.csdn.net/qq_16628781/article/details/49337565");
                shareTextImageDao.summary = ("到底是不是傻逼");
                shareTextImageDao.app_name = ("测试");
                shareTextImageDao.isToQozen = (true);
                shareApi.share(shareTextImageDao,null, ShareType.QQ);
            }
        });
        findViewById(R.id.bt_share_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWXTextDao wxTextDao = new ShareWXTextDao();
                wxTextDao.message = ("测试测试");
                wxTextDao.isFrendCircle = (false);
                shareApi.share(wxTextDao,null,ShareType.WEIXIN);
            }
        });

        findViewById(R.id.bt_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareWbTextDao textDao = new ShareWbTextDao();
//                textDao.share_text = "微博测试";
                ShareWbMixDao w = new ShareWbMixDao();
                w.share_text  =  "微博测试";
                w.share_bitmap_resource = R.mipmap.test;
                w.share_media_actionurl = "http://www.baidu.com";
                w.share_media_title = "测试";
                shareApi.share(w,MainActivity.this,ShareType.WEIBO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareApi.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onComplete(ShareType shareType) {
        Toast.makeText(this,"onComplete",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(ShareType shareType, String err_msg) {
        Toast.makeText(this,"onError",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(ShareType shareType) {
        Toast.makeText(this,"onCancel",Toast.LENGTH_SHORT).show();
    }
}
