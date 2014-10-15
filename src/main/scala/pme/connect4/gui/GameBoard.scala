package pme.connect4.gui

import pme.connect4.domain.GameConfig._
import pme.connect4.domain.{Chip, Combinations, ConnectFourGame, RedChip}

import scalafx.animation.TranslateTransition
import scalafx.util.Duration

trait GameBoard[TC <: ChipView, TS <: SpotView] {
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

  protected def handleChipSelected(col: Int, chip: ChipView): List[TC] = {
    fourConnect.dropChip(col, activeChip)
    if (!fourConnect.hasEmptySlot(col)) chip.setVisible(false)
    val otherChipV: TC = dropChipView(col)
    verifyTurn()
    switchPlayer()
    val myChipVOpt = runNextTurn()
    myChipVOpt match {
      case Some(chipView) => List(otherChipV, chipView)
      case None => List(otherChipV)
    }
  }

  def dropChipView(col: Int): TC = {
    val newChip = createChip(col)
    val dropChipsCount = rows - fourConnect.findFirstTakenSpot(col).get.row
    val transition = new TranslateTransition {
      duration = Duration(1000)
      node = newChip
      byY = dropHeight(dropChipsCount)
    }
    transition.play()
    newChip
  }


  protected def dropHeight(dropHeight: Int): Double

  def verifyTurn() = {
    if (!gameStartedSubject.gameStarted) startGame()
    val winners = fourConnect.winningSpots(activeChip)

    for {
      spotView <- gameSpots
      winner <- winners
      spot <- winner
      if spot.col == spotView.getSpot.col && spot.row == spotView.getSpot.row
    } {
      if (gameStartedSubject.gameStarted) finishGame()
      spotView.blink()
    }
  }

  def switchPlayer(): Unit = {
    activeChip = activeChip.other
    if (chipsToPlay != null) for (chip <- chipsToPlay) changeMaterial(chip)
  }

  def startGame() = {
    gameStartedSubject.startGame()
    gameWinnerSubject.startGame()
    if (playAloneMode) myChip = activeChip.other
  }


  protected def calcMyTurn(): TC = {
    val col = Combinations.evalBestMove(fourConnect.game, myChip)
    fourConnect.dropChip(col, myChip)
    println("My turn: " + col)
    dropChipView(col)
  }

  def runNextTurn(): Option[TC] = {
    if (!gameWinnerSubject.isFinish && playAloneMode
      && (myChip == activeChip)) Some(calcMyTurn())
    else None
  }

  protected def changeMaterial(chip: TC)

  def finishGame() = {
    chipsToPlay.foreach(chipView => chipView.visible = false)
    gameStartedSubject.finishGame()
    gameWinnerSubject.finishGame(activeChip)
  }


  def playAlone(playAlone: Boolean) = {
    playAloneMode = playAlone
  }
}
