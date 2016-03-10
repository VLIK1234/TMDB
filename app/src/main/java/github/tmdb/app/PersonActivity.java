package github.tmdb.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import github.tmdb.R;
import github.tmdb.fragment.PersonFragment;

/**
 * @author IvanBakach
 * @version on 10.03.2016
 */
public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            long personId = extra.getLong(PersonFragment.PERSON_ID);
            setCurrentFragment(PersonFragment.newInstance(personId), false);
        }
    }



    public void setCurrentFragment(Fragment fragment, boolean withBackStack) {
        String fragmentName = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment, fragmentName);
        if (withBackStack) {
            getSupportFragmentManager().popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack(fragmentName);
        }
        fragmentTransaction.commit();
    }
}
