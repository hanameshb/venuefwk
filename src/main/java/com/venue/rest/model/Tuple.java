package com.venue.rest.model;

/**
 * very generic key/value, can be used for any dto needs
 */
public class Tuple {
    private String name;
    private long id;

    /**
     * Default constructor, explicitly needed for json
     */
    public Tuple() {}

    public Tuple (String name, long id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

}
