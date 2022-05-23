package com.example.homeanimationpoc

import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    val list: RecyclerView by lazy { findViewById(R.id.listHome) }
    val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    val card1: CardView by lazy { findViewById(R.id.firstCard) }
    val card2: CardView by lazy { findViewById(R.id.secondCard) }

    private val alphaTransformAnimation by lazy { AlphaTransformAnimation().buildTransformation() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        card1.setOnClickListener {
            alphaTransformAnimation.downTransform(fab)
        }

        card2.setOnClickListener {
            alphaTransformAnimation.upTransform(fab)
        }

        val adapter = RecycleViewAnimation()
        list.layoutManager = VegaLayoutManager()
        val itemDecor = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = true.also { _ ->
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            })
        itemDecor.attachToRecyclerView(list)
        list.adapter = adapter
    }
}