package GameProcess;

import AST.PlanTree;
import GameProcess.Display.DisplayPlayer;
import GameProcess.Display.DisplayRegion;

public class GameSystem {
    private internalOperator game;

    public GameSystem(double m, double n, double init_plan_min, double init_plan_sec, double init_budget, double init_center_dep, double plan_rev_min, double plan_rev_sec, double rev_cost, double max_dep, double interest_pct){
        game = new internalOperator(m, n, init_plan_min, init_plan_sec, init_budget, init_center_dep, plan_rev_min, plan_rev_sec, rev_cost, max_dep, interest_pct);
    }

    public boolean isCorrectSyntax(String constructionPlan){
        try{ new PlanTree(constructionPlan); }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean addPlayer(String constructionPlan){
        try{
            game.addPlayer(constructionPlan);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public void NextTurn(){
        game.NextTurn();
    }

    public DisplayPlayer[] getAllPlayers(){
        return game.getAllPlayers();
    }

    public DisplayPlayer getCurrentPlayer(){
        return game.getCurrentPlayer();
    }

    public DisplayRegion[][] getAllTerritory(){
        return game.getAllTerritory();
    }

    public DisplayRegion[][] getCurrentTerritory(){
        return game.getCurrentTerritory();
    }
}
