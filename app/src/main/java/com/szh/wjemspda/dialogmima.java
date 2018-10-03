package com.szh.wjemspda;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.administrator.saomiao4xinshiqi.R;

public class dialogmima extends Dialog {
    public dialogmima(@NonNull Context context) {
        super(context);
    }

    public dialogmima(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected dialogmima(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mima);

    }
}
