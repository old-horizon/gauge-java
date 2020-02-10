import com.thoughtworks.gauge.Step
import org.assertj.core.api.Assertions

class StepTest {

    @Step("new step", "another step")
    fun newstep() {
        Assertions.assertThat(2).isEqualTo(2)
    }
}