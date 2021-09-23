package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class KrulTepes extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KrulTepes.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    private static final AbstractRelic relicReward = new BloodVial();

    public KrulTepes()
    {
        super(DATA);

        Initialize(12, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.SCARLET)).duration)
            .AddCallback(enemy ->
            {
                if (GameUtilities.InEliteOrBossRoom() && GameUtilities.IsFatal(enemy, false) && info.TryActivateLimited())
                {
                    ObtainReward();
                }
            });

            GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
    }

    public void ObtainReward()
    {
        int totalRelics = 0;
        final ArrayList<AbstractRelic> relics = player.relics;
        for (AbstractRelic relic : relics)
        {
            if (relic.relicId.equals(relicReward.relicId))
            {
                totalRelics += 1;
            }
            else if (relic.relicId.equals(ExquisiteBloodVial.ID))
            {
                totalRelics = -1;
                break;
            }
        }

        if (totalRelics >= 5)
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new ExquisiteBloodVial());
        }
        else if (UnnamedReignRelic.IsEquipped())
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientMedallion());
        }
        else
        {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(relicReward.makeCopy());
        }
    }
}