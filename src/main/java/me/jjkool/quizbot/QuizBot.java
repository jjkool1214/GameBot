package me.jjkool.quizbot;

import me.jjkool.quizbot.listeners.CommandListener;
import me.jjkool.quizbot.listeners.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class QuizBot {

    public static final String TOKEN = System.getenv("TOKEN");

    public static JDA JDA_BOT;

    public static void main(String[] args) {
        try{
            startBot();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void startBot() throws InterruptedException{
        JDA_BOT = JDABuilder.createDefault(TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.getPrivileged())
                .addEventListeners(new CommandListener(), new MessageListener())
                .setActivity(Activity.playing("with my nuts ;)"))
                .build()
                .awaitReady();

        CommandListener.init();

    }

    public static Guild getGuild() {
        return JDA_BOT.getGuildById("1144410123150831716");
    }


}
