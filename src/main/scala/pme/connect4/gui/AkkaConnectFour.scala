package pme.connect4.gui

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.BorderPane

object AkkaConnectFour extends JFXApp {
  val paneSize = (800, 800)
  val gameSize= (784.0, 762.0)


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
  }
  lazy val gamePane = new GameBoard(gameSize)



}
