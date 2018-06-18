import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board.JobBoard
import com.goyeau.orkestra.job.Job
import com.goyeau.orkestra.model.JobId
import com.goyeau.orkestra.parameter.Input

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
