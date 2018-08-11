package com.rygital.moneytracker.ui.account

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.rygital.moneytracker.R

abstract class SwipeToDeleteCallback(context: Context?) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    val icon = context?.getDrawable(R.drawable.ic_delete)
    private val intrinsicWidth = icon?.intrinsicWidth ?: 0
    private val intrinsicHeight = icon?.intrinsicHeight ?: 0

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Calculate position of delete icon
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        // Draw the delete icon
        icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}