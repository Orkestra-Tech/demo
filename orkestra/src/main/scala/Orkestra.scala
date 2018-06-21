import com.goyeau.orkestra._
import com.goyeau.orkestra.board._
import com.goyeau.orkestra.github._

object Orkestra extends OrkestraServer with GithubHooks {
  lazy val board = Folder("Orkestra")(
    PullRequestChecks.board,
    CreateEnvironment.board,
    PublishBackend.board,
    DeployBackend.board,
    PublishAndDeploy.board
  )

  lazy val jobs = Set(
    PullRequestChecks.job,
    CreateEnvironment.job,
    PublishBackend.job,
    DeployBackend.job,
    PublishAndDeploy.job
  )

  lazy val githubTriggers = Set(
    PullRequestTrigger(Repository("myOrganisation/myRepo"), PullRequestChecks.job)(),
    BranchTrigger(Repository("myOrganisation/myRepo"), "master", PublishAndDeploy.job)(true, "staging")
  )
}
