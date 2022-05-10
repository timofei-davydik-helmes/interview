package com.helmes.interview;

import com.helmes.interview.service.MainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberTest {

    MainService mainService = new MainService();

    @Test
    public void testNumberTransformation() {
        Assertions.assertEquals(4, mainService.transformNumber(7));
        Assertions.assertEquals(7, mainService.transformNumber(4));
    }
}
