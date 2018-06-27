import java.io.File

import com.goyeau.kubernetes.client.{KubeConfig, KubernetesClient}
import tech.orkestra.utils.AkkaImplicits._

import scala.io.Source

object Kubernetes {
  val client = KubernetesClient(
    KubeConfig(
      server = "https://kubernetes.default",
      oauthToken = Option(Source.fromFile("/var/run/secrets/kubernetes.io/serviceaccount/token").mkString),
      caCertFile = Option(new File("/var/run/secrets/kubernetes.io/serviceaccount/ca.crt"))
    )
  )
}
