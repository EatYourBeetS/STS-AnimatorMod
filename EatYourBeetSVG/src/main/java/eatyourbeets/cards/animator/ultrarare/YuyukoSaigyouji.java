package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.PetalEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.SpecialAttribute;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class YuyukoSaigyouji extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(YuyukoSaigyouji.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    public YuyukoSaigyouji()
    {
        super(DATA);

        Initialize(0, 0, 34, 10);
        SetUpgrade(0, 0, 6, 0);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetInnate(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return inBattle ? SpecialAttribute.Instance.SetCard(this, null, true) : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DrawNextTurn(1);

        final ArrayList<AbstractMonster> targets = new ArrayList<>();
        for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
        {
            if (GameUtilities.GetHP(m1, false, false) < magicNumber)
            {
                targets.add(m1);
            }
        }

        if (targets.size() > 0)
        {
            GameActions.Bottom.SFX(SFX.STANCE_ENTER_CALM, 0.6f, 0.66f, 0.85f);
            GameActions.Bottom.SFX(SFX.TINGSHA, 0.1f, 0.12f, 0.8f);
            GameActions.Bottom.BorderLongFlash(Color.PINK);
            GameActions.Bottom.Callback(info, this::EnqueuePetalEffect);

            for (AbstractMonster m1 : targets)
            {
                GameActions.Bottom.Add(new InstantKillAction(m1));
            }
        }
        else
        {
            GameActions.Bottom.SFX(SFX.APPEAR);
        }

        GameActions.Delayed.Callback(targets, (enemies, __) ->
        {
            for (AbstractMonster enemy : enemies)
            {
                if (GameUtilities.IsFatal(enemy, true))
                {
                    GameActions.Bottom.GainIntangible(1);
                    return;
                }
            }

            GameActions.Bottom.ModifyAllInstances(uuid)
            .AddCallback(c ->
            {
                final int bonus = Mathf.RoundToInt(c.baseMagicNumber * secondaryValue * 0.01f);
                if ((bonus + c.baseMagicNumber) > 999)
                {
                    GameUtilities.ModifyMagicNumber(c, 999, false);
                }
                else
                {
                    GameUtilities.IncreaseMagicNumber(c, bonus, false);
                }
            });
        });
    }

    private void EnqueuePetalEffect(CardUseInfo info, AbstractGameAction action)
    {
        final int baseTimes = 12;
        final int times = info.HasData() ? info.GetData(baseTimes) : baseTimes;
        if (times > 0)
        {
            info.SetData(times - 1);
            GameEffects.Queue.Add(new PetalEffect());
            GameEffects.Queue.Add(new PetalEffect());
            GameEffects.Queue.Callback(new WaitRealtimeAction(0.1f))
            .AddCallback(info, this::EnqueuePetalEffect);
        }
    }
}