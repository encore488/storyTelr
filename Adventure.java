import org.python.core.PyInstance;
import org.python.util.PythonInterpreter;

public class Adventure {
    private String paragraph;
    private String[] buttonText;
    private phase currPhase;
    private String adventure;

    public Adventure() {
        currPhase = phase.INITIATE;
        paragraph = "Enter a theme for your adventure:";
        buttonText = new String[4];
        adventure = "";
    }

    /**
     * In controller:
     * if (getParagraph() == ""): Theme Selection; render text + textbox
     * elif (getButtons() == null): Adventure End; render text-only (or perhaps with a "Save Adventure" button and a "New Session" button)
     * else: render text + buttons + textbox-button
     */

    public boolean submitChoice(int choiceNum) {
        String prompt;
        if (currPhase == phase.ACT) {
            prompt = "I select option " + choiceNum + ". Generate a paragraph describing my action, the consequences, " +
                    "and the resulting situation I find myself in. Generate 4 possible actions for the player to take " +
                    "that are less than 6 words long.";
        } else { //phase.SESSION_SELECTED
            prompt = "I select option " + choiceNum + ". My initial scenario is located in the selected option. " +
                    "Generate a paragraph describing the initial scenario and provide 4 possible actions for the player " +
                    "to take that are less than 6 words long.";
        }
        return communicateAI(prompt);
    }

    public boolean submitChoice(String choice) {
        String prompt;
        if (currPhase == phase.ACT) {
            prompt = "I decide to " + choice + ". Generate a paragraph describing my action, the consequences, " +
                    "and the resulting situation I find myself in. Generate 4 possible actions for the player to take " +
                    "that are less than 6 words long.";
        } else if (currPhase == phase.SESSION_SELECTED) {
            prompt = "I select a scenario where " + choice + ". My initial scenario is located in the selected option. " +
                    "Generate a paragraph describing the initial scenario and provide 4 possible actions for the player " +
                    "to take that are less than 6 words long.";
        } else {
            prompt = "Generate an introductory paragraph that summarizes the setting and provides 4 possible scenarios " +
            "for the player that are less than 6 words long. The theme is " + choice + ".";
        }
        return communicateAI(prompt);
    }

    private boolean communicateAI(String prompt) {
        try {
            //TODO: Send prompt to ChatGPT. If it fails, return false.
            String response = ""; //TODO: REPLACE WITH CODE
            interpretAIText(response);
            switch (currPhase) {
                case INITIATE -> currPhase = phase.SESSION_SELECTED;
                case SESSION_SELECTED -> currPhase = phase.ACT;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void interpretAIText(String text) {
        String[] lines = text.split("\n");
        paragraph = lines[0];

        adventure = (currPhase == phase.INITIATE) ? paragraph : adventure + "\n" + paragraph;

        if (lines.length >= 6) {
            for (int i = 4; i > 0; i--) {
                buttonText[4 - i] = lines[lines.length - i];
            }
        } else {
            buttonText = null;
        }
    }

    public String getParagraph() {
        return paragraph;
    }

    public String[] getButtonText() {
        return buttonText;
    }

    public String getAdventure() {
        return adventure;
    }


    private enum phase {
        INITIATE,
        SESSION_SELECTED,
        ACT
    }
}

