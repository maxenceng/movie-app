package app.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment called when the user clicks on 'Search' in the navigation drawer
 */
public class SearchFragment extends Fragment {
    @BindView(R.id.button_search) Button button;
    @BindView(R.id.search_text) TextInputEditText searchText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    // Called when the user put a value in the search input, then clicks on the 'Search' button
    // Inputs arguments from the search input and calls the imageList fragment
    @OnClick(R.id.button_search) void buttonClicked() {
        String search = searchText.getText().toString();
        Fragment imageListFragment = new ImageListFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Prepares arguments for the imageList fragment
        Bundle args = new Bundle();
        args.putString("search", search);
        imageListFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, imageListFragment).commit();
    }
}
