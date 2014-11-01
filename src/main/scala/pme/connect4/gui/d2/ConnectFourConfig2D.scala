package pme.connect4.gui.d2

import pme.connect4.gui.ConnectFourConfig

object ConnectFourConfig2D {
  protected[d2] val paneSize = (800, 880)
  protected[d2] val gameSize= (784.0, 842.0)
  protected[d2] val chipRadius = 40
  protected[d2] val fieldWidth = ConnectFourConfig2D().fieldWidth

  def apply() = new ConnectFourConfig2D
}
class ConnectFourConfig2D extends ConnectFourConfig {


  protected def chipRadius = ConnectFourConfig2D.chipRadius

}
