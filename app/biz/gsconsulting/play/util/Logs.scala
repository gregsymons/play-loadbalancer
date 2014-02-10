package biz.gsconsulting.play.util

import play.api.Logger

trait Logs {
  lazy val logger = Logger(this.getClass)
}
