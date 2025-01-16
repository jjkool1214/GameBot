package me.jjkool.quizbot.guessthatpokemon;



import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GeneralTrivia {

    public static String currAnswer = "";
    public static ArrayList<String> answers = new ArrayList<>();
    public static HashMap<String, Integer> scoreTracking =  new HashMap<>();

    public static void playTrivia(@NotNull StringSelectInteractionEvent e){
        JSONArray questions = getQuestions(e.getValues().getFirst()).getJSONArray("results");
        System.out.println(questions.toString());
        ArrayList<String> optionNumbers = new ArrayList<>();


        for(int i = 0; i < questions.length(); i++){
            JSONObject question = questions.getJSONObject(i);
            currAnswer = question.getString("correct_answer");
            optionNumbers.add(question.getString("correct_answer"));
            optionNumbers.add(question.getJSONArray("incorrect_answer").getString(0));
            optionNumbers.add(question.getJSONArray("incorrect_answer").getString(1));
            optionNumbers.add(question.getJSONArray("incorrect_answer").getString(2));

            Collections.shuffle(optionNumbers);
            answers = optionNumbers;
            e.getChannel().asTextChannel().sendMessage(question.getString("question")
                    + "Options are as follows:"
                    + "\nA. " + optionNumbers.get(0)
                    + "\nB. " + optionNumbers.get(1)
                    + "\nC. " + optionNumbers.get(2)
                    + "\nD. " + optionNumbers.get(3))
                    .addActionRow(
                        Button.primary("A-button", "A"), Button.primary("B-button", "B"), Button.primary("C-button", "C"), Button.primary("D-button", "D")
                    ).queue();


        }




    }

    public static JSONObject getQuestions(String difficulty){
        JSONObject jsonObject = null;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create("https://opentdb.com/api.php?amount=10&difficulty=" + difficulty))
                        .header("accept", "application/json").GET().build();
        try{

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            jsonObject = new JSONObject(response.body());
            System.out.println(jsonObject.get("results").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
        return jsonObject;
    }
}
