/* This demonstrates the simplest dialog  in the android api.
 */

package com.crazyj36.dialogs;

public class MainActivity extends android.app.Activity {

    private android.app.Dialog dialog;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new android.app.Dialog(MainActivity.this);
        dialog.setTitle("Dialog Title");
    }

    public void showDialog(android.view.View view) {
        dialog.show();
    }

}
