package com.example.sayedsalah.book_listing_app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private RecyclerView booklistview;
    BookAdapter adapter;
    private TextView info;
    private Button searchbtn;
    private EditText mSearchEditText;
    String searchInput;
    ArrayList<Book> book=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booklistview = (RecyclerView) findViewById(R.id.recycler_view);
        mSearchEditText = (EditText) findViewById(R.id.edit_text_search);
        searchbtn = (Button) findViewById(R.id.btn_search);
        book=new ArrayList<Book>();
        info = (TextView) findViewById(R.id.text_view_information);

        searchbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    searchInput = mSearchEditText.getText().toString();
                    if (searchInput.length() == 0) {
                        Toast.makeText(MainActivity.this, R.string.enter_search_keyword, Toast.LENGTH_SHORT).show();

                    } else {
                        booklistview.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        booklistview.setLayoutManager(llm);
                        new BookAsyncTask(BOOK_REQUEST_URL + searchInput).execute();
                    }
                } else {
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    info.setText(R.string.no_internet_connection);

                }


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        int len = book.size();
        for (int i = 0; i < len; i++) {
            titles.add(book.get(i).getBookTitle());
            authors.add(book.get(i).getauthors());
            description.add(book.get(i).getBookDescription());
        }
        outState.putStringArrayList("title", titles);
        outState.putStringArrayList("author", authors);
        outState.putStringArrayList("description",description);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String[] authorarray = new String[]{};
        ArrayList<String> titles = savedInstanceState.getStringArrayList("title");
        ArrayList<String> authors = savedInstanceState.getStringArrayList("author");
        authorarray = authors.toArray(new String[authors.size()]);

        ArrayList<String> descrption = savedInstanceState.getStringArrayList("description");
        int len = titles.size();
        book.clear();
        for (int i = 0; i < len; i++)
            book.add(new Book(titles.get(i), authorarray,descrption.get(i)));
        booklistview.setVisibility(View.VISIBLE);
        booklistview.setAdapter(new BookAdapter( book, new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {

            }
        }));


    }

    private class BookAsyncTask extends AsyncTask<String, Void, ArrayList<Book>> {


        String s = "";

        public BookAsyncTask(String usgsRequestUrl) {
            s = usgsRequestUrl;
        }

        @Override
        protected ArrayList<Book> doInBackground(String... strings) {

            book = QueryUtils.fetchbookdata(s);
            return book;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            adapter = new BookAdapter(books, new BookAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Book book) {

                }
            });
            booklistview.setAdapter(adapter);

        }
    }
}
