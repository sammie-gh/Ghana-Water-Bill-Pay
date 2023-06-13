package com.gh.sammie.ghanawater.Interface;


import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sammie on 2/19/2018.
 */

public interface RecyclerToucheHelperListener
{
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
