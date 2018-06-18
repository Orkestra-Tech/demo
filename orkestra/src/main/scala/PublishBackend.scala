import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board.JobBoard
import com.goyeau.orkestra.github.GitRef
import com.goyeau.orkestra.job.Job
import com.goyeau.orkestra.model.JobId
import com.goyeau.orkestra.parameter.Input

import scala.concurrent.Await
import scala.concurrent.duration._

object PublishBackend {
  lazy val board = JobBoard[GitRef => String](JobId("publishBackend"), "Publish Backend")(
    Input[GitRef]("Git ref")
  )

  lazy val job = Job(board) { implicit workDir => gitRef =>
    Await.result(Backend.publish(gitRef), 1.hour)
  }
}
