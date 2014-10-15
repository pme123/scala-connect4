package pme.connect4.gui


import pme.connect4.domain.GameConfig._
import pme.connect4.domain._
import pme.connect4.gui.ChipView2D._
import pme.connect4.gui.GuiGameConfig2D._
import pme.connect4.util.{Observer, Subject}

import scalafx.Includes._
import scalafx.animation.TranslateTransition
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape._
import scalafx.util.Duration

class GameBoard2D extends Pane with GameBoard[ChipView2D, SpotView2D] {


  override def startNewGame() = {
    super.startNewGame()
    content = chipsToPlay ++ gameSpots
  }

  def createChip(col: Int): ChipView2D = {
    val chipView: ChipView2D = createChip(col, activeChip)
    chipView.onMouseClicked = (me: MouseEvent) => content.add(handleChipSelected(col, chipView))
    chipView
  }

  def initGameSpots = {
    for {
      col <- 0 until horFieldCount
      row <- 0 until verFieldCount
    } yield {
      val rect = new Rectangle {
        x = paneOffsetX + col * fieldWidth + slotMargin
        y = paneOffsetY + row * fieldHeight + slotMargin
        width = fieldWidth - 2 * slotMargin
        height = fieldHeight - 2 * slotMargin
        fill = Color.DeepSkyBlue
      }
      val hole = new Ellipse() {
        centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
        centerY = paneOffsetY + row * fieldHeight + fieldHeight / 2
        radiusX = fieldWidth / 2 - 4 * slotMargin
        radiusY = fieldHeight / 2 - 4 * slotMargin
      }
      val shape = Shape.subtract(rect, hole).asInstanceOf[javafx.scene.shape.Path]
      new SpotView2D(fourConnect.game.slots(col).spots(verFieldCount - 1 - row), shape)
    }
  }


  def createChip(col: Int, chip: Chip): ChipView2D = {
    val chipView: ChipView2D = new ChipView2D(chip) {
      centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
      centerY = paneOffsetY - fieldHeight / 2
      radiusX = fieldWidth / 2 - 4 * slotMargin
      radiusY = fieldHeight / 2 - 4 * slotMargin
      fill = colorMap(chip)

    }
    chipView
  }



  def runNextTurn() = {
    if (!gameWinnerSubject.isFinish && playAloneMode) {
      if (myChip == activeChip) calcMyTurn()
    }
  }

  def verifyTurn() = {
    if (!gameStartedSubject.gameStarted) startGame()
    val winners = fourConnect.winningSpots(activeChip)

    for {
      spotView <- gameSpots
      winner <- winners
      spot <- winner
      if spot.col == spotView.spot.col && spot.row == spotView.spot.row
    } yield {
      if (gameStartedSubject.gameStarted) finishGame()
      spotView.blink
    }
  }

  def startGame() = {
    gameStartedSubject.startGame()
    gameWinnerSubject.startGame()
    if (playAloneMode) myChip = activeChip.other
  }

  def playAlone(playAlone: Boolean) = {
    playAloneMode = playAlone
  }

  def calcMyTurn() = {

    val col = Combinations.evalBestMove(fourConnect.game, myChip)
    fourConnect.dropChip(col, myChip)
    dropChipView(col)
    println("My turn: " + col)
  }

  def finishGame() = {
    chipsToPlay.foreach(chipView => chipView.visible = false)
    gameStartedSubject.finishGame()
    gameWinnerSubject.finishGame(activeChip)
  }

  def addGameStartedObserver(observer: Observer[GameStartedSubject]) = gameStartedSubject.addObserver(observer)

  def addGameWinnerObserver(observer: Observer[GameWinnerSubject]) = gameWinnerSubject.addObserver(observer)

  protected def changeMaterial(chip: ChipView2D): Unit = chip.fill = colorMap(activeChip)
  protected def dropHeight(dropHeight: Int): Double =     dropHeight * fieldHeight

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