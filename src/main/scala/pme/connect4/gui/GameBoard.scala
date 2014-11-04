package pme.connect4.gui

import pme.connect4.domain.GameConfig._
import pme.connect4.domain._
import pme.connect4.util.{Observer, Subject}

import scalafx.animation.TranslateTransition
import scalafx.scene.Node
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.util.Duration

trait GameBoard[TC <: ChipView, TS <: SpotView] extends Node {
  val gameStartedSubject = new GameStartedSubject
  val gameWinnerSubject = new GameWinnerSubject

  var fourConnect:ConnectFourGame = null
  var chipsToPlay: Seq[TC] = Nil
  var gameSpots: Map[(Int,Int), TS] = Map()
  var activeChip: Chip = RedChip
  var playAloneMode: Boolean = false
  var myChip: Chip = activeChip.other

  def startNewGame() = {
    fourConnect = new ConnectFourGame
    gameStartedSubject.gameStarted = false
    chipsToPlay = initChipsToPlay
    gameSpots = initGameSpots

  }

  private def initChipsToPlay: Seq[TC] = {
    for {
      col <- 0 until cols
    } yield {
      val chipView =createChip(col)
      addMouseListener(col, chipView)
      chipView
    }
  }

  protected def createChip(col: Int): TC   =createChip(col, activeChip)

  protected def createChip(col: Int, chip: Chip): TC

  private def initGameSpots: Map[(Int,Int), TS] = {
    (for {
      col <- 0 until cols
      row <- 0 until rows
    } yield {
      val spotView = createSpot(col, row)
      (spotView.getSpot.col, spotView.getSpot.row) -> spotView
    }).toMap
  }

  protected def createSpot(col: Int, row: Int): TS

  def addMouseListener(col: Int, chipView: ChipView):Unit =
    chipView.onMouseClicked = (me: MouseEvent) => handleChipSelected(col, chipView)


  protected def handleChipSelected(col: Int, chip: ChipView) = {
    fourConnect.dropChip(col, activeChip)
    if (!fourConnect.hasEmptySlot(col)) chip.setVisible(false)
      dropChipView(col)
  }

  def dropChipView(col: Int): Unit = {
    val newChip = createChip(col)
    addNewChipView(newChip)
    val spot: Spot = fourConnect.findFirstTakenSpot(col).get
    val spotView = gameSpots(spot.col, spot.row)
    val distance =dropHeight(spotView)
    val transition = new TranslateTransition {
      duration = Duration(Math.abs(distance))
      node = newChip
      byY = distance
    }
    transition.play()
    verifyTurn()
    switchPlayer()
    runNextTurn()
  }
  protected def addNewChipView(newChip: TC)

  protected def dropHeight(spotView: TS): Double

  def verifyTurn() = {
    if (!gameStartedSubject.gameStarted) startGame()
    val winners = fourConnect.winningSpots(activeChip)

    for {
      (spot,spotView) <- gameSpots
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


  protected def calcMyTurn() = {
    val col = Combinations.evalBestMove(fourConnect.game, myChip)
    fourConnect.dropChip(col, myChip)
    dropChipView(col)
    println("My turn: " + col)
  }
  def runNextTurn() = {
    if (!gameWinnerSubject.isFinish && playAloneMode
      && (myChip == activeChip)) calcMyTurn()

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

  def addGameStartedObserver(observer: Observer[GameStartedSubject]) = gameStartedSubject.addObserver(observer)

  def addGameWinnerObserver(observer: Observer[GameWinnerSubject]) = gameWinnerSubject.addObserver(observer)

}
class GameStartedSubject extends Subject[GameStartedSubject] {
  var gameStarted = false

  protected[gui] def startGame() = {
    gameStarted = true
    notifyObservers()
  }

  protected[gui] def finishGame() = {
    gameStarted = false
    notifyObservers()
  }

}

class GameWinnerSubject extends Subject[GameWinnerSubject] {
  var gameWinner: Chip = SpaceChip

  protected[gui] def isFinish = gameWinner != SpaceChip

  protected[gui] def startGame() = {
    gameWinner = SpaceChip
  }

  protected[gui] def finishGame(winner: Chip) = {
    gameWinner = winner
    notifyObservers()
  }

}