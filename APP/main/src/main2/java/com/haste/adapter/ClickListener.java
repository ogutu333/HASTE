package com.haste.adapter;

import android.view.View;

/**
 * Created by Deborah on 29/02/24.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
