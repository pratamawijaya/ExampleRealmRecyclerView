package com.pratamawijaya.examplerealm.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.pratamawijaya.examplerealm.R;
import com.pratamawijaya.examplerealm.model.Category;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import java.util.Date;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.container) CoordinatorLayout container;
  @Bind(R.id.fab_add_book) FloatingActionButton fabAddBook;
  @Bind(R.id.fab_add_category) FloatingActionButton fabAddCategory;

  private Realm realm;
  private RealmResults<Category> categoryRealmResults;
  private RealmChangeListener realmChangeCategoryListener;
  private RealmChangeListener realmChangeBookListener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    realm = Realm.getDefaultInstance();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Example of Realm");

    fabAddBook.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        showAddBookDialog();
      }
    });

    fabAddCategory.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        showAddCategory();
      }
    });

    setupListener();

    categoryRealmResults.addChangeListener(realmChangeCategoryListener);
  }

  private void setupListener() {
    realmChangeCategoryListener = new RealmChangeListener() {
      @Override public void onChange() {
        Timber.d("onChange(): category change");
      }
    };

    realmChangeBookListener = new RealmChangeListener() {
      @Override public void onChange() {

      }
    };
  }

  private void showAddCategory() {
    new MaterialDialog.Builder(this).title("Add Category")
        .inputType(InputType.TYPE_CLASS_TEXT)
        .input("Category", "", new MaterialDialog.InputCallback() {
          @Override public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            writeNewCategory(input.toString());
          }
        })
        .show();
  }

  /**
   * write category to database
   */
  private void writeNewCategory(final String txtCategory) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        Category category = new Category(txtCategory, new Date());
        realm.copyToRealmOrUpdate(category);
      }
    });
  }

  private void showAddBookDialog() {
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

  @Override protected void onStart() {
    super.onStart();
    categoryRealmResults = realm.where(Category.class).findAllAsync();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    realm.close();
    categoryRealmResults.removeChangeListener(realmChangeCategoryListener);
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
