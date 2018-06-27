import tech.orkestra.Dsl._
import tech.orkestra.board._
import tech.orkestra.job._
import tech.orkestra.model._
import tech.orkestra.parameter._

import scala.concurrent.Await
import scala.concurrent.duration._

object CreateEnvironment {
  lazy val board = JobBoard[String => Unit](JobId("createEnvironment"), "Create Environment")(
    Input[String]("Environment name")
  )

  lazy val job = Job(board) { implicit workDir => environmentName =>
    Await.result(for {
      _ <- Kubernetes.client.namespaces.createOrUpdate(Namespace(environmentName))
      _ <- Elasticsearch.deploy(environmentName)
    } yield (), 1.minute)
  }
}
