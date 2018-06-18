import com.goyeau.orkestra.github.GitRef

import scala.concurrent.Future

object Backend {
  def deploy(version: String, environmentName: String): Future[Unit] = ???

  def publish(gitRef: GitRef): Future[String] = ???
}
