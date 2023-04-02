import GameProcess.Clock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClockTest {
    @Test
    private void caseOne(){
        Clock clock = new Clock(5,0,30,0);
        System.out.println("Init_plan_sec : " + clock.getInit_plan_sec());
        System.out.println("Plan_rev_sec : " + clock.getPlan_rev_sec());
    }

}