package com.bonoworksdesign.tafsiribnukatsir;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bonoworksdesign.tafsiribnukatsir.adapter.AdapterBookmark;
import com.bonoworksdesign.tafsiribnukatsir.database.DataBookmark;
import com.bonoworksdesign.tafsiribnukatsir.model.ModelDataBookmark;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    ArrayList<ModelDataBookmark> listBookmark = new ArrayList<>();
    AdapterBookmark adapterBookmark;

    RecyclerView bookmarkRecycler;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout noBookmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        bookmarkRecycler = (RecyclerView) view.findViewById(R.id.bookmark_recyclerview);
        noBookmark = (LinearLayout) view.findViewById(R.id.no_bookmark_layout);

        loadData();

        return view;
    }

    public void loadData() {
        DataBookmark bookmark = new DataBookmark(getActivity());
        Cursor data = bookmark.getData();
        listBookmark.clear();

        while (data.moveToNext()) {
            ModelDataBookmark dataBookmark = new ModelDataBookmark(
                    data.getString(0),
                    data.getInt(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getInt(6)
            );

            listBookmark.add(dataBookmark);
        }

        layoutManager = new GridLayoutManager(getContext(), 2);
        bookmarkRecycler.setLayoutManager(layoutManager);
        adapterBookmark = new AdapterBookmark(listBookmark,getContext(),this);
        bookmarkRecycler.setAdapter(adapterBookmark);

        if (data.getCount() > 0) {
            noBookmark.setVisibility(View.GONE);
            bookmarkRecycler.setVisibility(View.VISIBLE);
        } else {
            noBookmark.setVisibility(View.VISIBLE);
            bookmarkRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }
}