package com.prototype.mistplaychallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements Filterable {
    List<Game> allGames;                //a list containing all games
    List<Game> displayedGame;           //a list of games already displayed on screen
    List<Game> filteredGames;           //a list of filtered games based on search
    private LayoutInflater mLayout;     //my custom layout for displaying a game
    private int startIndex;             //first index of a list

    public CustomAdapter(Context context, List<Game> gameList) {
        mLayout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.allGames = gameList;
        sortGameByRating(allGames);
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

    //sort by descending order
    private void sortGameByRating(List<Game> gameList) {
        Collections.sort(gameList, new Comparator<Game>() {
            @Override
            public int compare(Game o1, Game o2) {
                return Double.valueOf(o2.getRating()).compareTo(Double.valueOf(o1.getRating()));
            }
        });
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
        //setup the views
        View v = mLayout.inflate(R.layout.custom_layout, parent, false);
        TextView title = v.findViewById(R.id.titleTextView);
        TextView genre = v.findViewById(R.id.genreTextView);
        RatingBar rating = v.findViewById(R.id.gameRatingBar);
        ImageView image = v.findViewById(R.id.gameImageView);
        Game game = displayedGame.get(position);
        title.setText(game.getTitle());
        genre.setText(game.getGenre() + ": " + game.getSubGenre());
        rating.setRating((float) game.getRating());

        //use picasso for downloading images and caching for efficient image loading
        Picasso.get().load(game.getImgURL()).into(image);

        //slow image loading (memory inefficient)
//        new DownloadImageTask((ImageView) v.findViewById(R.id.gameImageView)).execute(game.getImgURL());

        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString();
                FilterResults results = new FilterResults();

                //if there is something to search
                if (constraint != null && constraint.toString().length() > 0) {
                    ArrayList<Game> filteredItem = new ArrayList<Game>();
                    //add the game that match the constraints
                    for (Game game : allGames) {
                        //check if title or subGenre contains the search characters
                        if (game.getTitle().toLowerCase().contains(constraint) || game.getSubGenre().toLowerCase().contains(constraint)) {
                            filteredItem.add(game);
                        }
                    }
                    results.count = filteredItem.size();
                    results.values = filteredItem;
                } else {
                    //nothing to search, return allGames as result
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
                    //sort the filteredGames list by rating
                    sortGameByRating(filteredGames);

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

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                mIcon11 = BitmapFactory.decodeStream(in, null, options);
//                in.close();
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

}
