import com.thoughtworks.gauge.Step
import org.assertj.core.api.Assertions.assertThat

class StepTest {

    @Step("new step")
    fun newstep() {
        assertThat(2).isEqualTo(2)
    }
}