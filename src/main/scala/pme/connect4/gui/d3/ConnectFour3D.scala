package pme.connect4.gui.d3


import pme.connect4.gui.d3.ConnectFourConfig3D._
import pme.connect4.gui.{ControlPane, InfoPane}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Box

/** ScalaFX implementation of `MoleculeSampleApp` from tutorial
  * [[http://docs.oracle.com/javafx/8/3d_graphics/jfxpub-3d_graphics.htm Getting Started with JavaFX 3D Graphics]]
  * by Cindy Castillo and John Yoon.
  *
  * @author Jarek Sacha
  */
object ConnectFour3D extends JFXApp {
  app =>
  System.setProperty("prism.dirtyopts", "false")

  private  val root = new VBox()
  private   val content3d = new Group()
  private  val world = new Xform()
  private  val camera: PerspectiveCamera = new PerspectiveCamera(true)
  private  val cameraXform = new Xform()
  private  val cameraXform2 = new Xform()
  private  val cameraXform3 = new Xform()
  private  val cameraDistance: Double = 450

  private var gameBoard: GameBoard3D = new GameBoard3D
  private var mousePosX: Double = .0
  private var mousePosY: Double = .0
  private var mouseOldX: Double = .0
  private var mouseOldY: Double = .0
  private var mouseDeltaX: Double = .0
  private var mouseDeltaY: Double = .0



  buildScene()
  buildCamera()
  buildGround()
  buildGameBoard()

  stage = new JFXApp.PrimaryStage {
    scene = new Scene(root, panelSize._1, panelSize._2, depthBuffer = true, antiAliasing = SceneAntialiasing.Balanced) {
      fill = Color.Gray
      title = "4 Connect"

    }
    handleMouse(scene(), world)

  }

  private def buildScene() {
    root.content = Seq(controlPane,subScene    ,infoPanel  )
    content3d.children += world

  }

  def  subScene:SubScene = new SubScene(content3d, panelSize._1, panelSize._2-100,true, SceneAntialiasing.Disabled) {
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

    world.children += ground
  }
  private def buildGameBoard() {
    gameBoard.startNewGame()
    content3d.children += gameBoard
  }

   private def handleMouse(scene: Scene, content3d: Node) {
    scene.onMousePressed = (me: MouseEvent) => {
      mousePosX = me.sceneX
      mousePosY = me.sceneY
      mouseOldX = me.sceneX
      mouseOldY = me.sceneY
    }
    scene.onMouseDragged = (me: MouseEvent) => {
      mouseOldX = mousePosX
      mouseOldY = mousePosY
      mousePosX = me.sceneX
      mousePosY = me.sceneY
      mouseDeltaX = mousePosX - mouseOldX
      mouseDeltaY = mousePosY - mouseOldY
      val modifier = if (me.isControlDown) 0.1 else if (me.isShiftDown) 10 else 1.0
      val modifierFactor = 0.1
      if (me.isPrimaryButtonDown) {
        cameraXform.ry.angle = cameraXform.ry.angle() - mouseDeltaX * modifierFactor * modifier * 2.0
        cameraXform.rx.angle = cameraXform.rx.angle() + mouseDeltaY * modifierFactor * modifier * 2.0
      } else if (me.isSecondaryButtonDown) {
        val z = camera.translateZ()
        val newZ = z + mouseDeltaX * modifierFactor * modifier
        camera.translateZ = newZ
      } else if (me.isMiddleButtonDown) {
        cameraXform2.t.x = cameraXform2.t.x() + mouseDeltaX * modifierFactor * modifier * 0.3
        cameraXform2.t.x = cameraXform2.t.y() + mouseDeltaY * modifierFactor * modifier * 0.3
      }
    }
  }


}
