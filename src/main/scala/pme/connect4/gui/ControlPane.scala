package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import pme.connect4.util.Observer

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{AnchorPane, VBox}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}

/**
 * Created by pascal.mengelt on 29.09.2014.
 */
class ControlPane(gameBoard: GameBoard) extends AnchorPane with Observer[GameBoard] {

  import pme.connect4.gui.GuiGameConfig._
var activeGame:GameBoard=gameBoard

  val newGameButton = new Button {
    layoutX = paneOffsetX
    layoutY = gameSize._2 - paneOffsetY / 2
    minWidth = boardWidth / 2
    prefWidth = boardWidth / 2
    text = "Start new Game"
    defaultButton = true
  }
  val changeColorButton = new Button {
    layoutX = paneOffsetX + boardWidth / 2
    layoutY = gameSize._2 - paneOffsetY / 2
    minWidth = boardWidth / 2
    prefWidth = boardWidth / 2
    text = "Change Color"
    onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        if(activeGame.gameStarted)  popupErrorMsg
        else         activeGame.switchPlayer
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
  def receiveUpdate(gameBoard: GameBoard) = {
    activeGame = gameBoard
    activeGame.addObserver(this)
    changeColorButton.disable = gameBoard.gameStarted
  }

  def popupErrorMsg {

    val myDialogScene = new Scene() {
      content = new VBox {
        content = Seq(new Text {
          text = "Game has already started!"
        }, okButton)
        spacing = 30
        alignment = Pos.Center
        padding = Insets(10)
      }
    }
    myDialog.initModality(Modality.APPLICATION_MODAL);
    myDialog.scene=myDialogScene
    myDialog.show();
  }




  minWidth = gameSize._1
  prefWidth = gameSize._1
  content = List(newGameButton, changeColorButton)
  margin = Insets(0, 0, 10, 0)

  receiveUpdate(gameBoard)
}
