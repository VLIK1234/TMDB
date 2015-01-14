package com.example.vlik1234.tmdb.source;

public interface DataSource<Result,Params>{

    Result getResult(Params params) throws Exception;

}
