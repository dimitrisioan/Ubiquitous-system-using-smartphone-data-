package com.univ.ubitrack;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class StatisticsFragment extends Fragment  {
    private Spinner sp_chart;
    private String CHART_SPINNER_DATA = "";

    DataTableFragment dataFragment;
    FirstChart chart1Fragment;
    SecondChart chart2Fragment;
    private Fragment fragment;
    SwipeRefreshLayout refreshLayout;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFragment = new DataTableFragment();
        chart1Fragment = new FirstChart();
        chart2Fragment = new SecondChart();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        Spinner sp_chart = view.findViewById(R.id.sp_chart);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
         {
                @Override
                public void onRefresh()
                {
                    assert getFragmentManager() != null;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    if (Build.VERSION.SDK_INT >= 26) {
                        ft.setReorderingAllowed(false);
                    }
                    ft.detach(fragment).attach(fragment).commit();

                    

//                    Fragment frag = new StatisticsFragment();
//                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.parent_fragment_container, frag).commit();
                    refreshLayout.setRefreshing(false);

                }

            });



        String[] chartItems = getResources().getStringArray(R.array.chart_items);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, chartItems);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_chart.setAdapter(adapter3);
        sp_chart.setSelection(0);
        sp_chart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CHART_SPINNER_DATA = adapterView.getItemAtPosition(i).toString();
                ((TextView) view).setTextColor(Color.BLACK);
//                Log.i("Chart", CHART_SPINNER_DATA);
                if (i == 0) {
                        setFragment(dataFragment);
                }
                if (i == 1) {
                        setFragment(chart1Fragment);
                }
                if (i == 2) {
                      setFragment(chart2Fragment);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public void setFragment(Fragment fragment){
        this.fragment = fragment;
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parent_fragment_container,fragment);
        fragmentTransaction.commit();
    }
}
