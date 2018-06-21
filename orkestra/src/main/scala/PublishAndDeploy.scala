import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board.JobBoard
import com.goyeau.orkestra.github.GitRef
import com.goyeau.orkestra.job.Job
import com.goyeau.orkestra.model.JobId
import com.goyeau.orkestra.parameter.{Checkbox, Input}
import com.goyeau.orkestra.utils.Triggers._

import scala.concurrent.Await
import scala.concurrent.duration._

object PublishAndDeploy {
  lazy val board =
    JobBoard[(GitRef, Boolean, String) => Unit](
      JobId("publishAndDeployBackend"),
      "Publish and Deploy Backend"
    )(
      Input[GitRef]("Git ref"),
      Checkbox("Run checks"),
      Input[String]("Environment name")
    )

  lazy val job = Job(board) {
    implicit workDir => (gitRef, runChecks, environmentName) =>
      Await.result(for {
        version <- PublishBackend.job.run(gitRef, runChecks)
        _ <- DeployBackend.job.run(version, environmentName)
      } yield (), 1.hour)
  }
}
