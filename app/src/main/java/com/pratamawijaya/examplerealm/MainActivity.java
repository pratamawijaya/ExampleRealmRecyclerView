package com.pratamawijaya.examplerealm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import io.realm.Realm;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.container) CoordinatorLayout container;

  private Realm realm;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    realm = Realm.getDefaultInstance();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //    .setAction("Action", null)
        //    .show();
        showAddDialog();
      }
    });
  }

  private void showAddDialog() {
    new AlertDialogWrapper.Builder(this).setTitle("Title")
        .setMessage("Message")
        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            Snackbar.make(container, "Yes", Snackbar.LENGTH_SHORT)
                .setAction("Ok", new View.OnClickListener() {
                  @Override public void onClick(View view) {
                    Timber.d("onClick(): snackbar ok");
                  }
                })
                .show();
          }
        })
        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            Snackbar.make(container, "No", Snackbar.LENGTH_SHORT)
                .setAction("No", new View.OnClickListener() {
                  @Override public void onClick(View view) {
                    Timber.d("onClick(): snackbar No");
                  }
                })
                .show();
          }
        })
        .show();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    realm.close();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
