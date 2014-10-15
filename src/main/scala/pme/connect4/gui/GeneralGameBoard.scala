package pme.connect4.gui

import pme.connect4.domain.GameConfig._
import pme.connect4.domain.{RedChip, Chip, ConnectFourGame}

trait GeneralGameBoard[T1 <: GeneralChipView,T2 <: GeneralSpotView] {
  val gameStartedSubject = new GameStartedSubject
  val gameWinnerSubject = new GameWinnerSubject

  var fourConnect = new ConnectFourGame
  var chipsToPlay: Seq[T1] = Nil
  var gameSpots: Seq[T2] = Nil
  var activeChip: Chip = RedChip
  var playAloneMode: Boolean = false
  var myChip: Chip = activeChip.other

  def startNewGame() = {
    gameStartedSubject.gameStarted = false
    chipsToPlay = initChipsToPlay
    gameSpots = initGameSpots
  }

 private def initChipsToPlay: Seq[T1] = {
    for {
      col <- 0 until cols
    } yield {
      createChip(col)
    }
  }
  protected def createChip(col: Int): T1
  protected def initGameSpots: Seq[T2]
}
