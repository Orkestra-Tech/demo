import com.goyeau.kubernetesclient.{KubeConfig, KubernetesClient}
import com.goyeau.orkestra.utils.AkkaImplicits._

object Kubernetes {
  val client = KubernetesClient(KubeConfig.apply(???))
}
