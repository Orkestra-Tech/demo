# Orkestra Demo

This repository is the complete project for the article "Functional DevOps with Scala and Kubernetes":
https://itnext.io/functional-devops-with-scala-a-kubernetes-3d7c91bca72f

This demo is a complete setup using Orkestra for the CI and CD.

See documentation:
https://orkestracd.github.io/orkestra

## Usage

```
minikube start                            # Start Minikube
eval `minikube docker-env`                # Make docker use the docker engine of Minikube
sbt orkestraJVM/Docker/publishLocal       # Publish the docker artifact
kubectl apply -f kubernetes/              # Apply the deployment to Kubernetes
kubectl proxy                             # Proxy the Kubernetes api
```
Visit Orkestra on `http://127.0.0.1:8001/api/v1/namespaces/orkestra/services/orkestra:http/proxy`.  
You can troubleshoot any deployment issue with `minikube dashboard`.
