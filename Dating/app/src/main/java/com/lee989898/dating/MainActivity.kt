package com.lee989898.dating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lee989898.dating.slider.CardStackAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object: CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                TODO("Not yet implemented")
            }

            override fun onCardSwiped(direction: Direction?) {
                TODO("Not yet implemented")
            }

            override fun onCardRewound() {
                TODO("Not yet implemented")
            }

            override fun onCardCanceled() {
                TODO("Not yet implemented")
            }

            override fun onCardAppeared(view: View?, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                TODO("Not yet implemented")
            }


        })

         val testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")

        cardStackAdapter = CardStackAdapter(baseContext, testList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter



    }
}