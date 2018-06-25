import com.goyeau.orkestra._
import com.goyeau.orkestra.board._
import com.goyeau.orkestra.cron.{CronTrigger, CronTriggers}
import com.goyeau.orkestra.github._

object Orkestra extends OrkestraServer with GithubHooks with CronTriggers {
  lazy val board = Folder("Orkestra")(
    PullRequestChecks.board,
    CreateEnvironment.board,
    PublishBackend.board,
    DeployBackend.board,
    PublishAndDeploy.board,
    CopyData.board
  )

  lazy val jobs = Set(
    PullRequestChecks.job,
    CreateEnvironment.job,
    PublishBackend.job,
    DeployBackend.job,
    PublishAndDeploy.job,
    CopyData.job
  )

  lazy val githubTriggers = Set(
    PullRequestTrigger(Repository("myOrganisation/myRepo"), PullRequestChecks.job)(),
    BranchTrigger(Repository("myOrganisation/myRepo"), "master", PublishAndDeploy.job)(true, "staging")
  )

  lazy val cronTriggers = Set(
//    CronTrigger("0 5 * * 1", CopyData.job)("prod", "staging")
  )
}
