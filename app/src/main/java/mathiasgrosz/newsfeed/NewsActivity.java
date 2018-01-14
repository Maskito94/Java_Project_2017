package mathiasgrosz.newsfeed;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import java.io.InputStream;

public class NewsActivity extends AppCompatActivity {

// AFAIRE justifier le texte


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

//Recuperation des donnees
        Intent in = getIntent();
        final String title = in.getExtras().getString("title");
        final String description = in.getExtras().getString("description");
        final String url = in.getExtras().getString("url");
        final String url_img = in.getExtras().getString("img");

// Affichage de l'article
        TextView titleView = (TextView) findViewById(R.id.titre);
        titleView.setText(title);
        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(description);


//Affichage de l'image
        new DownloadImageTask((ImageView) findViewById(R.id.image))
                .execute(url_img);
        ImageView img = (ImageView) findViewById(R.id.image);


//Bouton pour l'article
        Button lienArticle = (Button)findViewById(R.id.button);
        //ouvrir le navigateur
        lienArticle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }


    //fonction pour recuperer et traiter l'image a partir de l url ___________________________________ A RETESTER
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url_img = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(url_img).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
