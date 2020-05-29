/*
TODO: Replace this. Disabled Because of changes with Firebase.

package patches.metrics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.metrics.Metrics;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashMap;

@SpirePatch(clz= Metrics.class, method="run")
public class Metrics_Run
{
    private static final ArrayList<HashMap> cardsData = new ArrayList<>();
    private static final HashMap<Object, Object> params = new HashMap<>();
    private static final HashMap<Object, Object> params2 = new HashMap<>();
    private static final Gson gson = new Gson();

    @SpirePrefixPatch
    public static void Postfix(Metrics __instance)
    {
        if (Settings.UPLOAD_DATA && __instance.type == Metrics.MetricRequestType.UPLOAD_METRICS)
        {
            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && !Settings.isDebug && Settings.isStandardRun())
            {
                cardsData.clear();

                ArrayList<HashMap> cardChoices = CardCrawlGame.metricData.card_choices;
                if (cardChoices.size() > 2)
                {
                    for (HashMap choice : cardChoices)
                    {
                        AddOrUpdate((String) choice.get("picked"), true);

                        for (Object notPicked : (ArrayList)choice.get("not_picked"))
                        {
                            AddOrUpdate((String) notPicked, false);
                        }
                    }
                }
                else
                {
                    return;
                }

                params.clear();
                params.put("ascension", AbstractDungeon.isAscensionMode ? AbstractDungeon.ascensionLevel : 0);
                params.put("cards", cardsData);
                params.put("enteredAct5", GR.Common.Dungeon.IsUnnamedReign());
                params.put("isVictory", AbstractDungeon.is_victory);
                //params.put("startingSeries", GR.Animator.Data.SelectedLoadout.ID);
                params.put("language", Settings.language.name());

                String data = gson.toJson(params);
                String url = "https://us-central1-sts-theanimator-api.cloudfunctions.net/addMetrics";

                HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                Net.HttpRequest httpRequest = requestBuilder
                .newRequest()
                .method("POST")
                .header("Content-Type", "text/plain")
                .header("Accept", "text/plain")
                .header("User-Agent", "curl/7.43.0")
                .content(data)
                .url(url)
                .build();

                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener()
                {
                    public void handleHttpResponse(Net.HttpResponse httpResponse) { }

                    public void failed(Throwable t) {  }

                    public void cancelled() {  }
                });
            }
            else if (GR.Common.Dungeon.IsUnnamedReign())
            {
                params.clear();
                params.put("playerClass", AbstractDungeon.player.chosenClass.name());
                params.put("playerClassVictory", AbstractDungeon.is_victory);

                String data = gson.toJson(params);
                String url = "https://us-central1-sts-theanimator-api.cloudfunctions.net/addAlternativeMetrics";

                HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                Net.HttpRequest httpRequest = requestBuilder
                .newRequest()
                .method("POST")
                .header("Content-Type", "text/plain")
                .header("Accept", "text/plain")
                .header("User-Agent", "curl/7.43.0")
                .content(data)
                .url(url)
                .build();

                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener()
                {
                    public void handleHttpResponse(Net.HttpResponse httpResponse) { }

                    public void failed(Throwable t) {  }

                    public void cancelled() {  }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void AddOrUpdate(String cardMetricID, boolean picked)
    {
        if (!cardMetricID.equals("SKIP") && !cardMetricID.equals("Singing Bowl"))
        {
            String cardID= cardMetricID;
            if (cardID.contains("+"))
            {
                cardID = cardID.substring(0, cardID.indexOf("+"));
            }

            AbstractCard temp = GetCard(cardID);
            AnimatorCard card = JavaUtilities.SafeCast(temp, AnimatorCard.class);
            if (card != null)
            {
                for (HashMap<Object, Object> data : cardsData)
                {
                    if (data.get("ID").equals(card.cardID))
                    {
                        if (picked)
                        {
                            data.put("Pick", (int)data.get("Pick") + 1);
                        }
                        else
                        {
                            data.put("Skip", (int)data.get("Skip") + 1);
                        }

                        return;
                    }
                }

                HashMap<Object, Object> toAdd = new HashMap<>();
                toAdd.put("ID", card.cardID);
                //toAdd.put("Name", card.name);
                toAdd.put("Pick", picked ? 1 : 0);
                toAdd.put("Skip", picked ? 0 : 1);

                cardsData.add(toAdd);
            }
        }
    }

    private static AbstractCard GetCard(String cardID)
    {
        AbstractCard card = AnimatorCard_UltraRare.GetCards().get(cardID);
        if (card == null)
        {
            card = CardLibrary.getCard(cardID);
        }

        return card;
    }
}
*/