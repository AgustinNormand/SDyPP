consul {
  address = "172.17.0.2:8500"
  retry {
    enabled  = true
    attempts = 12
    backoff  = "250ms"
  }
}

template{
  source      = "./load-balancer.conf.ctmpl"
  destination = "./load-balancer.conf"
  perms       = 0600
}
