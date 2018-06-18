import com.goyeau.orkestra.Dsl._
import com.goyeau.orkestra.board._
import com.goyeau.orkestra.job._
import com.goyeau.orkestra.model._
import com.goyeau.orkestra.parameter._

import scala.concurrent.{Await, Future}
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
