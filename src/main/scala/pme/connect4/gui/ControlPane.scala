package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import pme.connect4.util.Observer

import scalafx.geometry.Insets
import scalafx.scene.control.{Button, CheckBox}
import scalafx.scene.layout.HBox
import scalafx.stage.Stage


class ControlPane(gameBoard: GameBoard[_ <: ChipView, _ <: SpotView]) extends HBox with Observer[GameStartedSubject] {

//  import pme.connect4.gui.ConnectFourConfig2D._

  val newGameButton = new Button {
    text = "Start new Game"
    defaultButton = true
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        changeColorButton.disable = false
        playAloneCheckBox.disable = false
        gameBoard.startNewGame()
      }
    }
  }
  val changeColorButton = new Button {
    text = "Change Color"
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        gameBoard.switchPlayer()
      }
    }
  }
  val playAloneCheckBox:CheckBox = new CheckBox() {
    text = "Play alone"
    indeterminate = false
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        println(s"Play alone: " + playAloneCheckBox.isSelected)
        gameBoard.playAlone(playAloneCheckBox.isSelected)
      }
    }
  }
  val myDialog = new Stage {
    title = "Error Message"

  }
  val okButton = new Button("Ok")
  okButton.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent) {
      myDialog.close()
    }
  })

  def receiveUpdate(subject: GameStartedSubject) = {
    changeColorButton.disable = subject.gameStarted
    playAloneCheckBox.disable = subject.gameStarted
  }

  content = List(newGameButton, changeColorButton, playAloneCheckBox)
  margin = Insets(10, 20, 20, 20)
  spacing = 20
  gameBoard.addGameStartedObserver(this)
//  gameBoard.startNewGame
}
