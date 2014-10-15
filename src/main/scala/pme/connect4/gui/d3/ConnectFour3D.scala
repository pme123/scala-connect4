package pme.connect4.gui.d3


import pme.connect4.domain.{ConnectFourGame, Chip, RedChip}
import pme.connect4.domain.GameConfig._
import pme.connect4.gui.ChipView._
import pme.connect4.gui.d3.ConnectFourConfig3D._

import scalafx.Includes._
import scalafx.animation.{TranslateTransition, Timeline}
import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.paint.{Color, PhongMaterial}
import scalafx.scene.shape.{Box, Cylinder, Sphere}
import scalafx.scene.transform.Rotate
import scalafx.util.Duration

/** ScalaFX implementation of `MoleculeSampleApp` from tutorial
  * [[http://docs.oracle.com/javafx/8/3d_graphics/jfxpub-3d_graphics.htm Getting Started with JavaFX 3D Graphics]]
  * by Cindy Castillo and John Yoon.
  *
  * @author Jarek Sacha
  */
object ConnectFour3D extends JFXApp {
  app =>
  System.setProperty("prism.dirtyopts", "false")

  private final val root = new Group()
  private final val chipsToPlay = new Group()
  private final val world = new Xform()
  private final val camera: PerspectiveCamera = new PerspectiveCamera(true)
  private final val cameraXform = new Xform()
  private final val cameraXform2 = new Xform()
  private final val cameraXform3 = new Xform()
  private final val cameraDistance: Double = 450


  private var activeChip: Chip = RedChip
  private var timelinePlaying = false
  private var ONE_FRAME: Double = 1.0 / 24.0
  private var DELTA_MULTIPLIER: Double = 200.0
  private val CONTROL_MULTIPLIER: Double = 0.1
  private val SHIFT_MULTIPLIER: Double = 0.1
  private val ALT_MULTIPLIER: Double = 0.5
  private var mousePosX: Double = .0
  private var mousePosY: Double = .0
  private var mouseOldX: Double = .0
  private var mouseOldY: Double = .0
  private var mouseDeltaX: Double = .0
  private var mouseDeltaY: Double = .0

  var fourConnect = new ConnectFourGame

  buildScene()
  buildCamera()
  buildGround()
  buildChipsToPlay()
  fourConnect = new ConnectFourGame

  stage = new JFXApp.PrimaryStage {
    scene = new Scene(root, 1024, 768, depthBuffer = true, antiAliasing = SceneAntialiasing.Balanced) {
      fill = Color.Gray
      title = "4 Connect"
      camera = app.camera
    }
    handleKeyboard(scene(), world)
    handleMouse(scene(), world)

  }


  private def buildScene() {
    root.children += world
  }

  private def buildCamera() {
    root.children += cameraXform
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

    val ground = new Box(groundSize, groundSize / 4, groundSize) {
      translateY = -groundSize / 2
      material = groundMaterial
    }

    world.children += ground
  }

  private def buildChipsToPlay() = {
    for {
      col <- 0 until cols
    }  {
      val chip: ChipView3D = createChip(col,  activeChip)
      chipsToPlay.children += chip
    }
    world.children += chipsToPlay
  }

  def createChip(col: Int, chip: Chip): ChipView3D = {
    val chipView: ChipView3D = new ChipView3D(chip) {
      translateX = -gameWidth/2 + col * gameWidth / cols + chipRadius
      translateY = groundSize/4
    }
    chipView.onMousePressed = (me: MouseEvent) => {
      fourConnect.dropChip(col, activeChip)
      dropChipView(col)
      if (!fourConnect.hasEmptySlot(col)) chipView.setVisible(false)
    }
    chipView
  }
  def dropChipView(col: Int): Unit = {
    val newChip = createChip(col, activeChip)
    world.children += newChip
    val dropHeight = rows - fourConnect.findFirstTakenSpot(col).get.row
    val transition = new TranslateTransition {
      duration = Duration(1000)
      node = newChip
      byY = -dropHeight * groundSize/2/(rows+1)
    }
    transition.play()
//    verifyTurn()
 //   switchPlayer()
//    runNextTurn()
  }



   private def handleMouse(scene: Scene, root: Node) {
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

  private def handleKeyboard(scene: Scene, root: Node) {
    //    val moveCamera: Boolean = true
    scene.onKeyPressed = (event: KeyEvent) => {
      //      val currentTime: Duration = null
      event.getCode match {
        case KeyCode.Z =>
          if (event.isShiftDown) {
            cameraXform.ry.setAngle(0.0)
            cameraXform.rx.setAngle(0.0)
            camera.setTranslateZ(-300.0)
          }
          cameraXform2.t.setX(0.0)
          cameraXform2.t.setY(0.0)
        case KeyCode.UP =>
          if (event.isControlDown && event.isShiftDown) {
            cameraXform2.t.setY(cameraXform2.t.getY - 10.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown && event.isShiftDown) {
            cameraXform.rx.setAngle(cameraXform.rx.getAngle - 10.0 * ALT_MULTIPLIER)
          } else if (event.isControlDown) {
            cameraXform2.t.setY(cameraXform2.t.getY - 1.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown) {
            cameraXform.rx.setAngle(cameraXform.rx.getAngle - 2.0 * ALT_MULTIPLIER)
          } else if (event.isShiftDown) {
            val z: Double = camera.getTranslateZ
            val newZ: Double = z + 5.0 * SHIFT_MULTIPLIER
            camera.setTranslateZ(newZ)
          }
        case KeyCode.DOWN =>
          if (event.isControlDown && event.isShiftDown) {
            cameraXform2.t.setY(cameraXform2.t.getY + 10.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown && event.isShiftDown) {
            cameraXform.rx.setAngle(cameraXform.rx.getAngle + 10.0 * ALT_MULTIPLIER)
          } else if (event.isControlDown) {
            cameraXform2.t.setY(cameraXform2.t.getY + 1.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown) {
            cameraXform.rx.setAngle(cameraXform.rx.getAngle + 2.0 * ALT_MULTIPLIER)
          } else if (event.isShiftDown) {
            val z: Double = camera.getTranslateZ
            val newZ: Double = z - 5.0 * SHIFT_MULTIPLIER
            camera.setTranslateZ(newZ)
          }
        case KeyCode.RIGHT =>
          if (event.isControlDown && event.isShiftDown) {
            cameraXform2.t.setX(cameraXform2.t.getX + 10.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown && event.isShiftDown) {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle - 10.0 * ALT_MULTIPLIER)
          } else if (event.isControlDown) {
            cameraXform2.t.setX(cameraXform2.t.getX + 1.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown) {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle - 2.0 * ALT_MULTIPLIER)
          }
        case KeyCode.LEFT =>
          if (event.isControlDown && event.isShiftDown) {
            cameraXform2.t.setX(cameraXform2.t.getX - 10.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown && event.isShiftDown) {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle + 10.0 * ALT_MULTIPLIER)
          } else if (event.isControlDown) {
            cameraXform2.t.setX(cameraXform2.t.getX - 1.0 * CONTROL_MULTIPLIER)
          } else if (event.isAltDown) {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle + 2.0 * ALT_MULTIPLIER)
          }
        case _ =>
      }
    }
  }
}
