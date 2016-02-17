package com.pratamawijaya.examplerealm.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pratamawijaya.examplerealm.R;
import com.pratamawijaya.examplerealm.model.Book;
import io.realm.RealmResults;

/**
 * Created by : pratama - set.mnemonix@gmail.com
 * Date : 2/17/16
 * Project : ExampleRealm
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

  public interface MainAdapterListener {
    void onItemClick(Book book);
  }

  private Context context;
  private RealmResults<Book> realmResultsBook;
  private MainAdapterListener listener;

  public MainAdapter(Context context, RealmResults<Book> realmResultsBook,
      MainAdapterListener listener) {
    this.context = context;
    this.realmResultsBook = realmResultsBook;
    this.listener = listener;
  }

  @Override public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MainHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false));
  }

  @Override public void onBindViewHolder(MainHolder holder, final int position) {
    holder.title.setText(realmResultsBook.get(position).getName());
    holder.categoryAuthor.setText("Category : " +
        realmResultsBook.get(position).getCategory().getName() + " | " + realmResultsBook.get(
        position).getAuthor());
    holder.date.setText("Published : " + java.text.DateFormat.getDateInstance()
        .format(realmResultsBook.get(position).getCreatedAt()));

    holder.container.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        listener.onItemClick(realmResultsBook.get(position));
      }
    });
  }

  @Override public int getItemCount() {
    return realmResultsBook.size();
  }

  public class MainHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.container_card) CardView container;
    @Bind(R.id.txt_title) TextView title;
    @Bind(R.id.txt_category_author) TextView categoryAuthor;
    @Bind(R.id.txt_date) TextView date;

    public MainHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
