package Sever;

import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
public class ConstructMessage {
   private int index;
   private String configurationPlan;

   public ConstructMessage(int index, String configurationPlan){
      this.index = index;
      this.configurationPlan = configurationPlan;
   }

   @Override
   public String toString() {
      StringBuilder s = new StringBuilder();
      s.append("index : ");
      s.append(index);
      s.append("\nconfigurationPlan : \n");
      s.append(configurationPlan);
      return s.toString();
   }
}
