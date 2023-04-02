package GameProcess;

import org.springframework.stereotype.Component;

@Component
public class Clock {
    private int init_plan_sec = 0;
    private int plan_rev_sec = 0;

    public Clock(int init_plan_min, int init_plan_sec, int plan_rev_min, int plan_rev_sec){
        this.init_plan_sec = 60*init_plan_min + init_plan_sec;
        this.plan_rev_sec = 60*plan_rev_min + plan_rev_sec;
    }

    public int getInit_plan_sec() {
        return init_plan_sec;
    }

    public int getPlan_rev_sec() {
        return plan_rev_sec;
    }

    public void setPlan_rev_sec(int plan_rev_sec){
        this.plan_rev_sec = plan_rev_sec;
    }
}
