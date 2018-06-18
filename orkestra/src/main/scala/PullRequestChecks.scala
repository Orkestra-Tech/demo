import com.goyeau.orkestra._
import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board._
import com.goyeau.orkestra.job._
import com.goyeau.orkestra.model._
import com.goyeau.orkestra.parameter._
import com.goyeau.orkestra.utils.Shells._
import com.goyeau.orkestra.github._

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
