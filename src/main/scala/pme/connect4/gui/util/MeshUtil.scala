package pme.connect4.gui.util

import java.net.URL

import com.interactivemesh.jfx.importer.stl.StlMeshImporter

import scalafx.scene.shape.{Mesh, TriangleMesh}


object MeshUtil {
  def loadMeshViews(meshFile: String): Mesh =
  {
    val file: URL = Thread.currentThread.getContextClassLoader.getResource(meshFile)
    val importer = new StlMeshImporter
    importer.read(file)
    val mesh = importer.getImport
    new TriangleMesh(mesh)
  }
}
