package me.jjkool.quizbot.guessthatpokemon;

import com.github.oscar0812.pokeapi.models.pokemon.Pokemon;
import com.github.oscar0812.pokeapi.utils.Client;
import me.jjkool.quizbot.QuizBot;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.ImageProxy;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GuessThatPokemon {

    public static final Random RANDOM = new Random();

    public static int NUM_OF_POKEMON = 1025;

    public static Timer TIMER = null;

    public static boolean IN_GAME = false;

    public static TextChannel CHANNEL;

    public static Pokemon ANSWER_POKEMON;

    public static void game(@NotNull SlashCommandInteractionEvent e) {
        ANSWER_POKEMON = getRandomPokemon();
        CHANNEL = e.getChannel().asTextChannel();
        ImageProxy imageProxy = new ImageProxy(ANSWER_POKEMON.getSprites().getFrontDefault());
        try{
            FileUpload file = FileUpload.fromData(imageProxy.download().get(), "nunya.png");
            e.reply(new MessageCreateBuilder().setContent("Who's that pokemon!").setFiles(file).build()).queue();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        IN_GAME = true;
        TIMER = new Timer();
        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                IN_GAME = false;
                CHANNEL.sendMessage("Nobody got it right! the correct pokemon is.... " + ANSWER_POKEMON.getName() + "!").queue();

            }
        }, 20000);

    }

    public static Pokemon getRandomPokemon(){
        return Client.getPokemonById(RANDOM.nextInt(NUM_OF_POKEMON));
    }


}
