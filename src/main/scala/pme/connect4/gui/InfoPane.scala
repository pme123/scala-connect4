package pme.connect4.gui

import pme.connect4.util.Observer

import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Insets
import scalafx.scene.Group
import scalafx.scene.control.Label

class InfoPane(gameBoard: GameBoard[_ <: ChipView,_ <: SpotView]) extends Group {

  import pme.connect4.gui.ConnectFourConfig._

  content = View
  gameBoard.addGameStartedObserver(new GameStartedObserver)
  gameBoard.addGameWinnerObserver(new GameWinnerObserver)


  private object Model {
    val text = ObjectProperty(this, "text", "Welcome  to  4-Connect!")
  }

  private object View extends Group {
    val info = new Label() {
      layoutX = paneOffsetX
      text <== Model.text
    }
    content = List(info)
    margin = Insets(10, 0, 20, 0)
  }

  private class GameWinnerObserver extends Observer[GameWinnerSubject]{
    def receiveUpdate(subject: GameWinnerSubject) = {
      Model.text() = "%s\nThe winner is %s.".format(View.info.text.value, subject.gameWinner.name)
    }
  }

  private class GameStartedObserver extends Observer[GameStartedSubject]{
    def receiveUpdate(subject: GameStartedSubject) = {
      if (subject.gameStarted) Model.text() = "Game is running!"
      else Model.text() = "Game is finished!"
    }
  }
}
