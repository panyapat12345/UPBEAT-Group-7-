package Sever;
import Exception.*;
import GameProcess.Display.DisplayGameSystem;
import GameProcess.GameSystem;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameSystemController {
//    @Autowired
    private GameSystem gameSystem;

    @MessageMapping("/set/configuration")
    public void newGame(ConfigMessage configMessage){
        System.err.println("new game ......");
//        System.out.println(configMessage);
        gameSystem = new GameSystem(configMessage);
    }

    @MessageMapping("/set/checkSyntax")
    @SendTo("/game/get/checkSyntax")
    public boolean isCorrectSyntax(StringMessage constructionPlan){
        return GameSystem.isCorrectSyntax(constructionPlan.getMessage());
    }

    @MessageMapping("/set/constructionPlan")
    public void addBuffer(ConstructMessage constructionPlan){
        System.err.println("add buffer ......");
        System.out.println(constructionPlan);
        gameSystem.addBuffer(constructionPlan.getIndex(), constructionPlan.getConfigurationPlan());
    }

    @MessageMapping("/start")
    public void start(){
        System.err.println("Start ......");
        gameSystem.start();
    }

    @MessageMapping("/want/data")
    @SendTo("/game/get/data")
    public ConstructMessage getCurrentPlayerData(){
        System.err.println("send data ......");
        return gameSystem.getCurrentPlayerData();
    }

    @MessageMapping("/set/changePlan")
    @SendTo("/game/get/changePlan")
    public boolean changePlan(StringMessage constructionPlan){
        System.err.println("change plan ......");
        return gameSystem.changePlan(constructionPlan.getMessage());
    }

    @MessageMapping("/nextAction")
    @SendTo("/game/display")
    public DisplayGameSystem nextAction(){
        System.err.println("next action ......");
        try {
            gameSystem.nextAction();
        } catch (DoneExecuteException e){
            return new DisplayGameSystem(gameSystem.getCurrentPlayerIndex(), "done", gameSystem.getCurrentAction(), gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
        } catch (WonException e){
            return new DisplayGameSystem(gameSystem.getCurrentPlayerIndex(), "won", gameSystem.getCurrentAction(), gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
        }
        return new DisplayGameSystem(gameSystem.getCurrentPlayerIndex(), "normal", gameSystem.getCurrentAction(), gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
    }

    @MessageMapping("/nextTurn")
    @SendTo("/game/display")
    public DisplayGameSystem nextTurn(){
        System.err.println("next turn ......");
        try {
            gameSystem.nextTurn();
        } catch (WonException e){
            return new DisplayGameSystem(gameSystem.getCurrentPlayerIndex(), "won", null, gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
        }
        return new DisplayGameSystem(gameSystem.getCurrentPlayerIndex(), "normal", null, gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
    }

    @MessageMapping("/get/init_plan_sec")
    @SendTo("/game/get/init_plan_sec")
    public int getInitPlanSec(){
        return gameSystem.getInitPlanSec();
    }

    @MessageMapping("/get/plan_rev_sec")
    @SendTo("/game/get/plan_rev_sec")
    public int getPlanRevSec(){
        return gameSystem.getPlanRevSec();
    }

    @MessageMapping("/set/plan_rev_sec")
    public void setPlanRevSec(TimeMessage timeMessage){
        gameSystem.setPlanRevSec(timeMessage.getSec());
    }
}
