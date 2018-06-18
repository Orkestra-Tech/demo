import com.goyeau.orkestra._
import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board._
import com.goyeau.orkestra.job._
import com.goyeau.orkestra.model._
import com.goyeau.orkestra.github._

object Orkestra extends OrkestraServer with GithubHooks {
  lazy val board = Folder("Orkestra")(PullRequestChecks.board)
  lazy val jobs = Set(PullRequestChecks.job)

  lazy val githubTriggers = Set(
    PullRequestTrigger(Repository("myOrganisation/myRepo"), PullRequestChecks.job)()
  )
}
