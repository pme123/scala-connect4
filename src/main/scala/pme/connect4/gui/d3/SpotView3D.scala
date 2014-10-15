package pme.connect4.gui.d3

import pme.connect4.domain.Spot
import pme.connect4.gui.SpotView

import scalafx.scene.Group

class SpotView3D(val spot: Spot) extends Group with SpotView {
  self =>

  def blink {
    /* val  animation = new FadeTransition{
       duration = Duration(500)
       fromValue = 1.0
       toValue = 0.0
       cycleCount = Timeline.Indefinite
       autoReverse = true
       node = self
     }
     animation.play();*/
  }

}
