import io.k8s.api.core.v1.{Namespace => KubeNamespace}
import io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

object Namespace {
  def apply(environmentName: String) =
    KubeNamespace(metadata = Option(ObjectMeta(name = Option(environmentName))))
}
