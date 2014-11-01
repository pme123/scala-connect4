package pme.connect4.gui.d2

import pme.connect4.domain.GameConfig._

object ConnectFourConfig2D {

  protected[d2] val paneSize = (800, 880)
  protected[d2] val gameSize= (784.0, 842.0)

  protected[d2] val paneOffsetX = 40
  protected[d2] val paneOffsetY = 120
  protected[d2]  val slotMargin = 2
  protected[d2]  val boardWidth = gameSize._1 - paneOffsetX * 2
  protected[d2]  val boardHeight = gameSize._2 - paneOffsetY * 2
  protected[d2] val fieldWidth = boardWidth / cols
  protected[d2] val fieldHeight = boardHeight / cols
}
