package app.movies;

/**
 * AddedMovie class will be instanciated for movies that were created locally
 */
public class AddedMovie {
    private String overview;
    private String title;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
