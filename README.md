# Server side load balancing for customer service nginx + consul temlate (main)
http://localhost:9090/home
replace home on url from source code.

Based on:
https://medium.com/@ladislavGazo/easy-routing-and-service-discovery-with-docker-consul-and-nginx-acfd48e1a291
https://github.com/seges/docker-nginx-consul
https://github.com/hashicorp/consul-template
https://github.com/hashicorp/consul-template/blob/master/examples/nginx.md
https://habr.com/post/262397/

Load balancing strategies:

https://www.hashicorp.com/blog/load-balancing-strategies-for-consul


# Consul tool
http://localhost:8500

Documentation:
https://cloud.spring.io/spring-cloud-static/spring-cloud-consul/1.3.4.RELEASE/multi/multi_spring-cloud-consul.html

https://projects.spring.io/spring-cloud/spring-cloud.html#_circuit_breaker_hystrix_clients

https://github.com/spring-cloud/spring-cloud-consul/blob/master/docs/src/main/asciidoc/spring-cloud-consul.adoc

ACL
https://www.consul.io/docs/guides/acl.html

https://www.consul.io/docs/agent/options.html

# Git2consul
https://github.com/Cimpress-MCP/docker-git2consul


# Fabio server side load balancig (not main)
http://localhost:9998 (roating table)
http://localhost:9999/home (request)

Based on:
https://github.com/fabiolb/fabio

# Vault
https://cloud.spring.io/spring-cloud-vault/spring-cloud-vault-config.html

Example
https://spring.io/guides/gs/vault-config/

Deploy
https://www.vaultproject.io/intro/getting-started/deploy.html

# Run it
docker-compose up 
