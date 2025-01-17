package me.jjkool.quizbot.listeners;

import me.jjkool.quizbot.QuizBot;
import me.jjkool.quizbot.guessthatpokemon.GeneralTrivia;
import me.jjkool.quizbot.guessthatpokemon.GuessThatPokemon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class CommandListener extends ListenerAdapter {

    public static Channel activeQuizChannel = null;

    public static boolean gameStarted = false;

    public static void init() {
        QuizBot.getGuild().upsertCommand(Commands.slash("gtp", "Starts a game of Guess That Pokemon")
                .setDefaultPermissions(DefaultMemberPermissions.ENABLED)).queue();
        QuizBot.getGuild().upsertCommand(Commands.slash("quiz", "Generates a Quiz Game")
                .setDefaultPermissions(DefaultMemberPermissions.ENABLED))
                .queue();
    }

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e){

        if(e.getChannel().equals(ChannelType.VOICE)){

        }

        if(e.getChannel().asTextChannel().getId().equals("1145498800652304554")) {
            e.reply(":x: You cannot use that command here :x:").queue();
            return;
        }


        if(e.getFullCommandName().equals("gtp")){
            if(GuessThatPokemon.IN_GAME){
                e.reply("There is a game in progress, what the skib!").queue();
                return;
            }
            GuessThatPokemon.game(e);
        }

        if(e.getFullCommandName().equals("quiz")){
            if(!e.getChannel().equals(activeQuizChannel) && activeQuizChannel != null){
                e.reply("Sorry, but there is a game happening elsewhere!").setEphemeral(true).queue();
                return;
            }
            if(e.getChannel().equals(activeQuizChannel) && GeneralTrivia.inPlay){
                e.reply("So um... theres a game in progress in this channel lol").setEphemeral(true).queue();
                return;
            }
            GeneralTrivia.inPlay = true;
            try {

                activeQuizChannel = e.getChannel();
                e.reply("Starting the game! choose your difficulty").addActionRow(StringSelectMenu.create("choose-difficulty")
                        .addOption("Easy", "easy", "Sets the difficulty to easy")
                        .addOption("Medium", "medium", "Sets the difficulty to medium")
                        .addOption("Hard", "hard", "Sets the difficulty to hard").build()).queue();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run(){
                        if(!e.getChannel().equals(activeQuizChannel)){
                            return;
                        }
                        if(!gameStarted){
                            e.getChannel().asTextChannel().sendMessage("Nothing was selected! cancelling game").queue();
                            activeQuizChannel = null;
                        }

                    }
                }, 10000);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }


    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent e) {



        if(!e.getChannel().equals(activeQuizChannel)){
            return;
        }


        if(!e.getInteraction().getSelectMenu().getId().equals("choose-difficulty")){
            return;
        }


        GeneralTrivia.inPlay = true;
        gameStarted = true;
        e.reply("Difficulty :" + e.getValues().getFirst()+ "\nGame..... Start!").queue();
        GeneralTrivia.playTrivia(e);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {
        if(!GeneralTrivia.inPlay){
            e.reply("Sorry, this is from and old game. you should start a new game!").queue();
            return;
        }

        if(!e.getChannel().equals(activeQuizChannel)){
            return;
        }

        if(!e.getMessage().getId().equals(GeneralTrivia.currMessageID)){
            e.reply("um so... wrong question...").setEphemeral(true).queue();
            return;
        }

        Member author = e.getMember();
        if(GeneralTrivia.hasAnswered.containsKey(author.getEffectiveName())){
            e.reply("You've already attempted to answer this question!").setEphemeral(true).queue();
            return;
        }
        GeneralTrivia.hasAnswered.put(author.getEffectiveName(), true);
        boolean correctAnswer = false;
        int chosenAnswer = 0;

        switch (e.getButton().getId()){
            case "A-button":
                if(GeneralTrivia.answers.getFirst().equals(GeneralTrivia.currAnswer)){
                    correctAnswer = true;
                    if(GeneralTrivia.scoreTracking.containsKey(author.getEffectiveName())){
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), GeneralTrivia.scoreTracking.get(author.getEffectiveName())+1);
                    } else {
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), 1);
                    }
                }
                break;
            case "B-button":
                if(GeneralTrivia.answers.get(1).equals(GeneralTrivia.currAnswer)){
                    correctAnswer = true;
                    if(GeneralTrivia.scoreTracking.containsKey(author.getEffectiveName())){
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), GeneralTrivia.scoreTracking.get(author.getEffectiveName())+1);
                    } else {
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), 1);
                    }
                }
                chosenAnswer = 1;
                break;
            case "C-button":
                if(GeneralTrivia.answers.get(2).equals(GeneralTrivia.currAnswer)){
                    correctAnswer = true;
                    if(GeneralTrivia.scoreTracking.containsKey(author.getEffectiveName())){
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), GeneralTrivia.scoreTracking.get(author.getEffectiveName())+1);
                    } else {
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), 1);
                    }

                }
                chosenAnswer = 2;
                break;
            case "D-button":
                if(GeneralTrivia.answers.get(3).equals(GeneralTrivia.currAnswer)){
                    correctAnswer = true;
                    if(GeneralTrivia.scoreTracking.containsKey(author.getEffectiveName())){
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), GeneralTrivia.scoreTracking.get(author.getEffectiveName())+1);
                    } else {
                        GeneralTrivia.scoreTracking.put(author.getEffectiveName(), 1);
                    }

                }
                chosenAnswer = 3;
                break;
        }
        if(!GeneralTrivia.scoreTracking.containsKey(author.getEffectiveName())){
            GeneralTrivia.scoreTracking.put(author.getEffectiveName(), 0);
        }

        e.reply("You have selected " + GeneralTrivia.answers.get(chosenAnswer) + " and you got it" + (correctAnswer ? " right!" : " wrong :(. the correct answer was " + GeneralTrivia.currAnswer)).setEphemeral(true).queue();
    }


}
