package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.ui.FtueTip;
import eatyourbeets.effects.player.SpawnRelicEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.InputManager;

public class Readme extends AnimatorRelic
{
    public static final String ID = CreateFullID(Readme.class.getSimpleName());

    private final String key;
    private final String header;
    private final String content;

    public Readme()
    {
        super(ID, ID, RelicTier.DEPRECATED, LandingSound.FLAT);

        key = "TheAnimator-Readme:<ERROR>";
        header = "<ERROR>";
        content = "<ERROR>";

        if (AbstractDungeon.player != null)
        {
            GameEffects.Queue.RemoveRelic(this);
        }
    }

    public Readme(String key, String header, String content)
    {
        super(ID, ID, RelicTier.DEPRECATED, LandingSound.FLAT);

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
            SpawnRelic(base + "Controller", 4);
        }

        SpawnRelic(base + "General", 0);
        SpawnRelic(base + "Synergies", 1);
        SpawnRelic(base + "Badges", 2);
    }

    private static void SpawnRelic(String key, int index)
    {
        AnimatorStrings.Tips Tips = GR.Animator.Strings.Tips;

        if (!GR.Animator.GetConfig().getBool(key))
        {
            GameEffects.Queue.Add(new SpawnRelicEffect(new Readme(key, Tips.Header(index), Tips.Content(index)),
            Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
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
    public void atBattleStart()
    {
        super.atBattleStart();

        if (AbstractDungeon.getCurrMapNode().y > 2)
        {
            GameEffects.Queue.RemoveRelic(this);
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (hb.hovered && !AbstractDungeon.isScreenUp && InputManager.RightClick.IsJustPressed())
        {
            AbstractDungeon.ftue = new FtueTip(header, content, Settings.WIDTH / 2f, Settings.HEIGHT / 2f, FtueTip.TipType.CARD_REWARD);

            GR.Animator.GetConfig().setBool(key, true);
            GR.Animator.SaveConfig();

            GameEffects.Queue.RemoveRelic(this);
        }
    }

    @Override
    public boolean canSpawn()
    {
        return false;
    }
}