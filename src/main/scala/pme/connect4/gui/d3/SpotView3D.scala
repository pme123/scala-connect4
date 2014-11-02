package pme.connect4.gui.d3

import javafx.scene.shape.Path

import pme.connect4.domain.Spot
import pme.connect4.gui.SpotView
import pme.connect4.gui.d3.ConnectFourConfig3D._

import scalafx.scene.paint.Color
import scalafx.scene.shape
import scalafx.scene.shape._

object SpotView3D {
  def createSpotView(col:Int, row:Int): javafx.scene.shape.Path =   {
    def createOneField(offset: Int): Path = {
      val path: Path = javafx.scene.shape.Shape.subtract(rect, hole).asInstanceOf[Path]
      path
    }

    def rect = new Rectangle {
      translateX = calcOffsetX(col)-fieldWidth/2
      translateY = calcOffsetY(row)-fieldWidth/2
      width = fieldWidth
      height = fieldWidth
      fill = Color.Blue

    }
    def hole = new Ellipse() {
      centerX = calcOffsetX(col)
      centerY = calcOffsetY(row)
      radiusX = chipRadius
      radiusY = chipRadius
    }

    val shape = Shape.subtract(rect, hole).asInstanceOf[Path]
    shape
  }



  def calcOffsetX(col: Int): Double = {
    gameOffsetX + col * fieldWidth
  }

  def calcOffsetY(row: Int): Double = {
    gameOffsetY +(3 + row) * fieldWidth
  }
}
class SpotView3D(val spot: Spot, val path: javafx.scene.shape.Path) extends shape.Path(path) with SpotView {


  override def getSpot = spot

}
