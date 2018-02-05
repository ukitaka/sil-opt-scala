import me.waft.parser.Comment
import org.scalatest._

class CommentParserSpec extends FlatSpec with Matchers {
  "Comment parser" should "work well" in {
    val sil = "// here is comment\n"
    val result = Comment.comment.parse(sil).get.value
    result should be ()
  }
}
