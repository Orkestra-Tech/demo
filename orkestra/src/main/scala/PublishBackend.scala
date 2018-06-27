import tech.orkestra.Dsl._
import tech.orkestra.board.JobBoard
import tech.orkestra.github.GitRef
import tech.orkestra.job.Job
import tech.orkestra.model.JobId
import tech.orkestra.parameter.{Checkbox, Input}

import scala.concurrent.Await
import scala.concurrent.duration._

object PublishBackend {
  lazy val board = JobBoard[(GitRef, Boolean) => String](JobId("publishBackend"), "Publish Backend")(
    Input[GitRef]("Git ref"),
    Checkbox("Run checks")
  )

  lazy val job = Job(board) { implicit workDir => (gitRef, runChecks) =>
    Await.result(Backend.publish(gitRef, runChecks), 1.hour)
  }
}
