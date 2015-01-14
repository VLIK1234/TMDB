package com.example.vlik1234.tmdb.processing;

public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;

}
