package Sever;

import lombok.Getter;

@Getter
public class ConstructMessage {
   private int index;
   private String configurationPlan;

   public ConstructMessage(int index, String configurationPlan){
      this.index = index;
      this.configurationPlan = configurationPlan;
   }
}
