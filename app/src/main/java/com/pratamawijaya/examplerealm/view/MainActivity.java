package com.pratamawijaya.examplerealm.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pratamawijaya.examplerealm.R;
import com.pratamawijaya.examplerealm.model.Book;
import com.pratamawijaya.examplerealm.model.Category;
import com.pratamawijaya.examplerealm.view.MainAdapter.MainAdapterListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener, MainAdapterListener {

  @Bind(R.id.container) CoordinatorLayout container;
  @Bind(R.id.fab_add_book) FloatingActionButton fabAddBook;
  @Bind(R.id.fab_add_category) FloatingActionButton fabAddCategory;
  @Bind(R.id.fab_menu) FloatingActionMenu fabMenu;
  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  private Spinner spCategory;
  private EditText etBookName, etAuthorName;
  private String bookName, authorName;
  private Realm realm;
  private RealmResults<Category> categoryRealmResults;
  private RealmResults<Book> bookRealmResults;
  private RealmChangeListener realmChangeCategoryListener;
  private RealmChangeListener realmChangeBookListener;

  private ArrayAdapter<String> spCategoryAdapter;
  private List<String> listCategory;
  private Category selectedCategory;
  private MainAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    realm = Realm.getDefaultInstance();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Example of Realm");
    listCategory = new ArrayList<>();
    spCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCategory);

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
  }

  private void setupRecylerView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new MainAdapter(this, bookRealmResults, this);
    recyclerView.setAdapter(adapter);
  }

  private void setupListener() {
    realmChangeCategoryListener = new RealmChangeListener() {
      @Override public void onChange() {
        Timber.d("onChange(): category change");
        listCategory.clear();
        for (Category data : categoryRealmResults) {
          Timber.d("onChange(): " + data.getName() + " - " + data.getCreatedAt().toString());
          listCategory.add(data.getName());
        }
        spCategoryAdapter.notifyDataSetChanged();
      }
    };

    realmChangeBookListener = new RealmChangeListener() {
      @Override public void onChange() {
        bookRealmResults.sort("createdAt", Sort.DESCENDING);
        adapter.notifyDataSetChanged();
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
        closeFab();
      }
    });
  }

  private void writeNewBook(final String bookName, final String authorName,
      final Category selectedCategory) {
    Toast.makeText(MainActivity.this, bookName, Toast.LENGTH_SHORT).show();
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        Book book = new Book(bookName, authorName, new Date(), selectedCategory);
        realm.copyToRealmOrUpdate(book);
        closeFab();
      }
    });
  }

  private void showAddBookDialog() {
    MaterialDialog dialog = new MaterialDialog.Builder(this).title("Add New Book")
        .customView(R.layout.custom_dialog, true)
        .positiveText("Ok")
        .negativeText("Cancel")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            writeNewBook(bookName, authorName, selectedCategory);
          }
        })
        .show();

    spCategory = (Spinner) dialog.getCustomView().findViewById(R.id.sp_category);
    etBookName = (EditText) dialog.getCustomView().findViewById(R.id.input_name);
    etAuthorName = (EditText) dialog.getCustomView().findViewById(R.id.input_author);
    spCategory.setAdapter(spCategoryAdapter);
    spCategory.setOnItemSelectedListener(this);
    etAuthorName.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        authorName = charSequence.toString();
      }

      @Override public void afterTextChanged(Editable editable) {

      }
    });

    etBookName.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        bookName = charSequence.toString();
      }

      @Override public void afterTextChanged(Editable editable) {

      }
    });
  }

  @Override protected void onStart() {
    super.onStart();
    categoryRealmResults = realm.where(Category.class).findAllAsync();
    bookRealmResults = realm.where(Book.class).findAllAsync();
    bookRealmResults.addChangeListener(realmChangeBookListener);
    categoryRealmResults.addChangeListener(realmChangeCategoryListener);

    setupRecylerView();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    bookRealmResults.removeChangeListener(realmChangeBookListener);
    categoryRealmResults.removeChangeListener(realmChangeCategoryListener);
    realm.close();
  }

  void closeFab() {
    fabMenu.close(true);
  }

  @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    selectedCategory = categoryRealmResults.get(i);
  }

  @Override public void onNothingSelected(AdapterView<?> adapterView) {

  }

  @Override public void onItemClick(Book book) {
    Toast.makeText(MainActivity.this, book.getName(), Toast.LENGTH_SHORT).show();
  }
}
