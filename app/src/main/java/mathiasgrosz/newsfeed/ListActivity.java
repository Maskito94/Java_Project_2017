package mathiasgrosz.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {
    ArrayAdapter<String> arrayAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//Recuperation des donnees du SplashScreen
        final String[] titles = getIntent().getStringArrayExtra("titre");
        final String[] descriptions = getIntent().getStringArrayExtra("description");
        final String[] urls = getIntent().getStringArrayExtra("url");
        final String[] images = getIntent().getStringArrayExtra("image");
        final String[] ids = getIntent().getStringArrayExtra("id");
        final String[] namesSources = getIntent().getStringArrayExtra("name");
        final int curseur = getIntent().getIntExtra("numero",0);

// Bouton de rafraichissement des news
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.refresh_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, SplashScreen.class);
                i.putExtra("source", ids[curseur]); // conserver le choix de source
                i.putExtra("numero",curseur); // conserver la place du curseur dans la liste
                i.putExtra("refresh", true); // ______________________________________________________tentative de message different pour refresh dans le splash screen
               startActivity(i);
                finish();
            }
        });

//Menu de choix des sources
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_view, R.id.textViewID, namesSources);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Spinner spinnerItems = (Spinner) findViewById(R.id.spinner);
        spinnerItems.setAdapter(adapter);
        spinnerItems.setSelection(curseur,false);
        spinnerItems.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int curs, long id) {

                        Object item = parent.getItemAtPosition(curs);
                        System.out.println(item.toString());
                        Intent i = new Intent(ListActivity.this, SplashScreen.class);
                        i.putExtra("source", ids[curs]); // conserver le choix de source
                        i.putExtra("numero",curs); // conserver la place du curseur dans la liste
                        startActivity(i);
                        finish();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

//        List<News> news = new ArrayList<News>();
//        for (int i =0; i< titles.length;i++)
//        {
//            news.add(new News(Color.GRAY,titles[i]));
//        }
       // arrayAdapt = new NewsAdapter(ListActivity.this,news);

//Liste des actualites selectionnees
        arrayAdapt = new ArrayAdapter<String>(this,R.layout.list_view, R.id.textViewID, titles);
        final ListView listeDArticles = (ListView) findViewById(R.id.list);
        listeDArticles.setAdapter(arrayAdapt);
        listeDArticles.setTextFilterEnabled(true);

        listeDArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int curseur = parent.getPositionForView(view);
                Intent i = new Intent(ListActivity.this, NewsActivity.class);
                // informations à donner
                i.putExtra("title", titles[curseur]);
                i.putExtra("description", descriptions[curseur]);
                i.putExtra("url", urls[curseur]);
                i.putExtra("image", images[curseur]);
                startActivity(i);
            }
        });
    }

    //______________________________________________________________________Tentative d'images à gauche du titre

//    private class NewsAdapter extends ArrayAdapter<News> {
//
//
//
//        private NewsAdapter(Context context, List<News> News) {
//            super(context, 0, News);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            if(convertView == null){
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent, false);
//            }
//
//            NewsViewHolder viewHolder = (NewsViewHolder) convertView.getTag();
//            if(viewHolder == null){
//                viewHolder = new NewsViewHolder();
//
//                viewHolder.text = (TextView) convertView.findViewById(R.id.textViewID);
//                viewHolder.color = (ImageView) convertView.findViewById(R.id.preview);
//                convertView.setTag(viewHolder);
//            }
//
//            News news = getItem(position);
//
//            viewHolder.text.setText(news.getText());
//            viewHolder.color.setImageDrawable(new ColorDrawable(news.getColor()));
//
//            return convertView;
//        }
//        private class NewsViewHolder{
//            private TextView text;
//            private ImageView color;
//        }
//
//    }
//    private class News {
//        private int preview_color;
//        private String text;
//
//        public News(int color, String text) {
//            this.preview_color = color;
//            this.text = text;
//        }
//        private String getText () {
//        return this.text;
//        }
//
//        private int getColor () {
//            return this.preview_color;
//        }
//    }
}
