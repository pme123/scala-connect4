package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import pme.connect4.util.Observer

import scalafx.geometry.Insets
import scalafx.scene.control.{CheckBox, Button}
import scalafx.scene.layout.{HBox, AnchorPane}
import scalafx.stage.Stage

/**
 * Created by pascal.mengelt on 29.09.2014.
 */
class ControlPane(gameBoard: GameBoard) extends HBox with Observer[GameStartedSubject] {

  import pme.connect4.gui.GuiGameConfig._

  val newGameButton = new Button {
    text = "Start new Game"
    defaultButton = true
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        changeColorButton.setDisable(false)
        gameBoard.startNewGame
      }
    }
  }
  val changeColorButton = new Button {
    text = "Change Color"
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        gameBoard.switchPlayer
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
  val okButton = new Button("Ok");
  okButton.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent) {
      myDialog.close
    }
  })

  def receiveUpdate(subject: GameStartedSubject) = {
    changeColorButton.disable = subject.gameStarted
    playAloneCheckBox.disable = subject.gameStarted
  }

  content = List(newGameButton, changeColorButton, playAloneCheckBox)
  margin = Insets(10, paneOffsetX, 20, paneOffsetX)
  spacing = 20
  gameBoard.addGameStartedObserver(this)
//  gameBoard.startNewGame
}
