package pme.connect4.gui.d3

import javafx.scene.shape
import pme.connect4.domain.GameConfig._
import pme.connect4.domain.Spot
import pme.connect4.gui.d3.ConnectFourConfig3D._
import pme.connect4.gui.{SpotView2D, SpotView}

import scala.collection.immutable.IndexedSeq
import scalafx.scene.transform.Translate
import scalafx.scene.{Node, Group}
import scalafx.scene.paint.Color
import scalafx.scene.shape._

object SpotView3D {
  def createSpotView(col:Int, row:Int): javafx.scene.shape.Path =   {
    def createOneField(offset: Int): shape.Path = {
      val path: shape.Path = javafx.scene.shape.Shape.subtract(rect, hole).asInstanceOf[shape.Path]
      path.setTranslateZ(chipThickness * offset)
      path
    }

    def rect = new Rectangle {
      translateX = calcOffsetX(col)-fieldWidth/2
      translateY = calcOffsetY(row)-fieldWidth/2
      width = fieldWidth
      height = fieldHeight
      fill = Color.Blue

    }
    def hole = new Ellipse() {
      centerX = calcOffsetX(col)
      centerY = calcOffsetY(row)
      radiusX = chipRadius
      radiusY = chipRadius
    }

    val paths: IndexedSeq[shape.Shape] = for(i <- -1 to 1)yield createOneField(i)
    paths.foldRight(Sh)
    javafx.scene.shape.Shape.union(paths)
    path
  }



  def calcOffsetX(col: Int): Double = {
    gameOffsetX + col * fieldWidth
  }

  def calcOffsetY(row: Int): Double = {
    gameOffsetY +(3 + row) * fieldHeight
  }
}
class SpotView3D(val spot: Spot, val path: javafx.scene.shape.Path) extends Path(path) with SpotView {


  override def getSpot = spot






  /*
 Let the radius from the center of the hole to the center of the torus tube be "c",
 and the radius of the tube be "a".
 Then the equation in Cartesian coordinates for a torus azimuthally symmetric about the z-axis is
 (c-sqrt(x^2+y^2))^2+z^2=a^2
 and the parametric equations are
 x = (c + a * cos(v)) * cos(u)
 y = (c + a * cos(v)) * sin(u)
 z =  a * sin(v)
 (for u,v in [0,2pi).

 Three types of torus, known as the standard tori, are possible,
 depending on the relative sizes of a and c. c>a corresponds to the ring torus (shown above),
 c=a corresponds to a horn torus which is tangent to itself at the point (0, 0, 0),
 and c<a corresponds to a self-intersecting spindle torus (Pinkall 1986).
 */
  def createToroidMesh() = {
    val POINT_SIZE = 3
    val TEXCOORD_SIZE = 2
    val FACE_SIZE = 6
    val tubeDivisions = 23
    val radiusDivisions = 33
    val numVerts = tubeDivisions * radiusDivisions
    val radius: Float = 23
    val tRadius: Float = 30
    val faceCount = numVerts * 2
    var smoothingGroups: Array[Int] = null
    val points = new Array[Float](numVerts * POINT_SIZE)
    val texCoords = new Array[Float](numVerts * TEXCOORD_SIZE)
    val faces = new Array[Int](faceCount * FACE_SIZE)
    var pointIndex = 0
    var texIndex = 0
    var faceIndex = 0
    var smoothIndex = 0
    val tubeFraction = 1.0f / tubeDivisions
    val radiusFraction = 1.0f / radiusDivisions

    var p0 = 0
    var p1 = 0
    var p2 = 0
    var p3 = 0
    var t0 = 0
    var t1 = 0
    var t2 = 0
    var t3 = 0

    // create points
    for (tubeIndex <- 0 until tubeDivisions) {

      val radian: Double = tubeFraction * tubeIndex * 2.0f * 3.141592653589793f

      for (radiusIndex <- 0 until radiusDivisions) {

        val localRadian = radiusFraction * radiusIndex * 2.0f * 3.141592653589793f

        points(pointIndex) = ((radius + tRadius * Math.cos(radian)) * Math.cos(localRadian)).toFloat
        points(pointIndex + 1) = ((radius + tRadius * Math.cos(radian)) * Math.sin(localRadian)).toFloat
        points(pointIndex + 2) = (tRadius * Math.sin(radian)).toFloat

        pointIndex += 3

        val r = if (radiusIndex < tubeDivisions) tubeFraction * radiusIndex * 2.0F * 3.141592653589793f else 0.0f
        texCoords(texIndex) = (0.5 + Math.sin(r) * 0.5).toFloat
        texCoords(texIndex + 1) = (Math.cos(r) * 0.5 + 0.5).toFloat

        texIndex += 2

      }

    }
    //create faces
    for (point <- 0 until tubeDivisions) {
      for (crossSection <- 0 until radiusDivisions) {
        p0 = point * radiusDivisions + crossSection
        p1 = if (p0 >= 0) p0 + 1 else p0 - radiusDivisions
        p1 = if (p1 % radiusDivisions != 0) p0 + 1 else p0 - (radiusDivisions - 1)
        p2 = if (p0 + radiusDivisions < tubeDivisions * radiusDivisions) p0 + radiusDivisions else p0 - tubeDivisions * radiusDivisions + radiusDivisions
        p3 = if (p2 < ((tubeDivisions * radiusDivisions) - 1)) p2 + 1 else p2 - (tubeDivisions * radiusDivisions) + 1
        p3 = if (p3 % radiusDivisions != 0) p2 + 1 else p2 - (radiusDivisions - 1)

        t0 = point * radiusDivisions + crossSection
        t1 = if (t0 >= 0) t0 + 1 else t0 - radiusDivisions
        t1 = if (t1 % radiusDivisions != 0) t0 + 1 else t0 - (radiusDivisions - 1)
        t2 = if (t0 + radiusDivisions < tubeDivisions * radiusDivisions) t0 + radiusDivisions else t0 - tubeDivisions * radiusDivisions + radiusDivisions
        t3 = if (t2 < tubeDivisions * radiusDivisions - 1) t2 + 1 else t2 - (tubeDivisions * radiusDivisions) + 1
        t3 = if (t3 % radiusDivisions != 0) t2 + 1 else t2 - (radiusDivisions - 1)

        try {
          faces(faceIndex) = p2
          faces(faceIndex + 1) = t3
          faces(faceIndex + 2) = p0
          faces(faceIndex + 3) = t2
          faces(faceIndex + 4) = p1
          faces(faceIndex + 5) = t0

          faceIndex += FACE_SIZE

          faces(faceIndex) = p2
          faces(faceIndex + 1) = t3
          faces(faceIndex + 2) = p1
          faces(faceIndex + 3) = t0
          faces(faceIndex + 4) = p3
          faces(faceIndex + 5) = t1
          faceIndex += FACE_SIZE
        } catch {
          case e: Exception => e.printStackTrace()
        }
      }
    }
    val localTriangleMesh: TriangleMesh = new TriangleMesh()
    localTriangleMesh.points = points
    localTriangleMesh.texCoords = texCoords
    localTriangleMesh.faces = faces
    localTriangleMesh
  }
}
