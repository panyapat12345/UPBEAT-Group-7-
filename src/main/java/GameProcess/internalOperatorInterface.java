package GameProcess;

import org.springframework.stereotype.Component;

@Component
public interface internalOperatorInterface {
    int rows();
    int cols();
    int currow();
    int curcol();
    int budget();
    int deposit();
    int interest();
    int maxDeposit();
    int random();
    int opponent();
    int nearby(String direction);

    player currentPlayer();
}
