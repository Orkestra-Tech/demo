import tech.orkestra.Dsl._
import tech.orkestra.board.JobBoard
import tech.orkestra.github.GitRef
import tech.orkestra.job.Job
import tech.orkestra.model.JobId
import tech.orkestra.parameter.{Checkbox, Input}
import tech.orkestra.utils.Triggers._

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
