package cz.cvut.fel.pda.buggerlist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TabFragmentBuggerList extends Fragment {
    private static final String TAG = "R.string.tab_buggerlist";

    static final String[] items = {"task 1","task 2","task 3","task 4"};

    public TabFragmentBuggerList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_buggerList));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_buggerList));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.tab_buggerList));
        View view = inflater.inflate(R.layout.tab_fragment_buggerlist, container,false);

        final ListView myLV = (ListView) view.findViewById(R.id.bugger_list_view);
        myLV.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));
        myLV.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long l) {
                CharSequence sprava = " " + items[position] + " has been done";
                Toast.makeText(getContext() , sprava, Toast.LENGTH_SHORT).show();
                myLV.getChildAt(position).setBackgroundColor(Color.parseColor("#689F38"));
            }
        });

        return view;
    }
}
