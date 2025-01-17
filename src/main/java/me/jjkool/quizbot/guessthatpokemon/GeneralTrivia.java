package me.jjkool.quizbot.guessthatpokemon;



import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class GeneralTrivia {

    public static String currAnswer = "";
    public static ArrayList<String> answers = new ArrayList<>();
    public static Map<String, Integer> scoreTracking;
    public static int questionNumber = 0;
    public static HashMap<String, Boolean> hasAnswered = new HashMap<>();
    public static boolean inPlay = false;
    public static String currMessageID;


    public static void playTrivia(@NotNull StringSelectInteractionEvent e){
        JSONArray questions = getQuestions(e.getValues().getFirst()).getJSONArray("results");
        System.out.println(questions.toString());
        scoreTracking = new TreeMap<>();


            hasAnswered.clear();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    hasAnswered.clear();
                    System.out.println(questionNumber);
                    if(questionNumber >= 10){
                        TreeMap<String, Integer> sortedMap = valueSort(scoreTracking);
                        StringBuilder output = new StringBuilder();
                        int position = 1;
                        for(String key : sortedMap.keySet()){
                            output.append(position).append(". ").append(key).append(" with a score of ").append(sortedMap.get(key)).append("\n");
                        }
                        e.getChannel().asTextChannel().sendMessage("Alright, that's the game and the scores are in. Here are the results!\n" + output).queue();
                        this.cancel();
                        timer.cancel();
                        inPlay = false;
                        return;

                    }
                    ArrayList<String> optionNumbers = new ArrayList<>();
                    JSONObject question = questions.getJSONObject(questionNumber);

                    System.out.println(question.toString());
                    currAnswer = URLDecoder.decode(question.getString("correct_answer"), StandardCharsets.UTF_8);
                    optionNumbers.add(URLDecoder.decode(question.getString("correct_answer"), StandardCharsets.UTF_8));
                    optionNumbers.add(question.getJSONArray("incorrect_answers").getString(0));
                    optionNumbers.add(question.getJSONArray("incorrect_answers").getString(1));
                    optionNumbers.add(question.getJSONArray("incorrect_answers").getString(2));

                    Collections.shuffle(optionNumbers);
                    answers = optionNumbers;
                    e.getChannel().asTextChannel().sendMessage(URLDecoder.decode(question.getString("question"), StandardCharsets.UTF_8)
                                    + " Options are as follows:"
                                    + "\nA. " + answers.get(0)
                                    + "\nB. " + answers.get(1)
                                    + "\nC. " + answers.get(2)
                                    + "\nD. " + answers.get(3))
                            .addActionRow(
                                    Button.primary("A-button", "A"), Button.primary("B-button", "B"), Button.primary("C-button", "C"), Button.primary("D-button", "D")
                            ).queue(message -> currMessageID = message.getId());

                    System.out.println(optionNumbers.toString() + "\n" + String.valueOf(questionNumber));
                    questionNumber++;
                }
            }, 0, 15000);




    }

    public static JSONObject getQuestions(String difficulty){
        JSONObject jsonObject = null;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create("https://opentdb.com/api.php?type=multiple&amount=10&difficulty=" + difficulty))
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

    public static <K, V extends Comparable<V> > TreeMap<K, V> valueSort(final Map<K, V> map)
    {
        // Static Method with return type Map and
        // extending comparator class which compares values
        // associated with two keys
        Comparator<K> valueComparator = new Comparator<K>() {

            // return comparison results of values of
            // two keys
            public int compare(K k1, K k2)
            {
                return map.get(k1).compareTo(map.get(k2));
            }

        };

        // SortedMap created using the comparator
        TreeMap<K, V> sorted = new TreeMap<K, V>(valueComparator);

        sorted.putAll(map);

        return sorted;
    }
}
