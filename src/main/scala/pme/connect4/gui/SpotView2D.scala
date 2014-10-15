package pme.connect4.gui

import pme.connect4.domain.Spot

import scalafx.scene.shape.Path

class SpotView2D(val spot: Spot, path: javafx.scene.shape.Path) extends Path(path) with SpotView {
  override def getSpot = spot

}
