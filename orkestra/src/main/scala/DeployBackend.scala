import tech.orkestra.Dsl._
import tech.orkestra.board.JobBoard
import tech.orkestra.job.Job
import tech.orkestra.model.JobId
import tech.orkestra.parameter.Input

import scala.concurrent.Await
import scala.concurrent.duration._

object DeployBackend {
  lazy val board = JobBoard[(String, String) => Unit](JobId("deployBackend"), "Deploy Backend")(
    Input[String]("Version"),
    Input[String]("Environment name")
  )

  lazy val job = Job(board) { implicit workDir => (version, environmentName) =>
    Await.result(Backend.deploy(version, environmentName), 1.minute)
  }
}
