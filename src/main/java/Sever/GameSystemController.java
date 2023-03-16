package Sever;
import Exception.*;
import GameProcess.Display.DisplayGameSystem;
import GameProcess.GameSystem;
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
        gameSystem = new GameSystem(configMessage);
    }

    @MessageMapping("/set/checkSyntax")
    @SendTo("/game/get/checkSyntax")
    public boolean isCorrectSyntax(String constructionPlan){
        return GameSystem.isCorrectSyntax(constructionPlan);
    }

    @MessageMapping("/set/constructionPlan")
    public void addBuffer(ConstructMessage constructionPlan){
        gameSystem.addBuffer(constructionPlan.getIndex(), constructionPlan.getConfigurationPlan());
    }

    @MessageMapping("/start")
    public void start(){
        gameSystem.start();
    }

    @MessageMapping("/want/data")
    @SendTo("/game/get/data")
    public ConstructMessage getCurrentPlayerData(){
        return gameSystem.getCurrentPlayerData();
    }

    @MessageMapping("/set/changePlan")
    @SendTo("/game/get/changePlan")
    public boolean changePlan(String constructionPlan){
        return gameSystem.changePlan(constructionPlan);
    }

    @MessageMapping("/nextAction")
    @SendTo("/game/display")
    public DisplayGameSystem nextAction(){
        try {
            gameSystem.nextAction();
        } catch (DoneExecuteException e){
            return new DisplayGameSystem("done", gameSystem.getCurrentAction(), gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
        } catch (WonException e){
            return new DisplayGameSystem("won", gameSystem.getCurrentAction(), gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
        }
        return new DisplayGameSystem("normal", gameSystem.getCurrentAction(), gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
    }

    @MessageMapping("/nextTurn")
    @SendTo("/game/display")
    public DisplayGameSystem nextTurn(){
        try {
            gameSystem.nextTurn();
        } catch (WonException e){
            return new DisplayGameSystem("won", null, gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
        }
        return new DisplayGameSystem("normal", null, gameSystem.getAllPlayers(), gameSystem.getAllTerritory());
    }
}
