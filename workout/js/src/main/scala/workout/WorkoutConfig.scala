package workout

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

class WorkoutConfig(routeProvider: RouteProvider) extends Config {
  routeProvider
    .when("/start", Route(templateUrl = "partials/start.html"))
    .when("/finish", Route(templateUrl = "partials/finish.html"))
    .when("/workout", Route(templateUrl = "partials/workout.html", None, Some("WorkoutController"), None))
    .otherwise(Route.redirectTo("/start"))
}