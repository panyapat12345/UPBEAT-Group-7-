package GameProcess.Display;

import org.springframework.stereotype.Component;

@Component
public class DisplayPlan {
    private int index;
    private String plan;

    public DisplayPlan(int index, String plan){
        this.index = index;
        this.plan = plan;
    }
}
