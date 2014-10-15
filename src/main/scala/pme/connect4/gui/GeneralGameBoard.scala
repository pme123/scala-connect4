package pme.connect4.gui

import pme.connect4.domain.GameConfig._
import pme.connect4.domain.{RedChip, Chip, ConnectFourGame}
import pme.connect4.gui.ChipView._
import pme.connect4.gui.GuiGameConfig2D._

import scalafx.animation.TranslateTransition
import scalafx.util.Duration

trait GeneralGameBoard[TC <: GeneralChipView,TS <: GeneralSpotView] {
  val gameStartedSubject = new GameStartedSubject
  val gameWinnerSubject = new GameWinnerSubject

  var fourConnect = new ConnectFourGame
  var chipsToPlay: Seq[TC] = Nil
  var gameSpots: Seq[TS] = Nil
  var activeChip: Chip = RedChip
  var playAloneMode: Boolean = false
  var myChip: Chip = activeChip.other

  def startNewGame() = {
    gameStartedSubject.gameStarted = false
    chipsToPlay = initChipsToPlay
    gameSpots = initGameSpots
  }

 private def initChipsToPlay: Seq[TC] = {
    for {
      col <- 0 until cols
    } yield {
      createChip(col)
    }
  }
  protected def createChip(col: Int): TC
  protected def initGameSpots: Seq[TS]

  protected def handleChipSelected(col: Int, chip: GeneralChipView): TC = {
    {
      fourConnect.dropChip(col, activeChip)
      if (!fourConnect.hasEmptySlot(col)) chip.setVisible(false)
      dropChipView(col)
    }
  }

  def dropChipView(col: Int): TC = {
    val newChip = createChip(col)
    val dropHeight = rows - fourConnect.findFirstTakenSpot(col).get.row
    val transition = new TranslateTransition {
      duration = Duration(1000)
      node = newChip
      byY = dropHeight * fieldHeight
    }
    transition.play()
//    verifyTurn()
    switchPlayer()
//    runNextTurn()
    newChip
  }

  def switchPlayer(): Unit = {
    activeChip = activeChip.other
    if (chipsToPlay != null) for (chip <- chipsToPlay) changeMaterial(chip)
  }

  protected def changeMaterial(chip: TC)
}
