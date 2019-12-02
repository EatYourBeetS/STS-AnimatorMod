package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.ui.FtueTip;
import eatyourbeets.characters.AnimatorMetrics;
import eatyourbeets.effects.RemoveRelicEffect;
import eatyourbeets.effects.SpawnRelicEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.InputManager;

public class Readme extends AnimatorRelic
{
    private static final String[] TEXT = Resources_Animator_Strings.Tips.TEXT;
    public static final String ID = CreateFullID(Readme.class.getSimpleName());

    private final String key;
    private final String header;
    private final String content;

    public Readme(String key, String header, String content)
    {
        super(ID, ID, RelicTier.STARTER, LandingSound.FLAT);

        this.key = key;
        this.header = header;
        this.content = content;

        this.tips.clear();
        this.tips.add(new PowerTip(this.name + ": " + this.header, this.description));
        this.initializeTips();
    }

    public static void SpawnAll()
    {
        String base;
        if (Settings.language == Settings.GameLanguage.ZHS ||
            Settings.language == Settings.GameLanguage.ZHT)
        {
            base = "TheAnimator-Readme-" + Settings.language + ":";
        }
        else
        {
            base = "TheAnimator-Readme:";
        }

        if (Settings.isControllerMode)
        {
            SpawnRelic(base + "Controller", 7, 8);
        }

        SpawnRelic(base + "General", 1, 2);
        SpawnRelic(base + "Synergies", 3, 4);
        SpawnRelic(base + "Badges", 5, 6);
    }

    private static void SpawnRelic(String key, int headerIndex, int contentIndex)
    {
        if (!AnimatorMetrics.GetConfig().getBool(key))
        {
            AbstractDungeon.effectsQueue.add(new SpawnRelicEffect(
                    new Readme(key, TEXT[headerIndex], TEXT[contentIndex]),Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
        }
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        beginLongPulse();
        flash();
    }

    @Override
    public void update()
    {
        super.update();

        if (hb.hovered && !AbstractDungeon.isScreenUp && InputManager.RightClick.IsJustPressed())
        {
            AbstractDungeon.ftue = new FtueTip(header, content, Settings.WIDTH / 2f, Settings.HEIGHT / 2f, FtueTip.TipType.CARD_REWARD);
            AbstractDungeon.effectsQueue.add(new RemoveRelicEffect(this, this));
            AnimatorMetrics.GetConfig().setBool(key, true);
            AnimatorMetrics.SaveConfig();
        }
    }

    @Override
    public boolean canSpawn()
    {
        return false;
    }
}