import tech.orkestra.Dsl._
import tech.orkestra.board._
import tech.orkestra.job._
import tech.orkestra.model._
import tech.orkestra.parameter._
import tech.orkestra.utils.Shells._
import tech.orkestra.github._

object PullRequestChecks {
  lazy val board = JobBoard[GitRef => Unit](JobId("pullRequestChecks"), "Pull Request Checks")(
    Input[GitRef]("Git ref")
  )

  lazy val job = Job(board) { implicit workDir => gitRef =>
    Github.statusUpdated(Repository("myOrganisation/backend"), gitRef) { implicit workDir =>
      sh("./sbt test")
    }
  }
}
