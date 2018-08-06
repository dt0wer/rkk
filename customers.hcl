consul {
  address = "consul:8500"
}

template {
  source = "/etc/consul-template/templates/customers.conf"
  destination = "/etc/nginx/conf.d/customers.conf"
  command = "/etc/init.d/nginx reload"
}
