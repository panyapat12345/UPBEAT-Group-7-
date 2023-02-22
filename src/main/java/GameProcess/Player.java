package GameProcess;
import AST.Action;

import java.util.HashMap;

interface PlayerInterface{
    void ChangeConstructionPlan(Action newPlan);
    void IsDefeat();
    CityCrew GetCityCrewInfo();
}

class Player implements PlayerInterface{
    private int PlayerIndex;
    private Action ConstructionPlan;
    private HashMap<String, Double> VariablesOfConstructionPlan;
    private int CityCenterPositionM;
    private int CityCenterPositionN;
    CityCrew crew = null;

    Player(int Id, Action constructionPlan){
        PlayerIndex = Id;
        ConstructionPlan = constructionPlan;
        VariablesOfConstructionPlan = new HashMap<>();
    }

    public void ChangeConstructionPlan(Action newPlan){ this.ConstructionPlan = newPlan; }
    @Override
    public void IsDefeat() {

    }

    @Override
    public CityCrew GetCityCrewInfo() {
        return null;
    }

    private void NewCityCrew(){
        crew = new CityCrew(CityCenterPositionM, CityCenterPositionN);
    }
    public String NextConstructionPlanNode(){
        return "EndTurn";
    }
}

class CityCrew {
    private int PositionM;
    private int PositionN;
    Double BudGet;

    CityCrew(int PositionN, int PositionM) {
        this.PositionM = PositionM;
        this.PositionN = PositionN;
    }

    public int currow() {
        return PositionN;
    }
    public int curcol() {
        return PositionM;
    }
}