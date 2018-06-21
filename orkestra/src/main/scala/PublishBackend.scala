import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board.JobBoard
import com.goyeau.orkestra.github.GitRef
import com.goyeau.orkestra.job.Job
import com.goyeau.orkestra.model.JobId
import com.goyeau.orkestra.parameter.{Checkbox, Input}

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
