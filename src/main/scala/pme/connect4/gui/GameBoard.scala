package pme.connect4.gui

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import pme.connect4.domain._
import pme.connect4.util.{Observer, Subject}

import scalafx.animation.TranslateTransition
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape._
import scalafx.util.Duration


/**
 * Created by pascal.mengelt on 18.09.2014.
 */
class GameBoard extends Pane {

  import pme.connect4.domain.GameConfig._
  import pme.connect4.gui.ChipView._
  import pme.connect4.gui.GuiGameConfig._


  val gameStartedSubject = new GameStartedSubject
  val gameWinnerSubject = new GameWinnerSubject

  var fourConnect = new ConnectFourGame
  var chipsToPlay: Seq[ChipView] = null
  var gameSpots: Seq[SpotView] = null
  var activeChip: Chip = RedChip
  var playAloneMode: Boolean = false
  var myChip: Chip = activeChip.other


  def startNewGame = {
    fourConnect = new ConnectFourGame
    chipsToPlay = initChipsToPlay
    gameSpots = initGameSpots
    gameStartedSubject.gameStarted = false
    content = chipsToPlay ++ gameSpots
  }

  def initChipsToPlay: Seq[ChipView] = {
    for {
      col <- 0 until horFieldCount
    } yield {
      val chip: ChipView = createChip(col, fieldWidth, fieldHeight, activeChip)
      chip.setOnMouseClicked(new EventHandler[MouseEvent] {
        override def handle(event: MouseEvent) {
          fourConnect.dropChip(col, activeChip)
          dropChipView(col)
          if (!fourConnect.hasEmptySlot(col)) chip.setVisible(false)
        }
      })
      chip
    }
  }

  def dropChipView(col: Int): Unit = {
    val newChip = createChip(col, fieldWidth, fieldHeight, activeChip)
    content.add(0, newChip)
    val dropHeight = rows - fourConnect.findFirstTakenSpot(col).get.row
    val transition = new TranslateTransition {
      duration = Duration(1000)
      node = newChip
      byY = dropHeight * fieldHeight
    }
    transition.play()
    verifyTurn
    switchPlayer
     runNextTurn
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
      val shape = (Shape.subtract(rect, hole)).asInstanceOf[javafx.scene.shape.Path]
      new SpotView(fourConnect.game.slots(col).spots(verFieldCount - 1 - row), shape)
    }
  }


  def createChip(col: Int, fieldWidth: Double, fieldHeight: Double, chip: Chip): ChipView = {
    val chipView: ChipView = new ChipView(chip) {
      centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
      centerY = paneOffsetY - fieldHeight / 2
      radiusX = fieldWidth / 2 - 4 * slotMargin
      radiusY = fieldHeight / 2 - 4 * slotMargin
      fill = colorMap(chip)

    }
    chipView
  }

  def switchPlayer: Unit = {
    activeChip = activeChip.other
    if(chipsToPlay!=null)    for (chip <- chipsToPlay) chip.fill = colorMap(activeChip)
  }

  def runNextTurn = {
    if (!gameWinnerSubject.isFinish && playAloneMode) {
      if (myChip == activeChip) calcMyTurn
    }
  }

  def verifyTurn = {
    if (!gameStartedSubject.gameStarted) startGame
    val winners = fourConnect.winningSpots(activeChip)

    for {
      spotView <- gameSpots
      winner <- winners
      spot <- winner
      if (spot.col == spotView.spot.col && spot.row == spotView.spot.row)

    } yield {
      if (gameStartedSubject.gameStarted) finishGame
      spotView.blink
    }
  }

  def startGame = {
    gameStartedSubject.startGame
    gameWinnerSubject.startGame
    if (playAloneMode) myChip = activeChip.other
  }

  def playAlone(playAlone: Boolean) = {
    playAloneMode = playAlone
  }

  def calcMyTurn = {

    val col = Combinations.evalBestMove(fourConnect.game,myChip)
    fourConnect.dropChip(col, myChip)
    dropChipView(col)
    println("My turn: " + col)
  }

  def finishGame = {
    chipsToPlay.foreach(chipView => chipView.visible = false)
    gameStartedSubject.finishGame
    gameWinnerSubject.finishGame(activeChip)
  }

  def addGameStartedObserver(observer: Observer[GameStartedSubject]) = gameStartedSubject.addObserver(observer)

  def addGameWinnerObserver(observer: Observer[GameWinnerSubject]) = gameWinnerSubject.addObserver(observer)
}

class GameStartedSubject extends Subject[GameStartedSubject] {
  var gameStarted = false

  protected[gui] def startGame = {
    gameStarted = true
    notifyObservers()
  }

  protected[gui] def finishGame = {
    gameStarted = false
    notifyObservers()
  }

}

class GameWinnerSubject extends Subject[GameWinnerSubject] {
  var gameWinner: Chip = SpaceChip

  protected[gui] def isFinish = gameWinner != SpaceChip
  protected[gui] def startGame = {
    gameWinner = SpaceChip
  }
  protected[gui] def finishGame(winner: Chip) = {
    gameWinner = winner
    notifyObservers()
  }

}