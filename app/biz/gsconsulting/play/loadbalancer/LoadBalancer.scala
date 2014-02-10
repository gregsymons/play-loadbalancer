package biz.gsconsulting.play.loadbalancer

import play.api._

case class LoadBalancer(name: String)

object LoadBalancer {
  def fromConfiguration(name: String, config: Configuration): LoadBalancer = {
    LoadBalancer(name)
  }
}
