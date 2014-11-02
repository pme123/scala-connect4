package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import pme.connect4.util.Observer

import scalafx.beans.property.BooleanProperty
import scalafx.geometry.Insets
import scalafx.scene.Group
import scalafx.scene.control.{Button, CheckBox}
import scalafx.scene.layout.HBox
import scalafx.stage.Stage


class ControlPane(gameBoard: GameBoard[_ <: ChipView, _ <: SpotView]) extends Group with Observer[GameStartedSubject] {
  content = View
  margin = Insets(10, 20, 20, 20)
  gameBoard.addGameStartedObserver(this)
  gameBoard.startNewGame()

  def receiveUpdate(subject: GameStartedSubject) = {
    Model.gameStarted() = subject.gameStarted
  }

  private object Model {
    val gameStarted = BooleanProperty(value = true)
  }

  private object View extends HBox {


    val newGameButton = new Button {
      text = "Start new Game"
      defaultButton = true
      onAction = new EventHandler[ActionEvent] {
        override def handle(event: ActionEvent) {
          Model.gameStarted() = false
          gameBoard.startNewGame()
        }
      }
    }
    val changeColorButton = new Button {
      text = "Change Color"
      disable <== Model.gameStarted
      onAction = new EventHandler[ActionEvent] {
        override def handle(event: ActionEvent) {
          gameBoard.switchPlayer()
        }
      }
    }
    val playAloneCheckBox: CheckBox = new CheckBox() {
      text = "Play alone"
      indeterminate = false
      disable <== Model.gameStarted
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
    content = List(newGameButton, changeColorButton, playAloneCheckBox)
    spacing = 20
  }

}
