package pme.connect4.gui.d2

import pme.connect4.domain.Spot
import pme.connect4.gui.SpotView

import scalafx.scene.shape.Path

class SpotView2D(val spot: Spot, path: javafx.scene.shape.Path) extends Path(path) with SpotView {
  override def getSpot = spot

}
