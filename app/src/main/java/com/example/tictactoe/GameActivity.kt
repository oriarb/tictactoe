package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {

    private lateinit var gridCells: Array<ImageView>
    private var isPlayerOneTurn = true
    private var boardState = Array(3) { Array(3) {""} }
    private lateinit var playerName: TextView
    private lateinit var playAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playerName = findViewById(R.id.playerName)
        playAgainButton = findViewById(R.id.playAgainButton)
        initGame()
    }

    private fun initGame() {
        playerName.setText("Player 1")
        gridCells = arrayOf(
            findViewById(R.id.cell1),
            findViewById(R.id.cell2),
            findViewById(R.id.cell3),
            findViewById(R.id.cell4),
            findViewById(R.id.cell5),
            findViewById(R.id.cell6),
            findViewById(R.id.cell7),
            findViewById(R.id.cell8),
            findViewById(R.id.cell9),
        )

        for (i in gridCells.indices) {
            gridCells[i].setOnClickListener {
                onCellClicked(i)
            }
        }

        playAgainButton.setOnClickListener {
            resetGame()
            playAgainButton.visibility = View.GONE
        }

    }

    private fun onCellClicked(index: Int) {
        val row = index / 3
        val col = index % 3

        // Check if the cell is already occupied
        if (boardState[row][col].isNotEmpty()) {
            Toast.makeText(this, "Cell is already occupied!", Toast.LENGTH_SHORT).show()
            return
        }

        // Set the image and update the board state based on the current player
        val currentPlayerSymbol = if (isPlayerOneTurn) "X" else "O"
        val currentDrawable = if (isPlayerOneTurn) R.drawable.ic_x else R.drawable.ic_o

        gridCells[index].setImageResource(currentDrawable)
        gridCells[index].tag = currentPlayerSymbol
        boardState[row][col] = currentPlayerSymbol

        // Check for a winner
        if (checkWinner(currentPlayerSymbol)) {
            Toast.makeText(this, "Player ${if (isPlayerOneTurn) "1" else "2"} won!", Toast.LENGTH_LONG).show()
            playAgainButton.visibility = View.VISIBLE
            return
        }

        // Check for a draw
        if (boardState.flatten().none { it.isEmpty() }) {
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_LONG).show()
            playAgainButton.visibility = View.VISIBLE
            return
        }

        // Switch turns
        isPlayerOneTurn = !isPlayerOneTurn
        if (isPlayerOneTurn) playerName.setText("Player 1") else playerName.setText("Player 2")
    }

    private fun checkWinner(playerSymbol: String): Boolean {

        for (i in 0..2) {
            // Check rows and columns
            if ((boardState[i][0] == playerSymbol && boardState[i][1] == playerSymbol && boardState[i][2] == playerSymbol) ||
                (boardState[0][i] == playerSymbol && boardState[1][i] == playerSymbol && boardState[2][i] == playerSymbol)
            ) {
                return true
            }
        }

        // Check diagonals
        if ((boardState[0][0] == playerSymbol && boardState[1][1] == playerSymbol && boardState[2][2] == playerSymbol) ||
            (boardState[0][2] == playerSymbol && boardState[1][1] == playerSymbol && boardState[2][0] == playerSymbol)
        ) {
            return true
        }

        return false
    }

    private fun resetGame() {
        // Clear the board and reset the state
        boardState = Array(3) { Array(3) { "" } }
        isPlayerOneTurn = true
        playerName.setText("Player 1")
        gridCells.forEach {
            it.setImageResource(0) // Clear the images
            it.tag = null
        }
    }
}