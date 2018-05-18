package advprog.example.bot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class FakeNews implements Comparable<FakeNews> {
    private String url;
    private Set<String> types;

    public FakeNews(String url, Set<String> types) {
        this.url = url;
        this.types = types;
    }

    public String getUrl() {
        return this.url;
    }

    public Set<String> getTypes() {
        return this.types;
    }

    public boolean isSatire() {
        return this.types.contains("satire");
    }

    public boolean isFake() {
        return this.types.contains("fake");
    }

    public boolean isConspiracy() {
        return this.types.contains("conspiracy");
    }
	
	public String getAllTypes(){
		return this.types.toString().replace("[","").replace("]","");
	}

    @Override
    public int compareTo(FakeNews other) {
        return this.url.compareTo(other.getUrl());
    }
}