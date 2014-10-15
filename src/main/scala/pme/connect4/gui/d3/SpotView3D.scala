package pme.connect4.gui.d3

import pme.connect4.domain.Spot
import pme.connect4.gui.SpotView

import scalafx.scene.Group

class SpotView3D(val spot: Spot) extends Group with SpotView {
  override def getSpot = spot
}
