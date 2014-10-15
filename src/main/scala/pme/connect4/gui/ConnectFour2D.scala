package pme.connect4.gui

import javafx.event.{ActionEvent, EventHandler}

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.{VBox, BorderPane, Pane}
import scalafx.scene.paint.Color
import scalafx.stage.PopupWindow

object ConnectFour2D extends JFXApp {

  import pme.connect4.gui.GuiGameConfig2D._



  stage = new JFXApp.PrimaryStage {
    title = "Akka Connect Four"
    width = paneSize._1
    height = paneSize._2
    resizable = false
    scene = new Scene(rootPane) {
      fill = Color.LightGreen
    }
  }

  lazy val rootPane = new VBox() {
    content = Seq(controlPane, gameBoard, infoPanel)
  }

  lazy val gameBoard = new GameBoard2D


  lazy val controlPane = new ControlPane2D(gameBoard)

  lazy val infoPanel = new InfoPane2D(gameBoard)


}
