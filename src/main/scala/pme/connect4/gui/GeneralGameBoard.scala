package pme.connect4.gui

import pme.connect4.domain.{RedChip, Chip, ConnectFourGame}

trait GeneralGameBoard {
  val gameStartedSubject = new GameStartedSubject
  val gameWinnerSubject = new GameWinnerSubject

  var fourConnect = new ConnectFourGame
  var chipsToPlay: Seq[ChipView] = null
  var gameSpots: Seq[SpotView] = null
  var activeChip: Chip = RedChip
  var playAloneMode: Boolean = false
  var myChip: Chip = activeChip.other

  def startNewGame() = {
    gameStartedSubject.gameStarted = false
  }
}
