package pme.connect4.gui.d3


import pme.connect4.gui.d3.ConnectFourConfig3D._
import pme.connect4.gui.{ControlPane, InfoPane}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.scene._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Box
import scalafx.scene.transform.Rotate

/** ScalaFX implementation of `MoleculeSampleApp` from tutorial
  * [[http://docs.oracle.com/javafx/8/3d_graphics/jfxpub-3d_graphics.htm Getting Started with JavaFX 3D Graphics]]
  * by Cindy Castillo and John Yoon.
  *
  * @author Jarek Sacha
  */
object ConnectFour3D extends JFXApp {

  System.setProperty("prism.dirtyopts", "false")


  private object Model {
    val mousePosX = DoubleProperty(.0)
    val mousePosY = DoubleProperty(.0)
    val mouseOldX = DoubleProperty(.0)
    val mouseOldY = DoubleProperty(.0)
    val mouseDeltaX = DoubleProperty(.0)
    val mouseDeltaY = DoubleProperty(.0)

  }

  private object View {
    app =>
      val root = new VBox()
      val content3d = new Xform()
      val camera: PerspectiveCamera = new PerspectiveCamera(true)
      val cameraXform = new Xform()
      val cameraXform2 = new Xform()
      val cameraXform3 = new Xform()
      val cameraDistance: Double = 450

     val gameBoard: GameBoard3D = new GameBoard3D
    private def buildScene() {
      root.content = Seq(controlPane,subScene    ,infoPanel  )
    }

    private def  subScene:SubScene = new SubScene(content3d, panelSize._1, panelSize._2-100,true, SceneAntialiasing.Disabled) {
      camera = app.camera
    }

    lazy val controlPane = new ControlPane(gameBoard)

    lazy val infoPanel = new InfoPane(gameBoard)

    private def buildCamera() {
      content3d.children += cameraXform
      cameraXform.children += cameraXform2
      cameraXform2.children += cameraXform3
      cameraXform3.children += camera
      cameraXform3.rotateZ = 180.0
      camera.nearClip = 0.1
      camera.farClip = 10000.0
      camera.translateZ = -cameraDistance
      cameraXform.ry.angle = 320.0
      cameraXform.rx.angle = 40
    }


    private def buildGround() {

      val ground = new Box(groundSize, -gameOffsetY, groundSize) {
        translateY = gameOffsetY-fieldWidth
        material = groundMaterial
      }
      content3d.children += ground
    }
    private def buildGameBoard() {
      gameBoard.startNewGame()
      content3d.children += gameBoard
    }
    private def buildLight() = {
      val light = new PointLight() {
        color = Color.White
        rotationAxis = Rotate.ZAxis
        rotate = -45
        translateX = 50
        translateY = 50
        translateZ = -50
      }
      root.children += light
    }
    buildScene()
    buildCamera()
    buildGround()
    buildGameBoard()
  //  buildLight()
  }


  stage = new JFXApp.PrimaryStage {
    scene = new Scene(View.root, panelSize._1, panelSize._2, depthBuffer = true, antiAliasing = SceneAntialiasing.Balanced) {
      fill = Color.Gray
      title = "4 Connect"

    }
  }
  handleMouse()

   private def handleMouse() {
    stage.scene().onMousePressed = (me: MouseEvent) => {
     Model.mousePosX() = me.sceneX
      Model.mousePosY() = me.sceneY
      Model.mouseOldX() = me.sceneX
      Model.mouseOldY() = me.sceneY
    }
     stage.scene().onMouseDragged = (me: MouseEvent) => {
      Model.mouseOldX() = Model.mousePosX()
      Model.mouseOldY() = Model.mousePosY()
      Model.mousePosX() = me.sceneX
      Model.mousePosY() = me.sceneY
      Model.mouseDeltaX() = Model.mousePosX() - Model.mouseOldX()
      Model.mouseDeltaY() = Model.mousePosY() - Model.mouseOldY()
      val modifier = if (me.isControlDown) 0.1 else if (me.isShiftDown) 10 else 1.0
      val modifierFactor = 0.1
      if (me.isPrimaryButtonDown) {
        View.cameraXform.ry.angle = View.cameraXform.ry.angle() - Model.mouseDeltaX() * modifierFactor * modifier * 2.0
        View.cameraXform.rx.angle = View.cameraXform.rx.angle() + Model.mouseDeltaY() * modifierFactor * modifier * 2.0
      } else if (me.isSecondaryButtonDown) {
        val z = View.camera.translateZ()
        val newZ = z + Model.mouseDeltaX() * modifierFactor * modifier
        View.camera.translateZ = newZ
      } else if (me.isMiddleButtonDown) {
        View.cameraXform2.t.x = View.cameraXform2.t.x() + Model.mouseDeltaX() * modifierFactor * modifier * 0.3
        View.cameraXform2.t.x = View.cameraXform2.t.y() + Model.mouseDeltaY() * modifierFactor * modifier * 0.3
      }
    }
  }
}
