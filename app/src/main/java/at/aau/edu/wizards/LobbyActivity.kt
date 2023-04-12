package at.aau.edu.wizards

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

lateinit var roomsListView: ListView
var roomsStringList = ArrayList<String>()
lateinit var roomsListAdapter: ArrayAdapter<String>

var roomNumber = 1

class LobbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lobby_layout)


        // Setup listView and its adapter
        roomsListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            roomsStringList
        )

        roomsListView = findViewById(R.id.roomsListView)
        roomsListView.adapter = roomsListAdapter

        // roomsStringList.add("Room 1")

        // Attach button onClick listener to the gotoLobbyBtn.
        findViewById<Button>(R.id.createNewRoomBtn)?.let { createNewRoomBtn ->
            createNewRoomBtn.setOnClickListener {
                // Create new room
                Log.d("GAME_LOBBY", "Adding new room!")
                roomsStringList.add("New room " + roomNumber++)
                roomsListAdapter.notifyDataSetChanged()
            }
        }
    }
}