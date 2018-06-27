import tech.orkestra.Dsl._
import tech.orkestra.board.JobBoard
import tech.orkestra.job.Job
import tech.orkestra.model.JobId
import tech.orkestra.parameter.Input

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object CopyData {
  lazy val board = JobBoard[(String, String) => Unit](JobId("copyData"), "Copy Data")(
    Input[String]("Source environment"),
    Input[String]("Destination environment")
  )

  lazy val job = Job(board) { implicit workDir => (source, destination) =>
    Await.result(copyData(source, destination), 1.minute)
  }

  // This should probably implemented in the backend repo and
  def copyData(source: String, destination: String): Future[Unit] = ???
}
