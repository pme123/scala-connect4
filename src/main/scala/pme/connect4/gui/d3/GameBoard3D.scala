package pme.connect4.gui.d3

import pme.connect4.domain.Chip
import pme.connect4.domain.GameConfig._
import pme.connect4.gui.GameBoard
import pme.connect4.gui.d3.ChipView3D._
import pme.connect4.gui.d3.ConnectFourConfig3D._
import pme.connect4.gui.util.MeshUtil

import scalafx.scene.Group
import scalafx.scene.shape.MeshView
import scalafx.scene.transform.Rotate

class GameBoard3D extends Group with GameBoard[ChipView3D, SpotView3D] {

  override def startNewGame() = {
    super.startNewGame()
    content = chipsToPlay ++ gameSpots.values  ++ createPillars ++ createClap
  }


  protected def createChip(col: Int, chip:Chip): ChipView3D = {
     new ChipView3D(chip) {
      translateX = SpotView3D.calcOffsetX(col)
    }
  }
  protected def createSpot(col: Int, row: Int): SpotView3D = {
    new SpotView3D(fourConnect.game.slots(col).spots(row))
  }

   protected def changeMaterial(chip: ChipView3D): Unit = chip.material = materialMap(activeChip)

  protected def addNewChipView(newChip: ChipView3D): Unit = content.add(newChip)

  protected def dropHeight(spotView: SpotView3D): Double =  -(rows-spotView.getSpot.row)*fieldWidth

  private def createPillars(): Array[MeshView] = {
    def loadMeshView(translateXDirection:Int): MeshView =
    {
      val mesh = MeshUtil.loadMeshViews(pillarMeshFile)
      new MeshView(mesh){
        translateX = translateXDirection*(cols/2*fieldWidth+fieldWidth/2)
        translateY = -9
        scaleX = 20
        scaleY = 28
        scaleZ = 28
        rotationAxis = Rotate.XAxis
        rotate = -90.0
        material=gameMaterial
      }
    }
    Array[MeshView](loadMeshView(1),loadMeshView(-1))
  }

  private def createClap(): Array[MeshView] = {
    def loadMeshView(): MeshView =
    {
      val mesh = MeshUtil.loadMeshViews(clapMeshFile)
      new MeshView(mesh){
      //  translateX = (cols/2*fieldWidth+fieldWidth/2)
        translateY = -(rows/2*fieldWidth)
        scaleX = 9.7
        scaleY = 9
        scaleZ = 14
        rotationAxis = Rotate.XAxis
        rotate = 90.0
        material=gameMaterial
      }
    }
    Array[MeshView](loadMeshView() )
  }

}
