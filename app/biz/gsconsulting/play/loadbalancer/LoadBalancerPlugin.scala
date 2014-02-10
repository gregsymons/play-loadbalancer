package biz.gsconsulting.play.loadbalancer

import scala.collection.JavaConversions._
import play.api._

import biz.gsconsulting.play.util._

class LoadBalancerPlugin(app: Application) extends Plugin 
  with Logs{

  private[this] var unsafeLoadBalancers: Option[Map[String, LoadBalancer]] = None

  def balancers() = unsafeLoadBalancers getOrElse Map()
  def configuration() = app.configuration.getConfig("loadbalancer")

  override def enabled() = {
    app.configuration.getString("loadbalancerplugin").filter(_ != "disabled").isDefined
  }

  override def onStart() = {
    if (enabled) {
      logger.info("Load Balancer Plugin Enabled.")

      unsafeLoadBalancers = for {
        config <- configuration
        balancers <- config.getConfig("balancers")
        names = balancers.subKeys
      } yield names.foldLeft(Map[String, LoadBalancer]()) {
        (acc: Map[String, LoadBalancer], name: String) =>
          logger.info(s"Creating load balancer '$name'")
          acc.updated(name, LoadBalancer.fromConfiguration(name, config))
      }

    }
  }
}
