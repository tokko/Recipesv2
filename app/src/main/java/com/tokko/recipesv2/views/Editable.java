package com.tokko.recipesv2.views;

public interface Editable<D> {
    void edit();

    void discard();

    void accept();

    D getData();

    void setData(D data);
}
