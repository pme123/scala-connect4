package pme.connect4.gui

object GuiGameConfig {
  val paneSize = (800, 800)
  val gameSize= (784.0, 762.0)

  val paneOffsetX = 40
  val paneOffsetY = 80
  val horFieldCount: Int = 7
  val verFieldCount: Int = 6
  val slotMargin = 2
  val boardWidth = gameSize._1 - paneOffsetX * 2
  val boardHeight = gameSize._2 - paneOffsetY * 2
  val fieldWidth = boardWidth / horFieldCount
  val fieldHeight = boardHeight / verFieldCount
}
