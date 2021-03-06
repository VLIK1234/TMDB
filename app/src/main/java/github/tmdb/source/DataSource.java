package github.tmdb.source;

public interface DataSource<Result, Params> {

    Result getResult(Params params) throws Exception;

}
