package github.tmdb.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.fragment.XFragment;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.cursor.PersonCursor;
import github.tmdb.database.model.Person;
import github.tmdb.database.processor.PersonProcessor;
import github.tmdb.utils.BitmapDisplayOptions;

/**
 * @author IvanBakach
 * @version on 26.02.2016
 */
public class PersonFragment extends XFragment<PersonCursor> {

    public static final String PERSON_ID = "person_id";
    private long mPersonId;
    private ImageView mProfilePath;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPersonId = getPersonId();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View view = getView();
        if (view != null) {
            mProfilePath = (ImageView) getView().findViewById(R.id.profile);
        }
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getSQLQueryUri("SELECT p.* FROM "+ DBHelper.getTableName(Person.class) +" p WHERE p."
                + Person.ID + " == " + mPersonId, ModelContract.getUri(Person.class));
    }

    @Override
    public String getUrl() {
        return String.format("https://api.themoviedb.org/3/person/%1$s?api_key=" + ApiTMDB.API_KEY, mPersonId);
    }

    @Override
    public String getProcessorKey() {
        return PersonProcessor.SYSTEM_SERVICE_KEY;
    }

    @Override
    protected void onLoadFinished(Cursor cursor) {
        if (cursor.getCount() > 0) {
            ImageLoader.getInstance().displayImage(ApiTMDB.getImagePath(ApiTMDB.POSTER_342X513_BACKDROP_342X192,
                    CursorUtils.getString(Person.PROFILE_PATH, cursor)), mProfilePath,
                    BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
        }
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
