package biz.gsconsulting.play.loadbalancer

import org.specs2.mutable._

import play.api.test.FakeApplication

class LoadBalancerPluginSpec extends Specification {
  "LoadBalancerPlugin.enabled" should {
    "be false if `loadbalancerplugin` is not set in configuration" in {
      val app = FakeApplication()
      val balancerPlugin = new LoadBalancerPlugin(app)
      balancerPlugin.enabled must beFalse
    }

    "be false if `loadbalancerplugin` is set to disabled" in {
      val app = FakeApplication(additionalConfiguration = Map("loadbalancerplugin" -> "disabled"))
      val balancerPlugin = new LoadBalancerPlugin(app)
      balancerPlugin.enabled must beFalse
    }

    "be true if `loadbalancerplugin` is set to enabled" in {
      val app = FakeApplication(additionalConfiguration = Map("loadbalancerplugin" -> "enabled"))
      val balancerPlugin = new LoadBalancerPlugin(app)
      balancerPlugin.enabled must beTrue
    }
  }

  "At startup, the LoadBalancerPlugin" should {
    "create a new LoadBalancer for each configured loadbalancer" in {
      val configuration = Map(
        "loadbalancerplugin" -> "enabled",
        "loadbalancer" -> Map(
          "balancers" -> Map(
            "foo" -> Map()
          )
        ))
      val app = FakeApplication(additionalConfiguration = configuration, additionalPlugins=Seq("loadbalancer"))
      val balancer = new LoadBalancerPlugin(app)
      balancer.onStart

      balancer.balancers must haveKey("foo")
    }
  }
}
