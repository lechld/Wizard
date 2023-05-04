package at.aau.edu.wizards.ui.gameboard.recycler

import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.ItemGameboardHeaderBinding
import at.aau.edu.wizards.ui.gameboard.GameBoardHeader
import at.aau.edu.wizards.ui.gameboard.GameBoardTheme

class GameBoardHeaderViewHolder(private val binding: ItemGameboardHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GameBoardHeader) {
        binding.headerUsername.text = item.name
        binding.headerScore.text = item.score.toString()
        binding.headerGuessAndWins.text = buildString {
            append(item.wins.toString())
            append(" / ")
            append(item.guess.toString())
        }
        when (item.theme) {
            GameBoardTheme.Red -> {
                binding.headerTxtTop.setImageResource(R.drawable.rtxt)
                binding.headerTxtEnd.setImageResource(R.drawable.rtxt)
                binding.headerTxtStart.setImageResource(R.drawable.rtxt)
                binding.headerCircle.setImageResource(R.drawable.rhc)
            }
            GameBoardTheme.No -> {
                binding.headerTxtTop.setImageResource(R.drawable.ntxt)
                binding.headerTxtEnd.setImageResource(R.drawable.ntxt)
                binding.headerTxtStart.setImageResource(R.drawable.ntxt)
                binding.headerCircle.setImageResource(R.drawable.nhc)
            }
            GameBoardTheme.Green -> {
                binding.headerTxtTop.setImageResource(R.drawable.gtxt)
                binding.headerTxtEnd.setImageResource(R.drawable.gtxt)
                binding.headerTxtStart.setImageResource(R.drawable.gtxt)
                binding.headerCircle.setImageResource(R.drawable.ghc)
            }
            GameBoardTheme.Orange -> {
                binding.headerTxtTop.setImageResource(R.drawable.otxt)
                binding.headerTxtEnd.setImageResource(R.drawable.otxt)
                binding.headerTxtStart.setImageResource(R.drawable.otxt)
                binding.headerCircle.setImageResource(R.drawable.ohc)
            }
            GameBoardTheme.Blue -> {
                binding.headerTxtTop.setImageResource(R.drawable.btxt)
                binding.headerTxtEnd.setImageResource(R.drawable.btxt)
                binding.headerTxtStart.setImageResource(R.drawable.btxt)
                binding.headerCircle.setImageResource(R.drawable.bhc)
            }
        }
        binding.headerIcon.setImageResource(
            when (item.icon) {
                1 -> R.drawable.icon1
                2 -> R.drawable.icon2
                3 -> R.drawable.icon3
                4 -> R.drawable.icon4
                5 -> R.drawable.icon5
                6 -> R.drawable.icon6
                7 -> R.drawable.icon7
                8 -> R.drawable.icon8
                9 -> R.drawable.icon9
                10 -> R.drawable.icon10
                11 -> R.drawable.icon11
                12 -> R.drawable.icon12
                13 -> R.drawable.icon13
                14 -> R.drawable.icon14
                15 -> R.drawable.icon15
                16 -> R.drawable.icon16
                17 -> R.drawable.icon17
                18 -> R.drawable.icon18
                else -> R.drawable.icon19
            }
        )
    }
}