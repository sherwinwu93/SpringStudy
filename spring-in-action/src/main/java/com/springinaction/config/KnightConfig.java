package com.springinaction.config;

import com.springinaction.knights.BraveKnight;
import com.springinaction.knights.Knight;
import com.springinaction.quest.Quest;
import com.springinaction.quest.SlayDragonQuest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class KnightConfig {
//    @Bean
    public Knight knight() {
        return new BraveKnight(quest());
    }

//    @Bean
    public Quest quest() {
        return new SlayDragonQuest(System.out);
    }
}
