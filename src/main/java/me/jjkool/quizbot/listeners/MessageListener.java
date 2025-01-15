package me.jjkool.quizbot.listeners;

import me.jjkool.quizbot.QuizBot;
import me.jjkool.quizbot.guessthatpokemon.GuessThatPokemon;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!GuessThatPokemon.IN_GAME){
            return;
        }

        if (e.getAuthor().isBot()) return;

        if(!e.getChannel().equals(GuessThatPokemon.CHANNEL)){
            return;
        }

        String message = e.getMessage().getContentRaw();
        if(!message.equalsIgnoreCase(GuessThatPokemon.ANSWER_POKEMON.getName())){
            System.out.println("erm....");
            return;
        }

        System.out.println("WHATTTT");
        e.getChannel().sendMessage("Good job, " + e.getAuthor().getName() + " got it right!").queue();

        GuessThatPokemon.IN_GAME = false;
        GuessThatPokemon.TIMER.cancel();

    }
}
