package com.dzm.jcenter.update;

import java.io.File;

/**
 * Created by 83642 on 2017/6/16.
 */

public abstract class OnFileUpdateListener {

    public abstract void updateSpeed(String speed);

    public void onStart(int max){}

    public abstract void onUpdate(float ratio,int schedule);

    public void onEnd(File file){}

    public abstract void onErre(String err);
}
