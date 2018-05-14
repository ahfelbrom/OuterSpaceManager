package boulet.com.outerspacemanager.outerspacemanager.Model;

import boulet.com.outerspacemanager.outerspacemanager.Model.Search;

/**
 * Created by bouleta on 26/02/2018.
 */
public class Searches {
    private String size;
    private Search[] searches;

    public void setSearches(Search[] searches) {
        this.searches = searches;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Search[] getSearches() {
        return searches;
    }

    public String getSize() {
        return size;
    }
}