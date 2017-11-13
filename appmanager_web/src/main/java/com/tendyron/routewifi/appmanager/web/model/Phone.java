package com.tendyron.routewifi.appmanager.web.model;

/**
 * Created by Neo on 2017/2/23.
 */
public class Phone extends Base {
    private String name;
    private String model;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
