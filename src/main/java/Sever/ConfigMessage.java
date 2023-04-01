package Sever;

import lombok.Getter;

@Getter
public class ConfigMessage {
    private int m;
    private int n;
    private int init_plan_min;
    private int init_plan_sec;
    private int init_budget;
    private int init_center_dep;
    private int plan_rev_min;
    private int plan_rev_sec;
    private int rev_cost;
    private int max_dep;
    private int interest_pct;

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("m : ");
        s.append(m);
        s.append("\nn : ");
        s.append(n);
        s.append("\ninit_plan_min : ");
        s.append(init_plan_min);
        s.append("\ninit_plan_sec : ");
        s.append(init_plan_sec);
        s.append("\ninit_budget : ");
        s.append(init_budget);
        s.append("\ninit_center_dep : ");
        s.append(init_center_dep);
        s.append("\nplan_rev_min : ");
        s.append(plan_rev_min);
        s.append("\nplan_rev_sec : ");
        s.append(plan_rev_sec);
        s.append("\nrev_cost : ");
        s.append(rev_cost);
        s.append("\nmax_dep : ");
        s.append(max_dep);
        s.append("\ninterest_pct : ");
        s.append(interest_pct);
        return s.toString();
    }
}
