package app.api;

import java.util.List;

public class Movie {

    public class Results {
        public Boolean adult;
        public String backdrop_path;
        public List<Integer> genre_ids;
        public Integer id;
        public String overview;
        public String poster_path;
        public String title;
        public Float vote_average;
        public Integer vote_count;
    }

    public String page;
    public List<Results> results;

}

