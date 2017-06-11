package igor.com.br.tccrestwsandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import igor.com.br.tccrestwsandroid.R;

/**
 * Created by Igor on 14/02/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public static ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setSharedPreferences(Context mContext, String nome,String json){
        SharedPreferences settings = mContext.getSharedPreferences(mContext.getString(R.string.preferencias), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(nome, json);
        editor.commit();
    }

    public String getSharedPreferences(Context mContext, String nome){
        return mContext.getSharedPreferences(mContext.getString(R.string.preferencias), 0).getString(nome,"");
    }


    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
}
