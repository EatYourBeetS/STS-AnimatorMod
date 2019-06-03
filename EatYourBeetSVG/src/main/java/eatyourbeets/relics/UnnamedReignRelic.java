package eatyourbeets.relics;

import basemod.DevConsole;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.effects.RemoveRelicEffect;
import eatyourbeets.effects.SequentialEffect;
import eatyourbeets.effects.UnnamedRelicEquipEffect;
import eatyourbeets.interfaces.AllowedUnnamedReignRelic;
import eatyourbeets.powers.PlayerStatistics;

public abstract class UnnamedReignRelic extends AnimatorRelic
{
    public UnnamedReignRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, tier, sfx);
    }

    public static boolean IsEquipped()
    {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof UnnamedReignRelic)
            {
                return true;
            }
        }

        return false;
    }

// TODO: Re-Enable this
//
    @Override
    public void update()
    {
        super.update();
        if (isObtained)
        {
            DevConsole.visible = false;
            DevConsole.enabled = false;
            DevConsole.commandPos = -1;
            DevConsole.currentText = "";
        }
    }

    private void DisableConsole()
    {
        Gdx.input.setInputProcessor((InputProcessor) ReflectionHacks.getPrivate(null, DevConsole.class, "otherInputProcessor"));
        DevConsole.visible = false;
        DevConsole.enabled = false;
        DevConsole.commandPos = -1;
        DevConsole.currentText = "";
        Settings.isDebug = false;
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip)
    {
        super.instantObtain(p, slot, callOnEquip);

        if (PlayerStatistics.LoadingPlayerSave)
        {
            DisableConsole();
        }
    }

    protected abstract void OnManualEquip();

    public static void OnRelicReceived(AbstractRelic relic)
    {
        if (relic instanceof UnnamedReignRelic)
        {
            AbstractPlayer p = AbstractDungeon.player;

            SequentialEffect effect = new SequentialEffect();

            for (AbstractRelic r : p.relics)
            {
                if (r != relic && !(r instanceof AllowedUnnamedReignRelic))
                {
                    effect.Enqueue(new RemoveRelicEffect(relic, r));
                }
            }

            ((UnnamedReignRelic)relic).OnManualEquip();

            effect.Enqueue(new UnnamedRelicEquipEffect());

            AbstractDungeon.effectList.add(effect);
        }
        else if (!(relic instanceof AllowedUnnamedReignRelic))
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractRelic r : p.relics)
            {
                if ((r instanceof UnnamedReignRelic) && r != relic)
                {
                    AbstractDungeon.effectsQueue.add(new RemoveRelicEffect(r, relic));
                    r.flash();
                }
            }
        }
    }
}
