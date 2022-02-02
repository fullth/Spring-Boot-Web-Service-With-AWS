package com.fullth.web.springboot.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트() {

        String name = "test";
        int amount = 1000;

        HelloResponseDto helloResponseDto = new HelloResponseDto(name, amount);

        Assertions.assertEquals(helloResponseDto.getName(), name);
        Assertions.assertEquals(helloResponseDto.getAmount(), amount);

    }
}