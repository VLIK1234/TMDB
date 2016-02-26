package github.tmdb.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import by.istin.android.xcore.fragment.XFragment;
import by.istin.android.xcore.provider.ModelContract;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.model.Person;
import github.tmdb.database.processor.PersonProcessor;

/**
 * @author IvanBakach
 * @version on 26.02.2016
 */
public class PersonFragment extends XFragment {

    public static final String PERSON_ID = "person_id";
    private long mPersonId;

    public static Fragment newInstance(long personId) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putLong(PERSON_ID, personId);
        fragment.setArguments(args);
        return fragment;
    }

    public long getPersonId() {
        Bundle extra = getArguments();
        if (extra != null) {
            return extra.getLong(PERSON_ID);
        } else {
            return 0L;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPersonId = getPersonId();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getUri(Person.class);
    }

    @Override
    public String getUrl() {
        return String.format("https://api.themoviedb.org/3/person/%1$s?api_key=f413bc4bacac8dff174a909f8ef535ae", mPersonId);
    }

    @Override
    public String getProcessorKey() {
        return PersonProcessor.SYSTEM_SERVICE_KEY;
    }

    @Override
    protected void onLoadFinished(Cursor cursor) {
       ApiTMDB.getImagePath(ApiTMDB.POSTER_342X513_BACKDROP_342X192, Person.PROFILE_PATH);
    }

    @Override
    protected String[] getAdapterColumns() {
        return new String[]{Person.NAME, Person.BIRTHDAY, Person.PLACE_OF_BIRTH, Person.BIOGRAPHY};
    }

    @Override
    protected int[] getAdapterControlIds() {
        return new int[]{R.id.name, R.id.birthday, R.id.place_of_birth, R.id.biography};
    }

    @Override
    protected void onLoaderReset() {

    }
}
