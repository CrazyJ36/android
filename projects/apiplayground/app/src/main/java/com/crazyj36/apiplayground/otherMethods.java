package com.crazyj36.apiplayground;

import android.content.Context;
import android.widget.Toast;

class otherMethods {

    private Context context;

    void testToast() {
        Toast.makeText(context.getApplicationContext(), "Test Toast", Toast.LENGTH_SHORT).show();
    }

}



