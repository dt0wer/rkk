consul {
  address = "consul:8500"
}

template {
  source = "/etc/consul-template/templates/rkk_lb.conf"
  destination = "/etc/nginx/conf.d/rkk_lb.conf"
  command = "/etc/init.d/nginx reload"
}
