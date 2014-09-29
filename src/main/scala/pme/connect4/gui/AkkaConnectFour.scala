package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{BorderPane, Pane}
import scalafx.scene.paint.Color

object AkkaConnectFour extends JFXApp {

  import pme.connect4.gui.GuiGameConfig._


  stage = new JFXApp.PrimaryStage {
    title = "Akka Connect Four"
    width = paneSize._1
    height = paneSize._2
    resizable = false
    scene = new Scene(rootPane) {
      fill = Color.LightGreen
    }
  }

  lazy val rootPane = new BorderPane {
    center = gamePane
    bottom = controlPane
  }

  def gamePane = new GameBoard(gameSize)

  def controlPane: Pane = {
    val controlPane = new ControlPane()
    controlPane.newGameButton.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        rootPane.center = gamePane
      }
    })
    controlPane
  }

}
