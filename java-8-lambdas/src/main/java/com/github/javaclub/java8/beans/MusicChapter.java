package com.github.javaclub.java8.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard Warburton
 */
public abstract class MusicChapter {
    
    protected List<Artist> artists;
    protected List<Album> albums;

    public MusicChapter() {
        artists = new ArrayList<>();
        albums = new ArrayList<>();
        loadData("");
    }

    private void loadData(String file) {
    	artists = SampleData.getSomeArtists();
    }
    
}
