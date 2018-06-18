import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board.JobBoard
import com.goyeau.orkestra.github.GitRef
import com.goyeau.orkestra.job.Job
import com.goyeau.orkestra.model.JobId
import com.goyeau.orkestra.parameter.Input
import com.goyeau.orkestra.utils.Triggers._

import scala.concurrent.Await
import scala.concurrent.duration._

object PublishAndDeploy {

  def board =
    JobBoard[(GitRef, String) => Unit](
      JobId(s"publishAndDeployBackend"),
      s"Publish and Deploy Backend"
    )(
      Input[GitRef]("Git ref"),
      Input[String]("Environment name")
    )

  def job = Job(board) { implicit workDir => (gitRef, environmentName) =>
    Await.result(
      for {
        version <- PublishBackend.job.run(gitRef)
        _ <- DeployBackend.job.run(version, environmentName)
      } yield (),
      2.hours
    )
  }
}
