package com.prototype.mistplaychallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements Filterable {
    List<Game> allGames;
    List<Game> displayedGame;
    List<Game> filteredGames;
    private LayoutInflater mLayout;
    private int startIndex;

    public CustomAdapter(Context context, List<Game> gameList) {
        mLayout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.allGames = gameList;
        this.filteredGames = allGames;
        this.displayedGame = new ArrayList<Game>();
        startIndex = 0;
        addGames(startIndex);
    }

    //when needed, add 10 more games to the list of displayedGame
    public void addGames(int index) {
        int gameIndexRange = index + 10;
        //check if gameIndexRange out of bound
        if (gameIndexRange > filteredGames.size()) {
            gameIndexRange = filteredGames.size();
        }
        for (int i = index; i < gameIndexRange; i++) {
            displayedGame.add(filteredGames.get(i));
        }
    }

    @Override
    public int getCount() {
        return displayedGame.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mLayout.inflate(R.layout.custom_layout, parent, false);
        TextView title = v.findViewById(R.id.titleTextView);
        TextView genre = v.findViewById(R.id.genreTextView);
        RatingBar rating = v.findViewById(R.id.gameRatingBar);
        Game game = displayedGame.get(position);
        title.setText(game.getTitle());
        genre.setText(game.getGenre() + ": " + game.getSubGenre());
        rating.setRating((float) game.getRating());
        new DownloadImageTask((ImageView) v.findViewById(R.id.gameImageView)).execute(game.getImgURL());

        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString();
                FilterResults results = new FilterResults();

                //filter the game by title
                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Game> filteredItem = new ArrayList<Game>();
                    //add the game that match the constraints
                    for (Game game : allGames) {
                        if (game.getTitle().toLowerCase().contains(constraint) || game.getSubGenre().toLowerCase().contains(constraint)) {
                            filteredItem.add(game);
                        }
                    }
                    results.count = filteredItem.size();
                    results.values = filteredItem;
                } else {
                    synchronized (this) {
                        ArrayList<Game> list = new ArrayList<Game>(allGames);
                        results.values = list;
                        results.count = list.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredGames = (ArrayList<Game>) results.values;
                if (results.count > 0) {
                    //clear the displayedGame list for the new result
                    displayedGame.clear();
                    //add games to displayedGame list when new result is to be published
                    addGames(startIndex);
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                mIcon11 = BitmapFactory.decodeStream(in, null, options);
                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
