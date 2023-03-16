package GameProcess;

import AST.Action;
import AST.PlanTree;
import AST.Tree;
import GameProcess.Display.DisplayPlan;
import GameProcess.Display.DisplayGameSystem;
import GameProcess.Display.DisplayPlayer;
import GameProcess.Display.DisplayRegion;
import Exception.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GameSystem {
    private internalOperator game;
    private int maxPlayer = 2;
    private int totalPlayers = 0;
//    private int totalTurn = 0;
    private Tree[] constructionPlans = new Tree[maxPlayer];
    private String[] buffers = new String[maxPlayer];
    private Iterator<Action.FinalActionState> currentPlan;
    private Action.FinalActionState currentAction;

    public GameSystem(double m, double n, double init_plan_min, double init_plan_sec, double init_budget, double init_center_dep, double plan_rev_min, double plan_rev_sec, double rev_cost, double max_dep, double interest_pct){
        game = new internalOperator(m, n, init_plan_min, init_plan_sec, init_budget, init_center_dep, plan_rev_min, plan_rev_sec, rev_cost, max_dep, interest_pct);
    }

    public GameSystem(HashMap<String, Double> configVals){
        game = new internalOperator(configVals);
    }

    public static boolean isCorrectSyntax(String constructionPlan){
        try{ new PlanTree(constructionPlan); }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public void addBuffer(int index, String constructionPlan){
        if(index >= 0 && index <= 1)
            buffers[index] = constructionPlan;
    }

    public boolean addPlayer(String constructionPlan){
        // parsing
        Tree tree = null;
        try{ tree = new PlanTree(constructionPlan); }
        catch (Exception e) {
            return false;
        }
        totalPlayers++;
        constructionPlans[totalPlayers-1] = tree;
        game.addPlayer(constructionPlan);
        return true;
    }

    private void start(){
        for (String buffer : buffers){
            addPlayer(buffer);
        }
        try{
            game.NextTurn();
        } catch(WonException e){
            return;
        }
    }

    public boolean nextTurn() throws WonException{
        try {
//            totalTurn++;
            game.NextTurn();
            currentPlan =  constructionPlans[game.getIndexCurrentPlayer()].iteratorRealTime();
        } catch (WonException e){
            return true;
        }
        return false;
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

    public void nextAction() throws DoneExecuteException, WonException{
        Action.FinalActionState currentAction = currentPlan.next();
        this.currentAction = currentAction;
        if (currentAction == null){
            this.currentAction = new Action(null, "done", null).getFinalAction(null);
            throw new DoneExecuteException("Done");
        }
        System.out.println(currentAction);
        game.actionProcess(currentAction);
    }

    public boolean changePlan(String constructionPlan){
        Tree tree = null;
        try{ tree = new PlanTree(constructionPlan); }
        catch (Exception e) {
            return false;
        }
        int currentPlayerIndex = game.getIndexCurrentPlayer();
        buffers[currentPlayerIndex] = constructionPlan;
        constructionPlans[currentPlayerIndex] = tree;
        currentPlan =  constructionPlans[currentPlayerIndex].iteratorRealTime();
        return true;
    }

    public DisplayPlan getCurrentPlan(){
        int index = game.getIndexCurrentPlayer();
        return new DisplayPlan(index, buffers[index]);
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

    public DisplayGameSystem getAllGameSystem(String status){
        return new DisplayGameSystem(status, this.currentAction, getAllPlayers(), getAllTerritory());
    }

    public DisplayGameSystem getCurrentGameSystem(String status){
        return new DisplayGameSystem(status, this.currentAction, new DisplayPlayer[]{game.getCurrentPlayer()}, game.getCurrentTerritory());
    }
}
