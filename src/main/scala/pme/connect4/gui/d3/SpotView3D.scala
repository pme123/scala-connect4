package pme.connect4.gui.d3

import pme.connect4.domain.Spot
import pme.connect4.gui.SpotView
import pme.connect4.gui.d3.ConnectFourConfig3D._
import pme.connect4.gui.util.MeshUtil

import scalafx.Includes._
import scalafx.animation.Timeline
import scalafx.animation.Timeline.Indefinite
import scalafx.scene.shape._
import scalafx.scene.transform.Rotate

object SpotView3D {
  def calcOffsetX(col: Int): Double = {
    gameOffsetX + col * fieldWidth
  }

  def calcOffsetY(row: Int): Double = {
    gameOffsetY + (3 + row) * fieldWidth
  }
}

class SpotView3D(val spot: Spot) extends MeshView with SpotView {
  self =>
  import pme.connect4.gui.d3.SpotView3D._

  mesh = MeshUtil.loadMeshViews(spotMeshFile)
  translateX = calcOffsetX(spot.col)
  translateY = calcOffsetY(spot.row)
  scaleX = 14
  scaleY = 14
  scaleZ = 14
  rotationAxis = Rotate.XAxis
  rotate = 90.0
  material = gameMaterial

  def getSpot = spot



  override def blink() {
    new Timeline() {
      cycleCount = Indefinite
      autoReverse = true
      keyFrames = Seq(
        at(0 s) {
          material -> groundMaterial
        },
        at(0.5 s) {
          material -> gameMaterial
        } ,
        at(1 s) {
          material -> groundMaterial
        }
      )
    }.play()

  }
}
