package GameProcess;

import org.springframework.stereotype.Component;

@Component
public interface playerInterface {
    boolean isDefeat();
    int budget();
    peekCiryCrew getCityCrewInfo();
}
