package app.api;

import java.util.List;

public class Movie {

    public class Results {
        private Boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private Integer id;
        private String overview;
        private String poster_path;
        private String title;
        private Float vote_average;
        private Integer vote_count;

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Float getVote_average() {
            return vote_average;
        }

        public void setVote_average(Float vote_average) {
            this.vote_average = vote_average;
        }

        public Integer getVote_count() {
            return vote_count;
        }

        public void setVote_count(Integer vote_count) {
            this.vote_count = vote_count;
        }
    }

    private String page;
    public List<Results> results;

}

