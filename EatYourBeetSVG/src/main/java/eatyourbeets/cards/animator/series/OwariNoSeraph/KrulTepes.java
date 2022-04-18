package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.relics.animator.BloodVial_Alt;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class KrulTepes extends AnimatorCard
{
    private static final AbstractRelic relic = new BloodVial();
    private static final AbstractRelic relic_alt = new BloodVial_Alt();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(relic.name, relic.description);

    public static final EYBCardData DATA = Register(KrulTepes.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public KrulTepes()
    {
        super(DATA);

        Initialize(12, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2, 0, 1);

        SetObtainableInCombat(false);
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
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltip.SetIcon(relic);
            tooltip.id = cardID + ":" + tooltip.title;
            tooltips.add(tooltip);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.SCARLET)).duration)
            .AddCallback(info, (info2, enemy) ->
            {
                if (GameUtilities.InEliteOrBossRoom() && GameUtilities.IsFatal(enemy, false) && info2.TryActivateLimited())
                {
                    ObtainReward();
                }
            });

            GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
    }

    public void ObtainReward()
    {
        final AbstractRoom room = GameUtilities.GetCurrentRoom(true);

        int totalRelics = 0;
        for (AbstractRelic relic : player.relics)
        {
            if (relic.relicId.equals(KrulTepes.relic.relicId))
            {
                totalRelics += 1;
            }
        }
        for (RewardItem ri : room.rewards)
        {
            if (ri.relic != null && ri.relic.relicId.equals(KrulTepes.relic.relicId))
            {
                totalRelics += 1;
            }
        }

        if (UnnamedReignRelic.IsEquipped())
        {
            room.addRelicToRewards(new AncientMedallion());
        }
        else if (totalRelics > 0)
        {
            room.addRelicToRewards(relic_alt.makeCopy());
        }
        else
        {
            AbstractDungeon.commonRelicPool.remove(relic.relicId);
            room.addRelicToRewards(relic.makeCopy());
        }
    }
}