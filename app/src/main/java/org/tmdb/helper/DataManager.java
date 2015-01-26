package org.tmdb.helper;

import android.os.Handler;

import org.tmdb.os.AsyncTask;
import org.tmdb.processing.Processor;
import org.tmdb.source.DataSource;

public class DataManager {

    public static final boolean IS_ASYNC_TASK = true;

    public static interface Callback<Result> {
        void onDataLoadStart();

        void onDone(Result data);

        void onError(Exception e);
    }

    public static interface Loader<ProcessingResult, DataSourceResult, Params> {

        void load(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor);

    }

    public static <ProcessingResult, DataSourceResult, Params> void
    loadData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor
    ) {
        loadData(callback, params, dataSource, processor, new Loader<ProcessingResult, DataSourceResult, Params>() {
            @Override
            public void load(Callback<ProcessingResult> callback, Params params, DataSource<DataSourceResult, Params> dataSource, Processor<ProcessingResult, DataSourceResult> processor) {
                if (IS_ASYNC_TASK) {
                    executeInAsyncTask(callback, params, dataSource, processor);
                } else {
                    executeInThread(callback, params, dataSource, processor);
                }
            }
        });
    }

    public static <ProcessingResult, DataSourceResult, Params> void
    loadData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor,
            final Loader<ProcessingResult, DataSourceResult, Params> mySuperLoader) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        mySuperLoader.load(callback, params, dataSource, processor);
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInAsyncTask(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        new AsyncTask<Params, Void, ProcessingResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onDataLoadStart();
            }

            @Override
            protected void onPostExecute(ProcessingResult processingResult) {
                super.onPostExecute(processingResult);
                callback.onDone(processingResult);
            }

            @SafeVarargs
            @Override
            protected final ProcessingResult doInBackground(Params... params) throws Exception {
                DataSourceResult dataSourceResult = dataSource.getResult(params[0]);
                return processor.process(dataSourceResult);
            }

            @Override
            protected void onPostException(Exception e) {
                callback.onError(e);
            }

        }.execute(params);
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInThread(final Callback<ProcessingResult> callback, final Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        final Handler handler = new Handler();
        callback.onDataLoadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final DataSourceResult result = dataSource.getResult(params);
                    final ProcessingResult processingResult = processor.process(result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }

}
