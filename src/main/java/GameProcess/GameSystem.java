package GameProcess;

import AST.Action;
import AST.PlanTree;
import AST.Tree;
import GameProcess.Display.DisplayGameSystem;
import GameProcess.Display.DisplayPlayer;
import GameProcess.Display.DisplayRegion;
import Exception.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GameSystem {
    private internalOperator game;
    private LinkedList<Tree> constructionPlans = new LinkedList<>();
    private int totalPlayers = 0;
    private int totalTurn = 0;
    private Iterator<Action.FinalActionState> currentPlan;

    public GameSystem(double m, double n, double init_plan_min, double init_plan_sec, double init_budget, double init_center_dep, double plan_rev_min, double plan_rev_sec, double rev_cost, double max_dep, double interest_pct){
        game = new internalOperator(m, n, init_plan_min, init_plan_sec, init_budget, init_center_dep, plan_rev_min, plan_rev_sec, rev_cost, max_dep, interest_pct);
    }

    public GameSystem(HashMap<String, Double> configVals){
        game = new internalOperator(configVals);
    }

    public boolean isCorrectSyntax(String constructionPlan){
        try{ new PlanTree(constructionPlan); }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean addPlayer(String constructionPlan){
        // parsing
        Tree tree = null;
        try{ tree = new PlanTree(constructionPlan); }
        catch (Exception e) {
            return false;
        }
        constructionPlans.add(tree);
        game.addPlayer(constructionPlan);
        totalPlayers++;
        return true;
    }

    public void nextTurn() throws WonException{
        totalTurn++;
        game.NextTurn();
        currentPlan =  constructionPlans.get((totalTurn-1) %totalPlayers).iteratorRealTime();
/*
        Action.FinalActionState currentAction;
        while(true){
            currentAction = currentPlan.next();
            if (currentAction == null) break;
            try {
                System.out.println(currentAction);
                game.actionProcess(currentAction);
            } catch (DoneExecuteException e) {
                return;
            }
        }
 */
    }

    public void nextAction() throws DoneExecuteException{
        Action.FinalActionState currentAction = currentPlan.next();
        if (currentAction == null) throw new DoneExecuteException("Done");
        System.out.println(currentAction);
        game.actionProcess(currentAction);
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

    public DisplayGameSystem getAllGameSystem(){
        return new DisplayGameSystem(getAllPlayers(), getAllTerritory());
    }

    public DisplayGameSystem getCurrentGameSystem(){
        return new DisplayGameSystem(new DisplayPlayer[]{game.getCurrentPlayer()}, game.getCurrentTerritory());
    }
}
