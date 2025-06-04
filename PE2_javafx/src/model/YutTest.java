package model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class YutTest {

  @Test
  @DisplayName("randomYut의 결과가 0~5사이로 나오는 지 테스트")
  public void test_randomYut() {
    int result;
    for(int i = 0; i < 1000; i++) {
      result = Yut.throwYut();
      assertFalse(result < 0 || result > 5);
    }
  }

}
