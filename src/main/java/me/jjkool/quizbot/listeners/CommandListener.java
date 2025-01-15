package me.jjkool.quizbot.listeners;

import me.jjkool.quizbot.QuizBot;
import me.jjkool.quizbot.guessthatpokemon.GuessThatPokemon;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    public static void init() {
        QuizBot.getGuild().upsertCommand(Commands.slash("gtp", "Starts a game of Guess That Pokemon")
                .setDefaultPermissions(DefaultMemberPermissions.ENABLED)).queue();
    }

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e){
        System.out.println("Started");
        if(!e.getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        System.out.println("Is a text channel");
        if(e.getChannel().asTextChannel().getId().equals("1145498800652304554")) {
            e.reply(":x: You cannot use that command here :x:").queue();
            return;
        }
        System.out.println("Is good");


        if(e.getFullCommandName().equals("gtp")){
            if(GuessThatPokemon.IN_GAME){
                e.reply("There is a game in progress, what the skib!").queue();
                return;
            }
            System.out.println(e.getCommandString());
            GuessThatPokemon.game(e);
            System.out.println("Is replied");
        }
    }


}
