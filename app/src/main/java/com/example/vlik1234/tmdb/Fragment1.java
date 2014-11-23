package com.example.vlik1234.tmdb;
        import android.app.Activity;
        import android.app.Fragment;
        import android.app.ListFragment;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;

public class Fragment1 extends ListFragment {
    String data[] = new String[] { "Add New Movie", "Movies", "TV Shows", "People", "Featured List"};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }




}
