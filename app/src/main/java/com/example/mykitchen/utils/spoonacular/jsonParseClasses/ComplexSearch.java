package com.example.mykitchen.utils.spoonacular.jsonParseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplexSearch {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("totalResults")
    @Expose
    private int totalResults;


    public ComplexSearch() {
    }

    public ComplexSearch(List<Result> results, int offset, int number, int totalResults) {
        super();
        this.results = results;
        this.offset = offset;
        this.number = number;
        this.totalResults = totalResults;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
