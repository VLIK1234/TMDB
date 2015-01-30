package github.tmdb.processing;

public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;

}
