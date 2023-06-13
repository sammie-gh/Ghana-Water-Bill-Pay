package com.gh.sammie.ghanawater.Helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.ghanawater.Interface.RecyclerToucheHelperListener;
import com.gh.sammie.ghanawater.ViewHolder.CartViewHolder;

/**
 * Created by Sammie on 2/19/2018.
 */

public class RecyclerItemTouchhelper extends ItemTouchHelper.SimpleCallback {


    private RecyclerToucheHelperListener recyclerToucheHelperListener;

    public RecyclerItemTouchhelper(int dragDirs, int swipeDirs, RecyclerToucheHelperListener recyclerToucheHelperListener) {
        super(dragDirs, swipeDirs);
        this.recyclerToucheHelperListener = recyclerToucheHelperListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (recyclerToucheHelperListener != null )
            recyclerToucheHelperListener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof CartViewHolder){


        View foregroundView = ((CartViewHolder)viewHolder).view_forebackground;
        getDefaultUIUtil().clearView(foregroundView);
        }


    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (viewHolder instanceof  CartViewHolder)
        {
        View foregroundView = ((CartViewHolder)viewHolder).view_forebackground;
        getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
        }

        }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder != null)
        {
            if (viewHolder instanceof CartViewHolder)
            {
                View foregroundView = ((CartViewHolder)viewHolder).view_forebackground;
                getDefaultUIUtil().onSelected(foregroundView);
            }


        }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

if (viewHolder instanceof CartViewHolder)
{
    View foregroundView = ((CartViewHolder)viewHolder).view_forebackground;
    getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
}



    }
}
