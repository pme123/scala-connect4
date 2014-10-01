package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import pme.connect4.util.Observer

import scalafx.geometry.Insets
import scalafx.scene.control.Button
import scalafx.scene.layout.AnchorPane
import scalafx.stage.Stage

/**
 * Created by pascal.mengelt on 29.09.2014.
 */
class ControlPane(gameBoard: GameBoard) extends AnchorPane with Observer[GameStartedSubject] {

  import pme.connect4.gui.GuiGameConfig._

  val newGameButton = new Button {
    layoutX = paneOffsetX
    minWidth = boardWidth / 2
    prefWidth = boardWidth / 2
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
    layoutX = paneOffsetX + boardWidth / 2
    minWidth = boardWidth / 2
    prefWidth = boardWidth / 2
    text = "Change Color"
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        gameBoard.switchPlayer
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
  }


  content = List(newGameButton, changeColorButton)
  margin = Insets(10, 0, 20, 0)
  gameBoard.addGameStartedObserver(this)
//  gameBoard.startNewGame
}
