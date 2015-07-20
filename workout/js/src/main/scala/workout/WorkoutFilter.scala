package workout

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

@injectable("secondsToTime")
class WorkoutFilter extends Filter[String] {
  
  import scalajs.js.Math
  
  override def filter(input: String) = {
    try {
      val sec = input.toInt
      val hours = Math.floor(sec.toDouble / 3600)
      val minutes = Math.floor((sec - (hours* 3600)).toDouble / 60)
      val seconds = sec - (hours * 3600) - (minutes * 60)
      
      ("0" + hours).substring(("0" + hours).length - 2) + ":" + 
      ("0" + minutes).substring(("0" + minutes).length - 2) + ":" + 
      ("0" + seconds).substring(("0" + seconds).length - 2)
    } catch {
      case _: java.lang.NumberFormatException =>
        "00:00:00"
    }
  }
}