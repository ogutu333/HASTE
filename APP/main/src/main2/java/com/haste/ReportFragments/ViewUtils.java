package com.haste.ReportFragments;

import android.widget.RadioGroup;

/**
 * Created by Deborah on 29/02/24.
 */

public class ViewUtils {

    static public void setRadioGroupClickable(RadioGroup group, boolean clickable){
        for(int i = 0; i < group.getChildCount(); i++){
            group.getChildAt(i).setEnabled(clickable);
        }
    }



}
