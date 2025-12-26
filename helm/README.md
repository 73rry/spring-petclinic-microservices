# Petclinic Helm Chart

This chart deploys the Spring Petclinic microservices to Kubernetes using Deployments and NodePort Services.

## Prerequisites
- Kubernetes cluster access (`kubectl` configured)
- Helm v3 installed locally
- Container images available (defaults to `springcommunity/*`)

## Install
```bash
helm install petclinic ./helm/petclinic
```

## Upgrade
```bash
helm upgrade petclinic ./helm/petclinic
```

## Uninstall
```bash
helm uninstall petclinic
```

## Configuration
Edit `values.yaml` to enable/disable services, set image tags, ports, and NodePort numbers.

Key settings:
- `global.imagePrefix`: registry/repository prefix (default `springcommunity`)
- `global.tag`: image tag (default `latest`)
- `services.<name>.port`: container/service port
- `services.<name>.nodePort`: NodePort (30000-32767)
- `services.genai-service.env`: set AI-related environment variables if required

## Notes
- All Services are `NodePort`. Adjust `nodePort` values to avoid collisions.
- For production, consider Ingress or LoadBalancer for `api-gateway`.
