package workout

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

@injectable("secondsToTime")
class WorkoutFilter extends Filter[Int] {
  
  import scalajs.js.Math
  
  override def filter(sec: Int) = {
    val hours = Math.floor(sec.toDouble / 3600)
    val minutes = Math.floor((sec - (hours* 3600)).toDouble / 60)
    val seconds = sec - (hours * 3600) - (minutes * 60)
    
    ("0" + hours).substring(("0" + hours).length - 2) + ":" + 
    ("0" + minutes).substring(("0" + minutes).length - 2) + ":" + 
    ("0" + seconds).substring(("0" + seconds).length - 2)
  }
}

@injectable("myLineBreakFilter")
class LineBreakFilter extends Filter[String] {
  override def filter(line: String): String = 
    line.replaceAll("\n", "<br />")
}