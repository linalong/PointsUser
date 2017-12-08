package com.heizi.pointsuser.utils;

import android.content.Context;

import com.heizi.pointsuser.R;
import com.hz.niftymodaldialogeffects.NiftyDialogBuilder;



public class UtilDialog {

    static NiftyDialogBuilder instance;
    private static Context tmpContext;

    /**
     * 消失
     */
    public static void dismiss() {
        if (instance != null && instance.isShowing()) {
            instance.dismiss();
        }
    }

    private static NiftyDialogBuilder getInstance(Context context) {

        if (instance == null || !tmpContext.equals(context)) {
            synchronized (NiftyDialogBuilder.class) {
                if (instance == null || !tmpContext.equals(context)) {
                    instance = new NiftyDialogBuilder(context,
                            R.style.dialog_untran);
                }
            }
        }
        tmpContext = context;
        return instance;

    }


}
