package com.example.sayedsalah.book_listing_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sayed Salah on 10/26/2017.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Bookviewholder> {
    private List<Book> mBooks;
    private final OnItemClickListener mListener;


    public BookAdapter(List<Book> mBooks, OnItemClickListener mListener) {

        this.mBooks = mBooks;
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    @Override
    public Bookviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new Bookviewholder(v);
    }

    @Override
    public void onBindViewHolder(Bookviewholder holder, int position) {
        Book book = mBooks.get(position);
        holder.mBookTitle.setText(book.getBookTitle());
        holder.mBookAuthors.setText(book.getauthors());
        holder.mBookDescription.setText(book.getBookDescription());
        holder.bind(mBooks.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }


    public class Bookviewholder extends RecyclerView.ViewHolder {

        private TextView mBookTitle, mBookAuthors, mBookDescription;

        public Bookviewholder(View itemView) {
            super(itemView);
            mBookTitle = (TextView) itemView.findViewById(R.id.book_title);
            mBookAuthors = (TextView) itemView.findViewById(R.id.book_authors);
            mBookDescription = (TextView) itemView.findViewById(R.id.book_description);

        }

        public void bind(final Book book, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }

    }


}
