import com.goyeau.kubernetes.client.IntValue
import tech.orkestra.utils.AkkaImplicits._
import io.k8s.api.apps.v1beta2.{StatefulSet, StatefulSetSpec, StatefulSetUpdateStrategy}
import io.k8s.api.core.v1._
import io.k8s.apimachinery.pkg.api.resource.Quantity
import io.k8s.apimachinery.pkg.apis.meta.v1.{LabelSelector, LabelSelectorRequirement, ObjectMeta}

import scala.concurrent.Future

object Elasticsearch {
  val advertisedHostName = "elasticsearch"
  val appElasticsearchLabel = Option(Map("app" -> "elasticsearch"))

  def deploy(environmentName: String): Future[Unit] = for {
    _ <- Kubernetes.client.statefulSets.namespace(environmentName).createOrUpdate(statefulSet)
    _ <- Kubernetes.client.services.namespace(environmentName).createOrUpdate(service)
    _ <- Kubernetes.client.services.namespace(environmentName).createOrUpdate(internalService)
  } yield ()

  val service = Service(
    metadata = Option(ObjectMeta(name = Option(advertisedHostName))),
    spec = Option(
      ServiceSpec(
        selector = appElasticsearchLabel,
        ports = Option(
          Seq(
            ServicePort(name = Option("http"), port = 9200, targetPort = Option(IntValue(9200))),
            ServicePort(name = Option("transport"), port = 9300, targetPort = Option(IntValue(9300)))
          )
        )
      )
    )
  )

  val internalService = Service(
    metadata = Option(ObjectMeta(name = Option("elasticsearch-internal"))),
    spec = Option(
      ServiceSpec(
        selector = appElasticsearchLabel,
        clusterIP = Option("None"),
        ports = Option(Seq(ServicePort(port = 9300, targetPort = Option(IntValue(9300)))))
      )
    )
  )

  val statefulSet = StatefulSet(
    metadata = Option(ObjectMeta(name = service.metadata.get.name)),
    spec = Option(
      StatefulSetSpec(
        selector = Option(LabelSelector(matchLabels = appElasticsearchLabel)),
        serviceName = internalService.metadata.get.name.get,
        replicas = Option(3),
        updateStrategy = Option(StatefulSetUpdateStrategy(`type` = Option("RollingUpdate"))),
        podManagementPolicy = Option("Parallel"),
        template = PodTemplateSpec(
          metadata = Option(ObjectMeta(labels = appElasticsearchLabel)),
          spec = Option(
            PodSpec(
              affinity = Option(
                Affinity(
                  podAntiAffinity = Option(
                    PodAntiAffinity(
                      requiredDuringSchedulingIgnoredDuringExecution = Option(
                        Seq(
                          PodAffinityTerm(
                            labelSelector = Option(
                              LabelSelector(
                                matchExpressions = Option(
                                  Seq(
                                    LabelSelectorRequirement(
                                      key = "app",
                                      operator = "In",
                                      values = Option(Seq("elasticsearch"))
                                    )
                                  )
                                )
                              )
                            ),
                            topologyKey = Option("failure-domain.beta.kubernetes.io/zone")
                          )
                        )
                      )
                    )
                  )
                )
              ),
              containers = Seq(
                Container(
                  name = "elasticsearch",
                  image = Option("docker.elastic.co/elasticsearch/elasticsearch-oss:6.1.1"),
                  env = Option(
                    Seq(
                      EnvVar(name = "cluster.name", value = Option("elasticsearch")),
                      EnvVar(
                        name = "node.name",
                        valueFrom =
                          Option(EnvVarSource(fieldRef = Option(ObjectFieldSelector(fieldPath = "metadata.name"))))
                      ),
                      EnvVar(name = "discovery.zen.ping.unicast.hosts", value = internalService.metadata.get.name),
                      EnvVar(name = "xpack.security.enabled", value = Option(false.toString)),
                      // Index settings
                      EnvVar(name = "indices.query.bool.max_clause_count", value = Option(6144.toString)),
                      // Lock page swapping
                      // EnvVar(name = "bootstrap.memory_lock", value = Option(true.toString))
                    )
                  ),
                  volumeMounts = Option(Seq(VolumeMount(name = "data", mountPath = "/usr/share/elasticsearch/data")))
                )
              ),
              securityContext = Option(PodSecurityContext(runAsUser = Option(1000), fsGroup = Option(1000)))
            )
          )
        ),
        volumeClaimTemplates = Option(
          Seq(
            PersistentVolumeClaim(
              metadata = Option(ObjectMeta(name = Option("data"))),
              spec = Option(
                PersistentVolumeClaimSpec(
                  accessModes = Option(Seq("ReadWriteOnce")),
                  resources = Option(ResourceRequirements(requests = Option(Map("storage" -> Quantity("100Gi")))))
                )
              )
            )
          )
        )
      )
    )
  )
}
