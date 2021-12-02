package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.UUID;

public class Serara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Serara.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None, true)
            .SetSeries(CardSeries.LogHorizon);
    private static HashMap<UUID, Integer> buffs;

    public Serara()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.MoveCards(player.hand, player.drawPile, 1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (JUtils.Count(GameUtilities.GetIntents(), EnemyIntent::IsAttacking) == 0)
        {
            return;
        }

        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        GameActions.Bottom.GainEndurance(secondaryValue);
        GameActions.Bottom.SelectFromHand(name, 1, !upgraded)
        .SetOptions(false, false, false)
        .SetMessage(GR.Common.Strings.HandSelection.GenericBuff)
        .SetFilter(c -> c instanceof EYBCard && !GameUtilities.IsHindrance(c) && buffs.getOrDefault(c.uuid, 0) < secondaryValue && (c.baseDamage >= 0 || c.baseBlock >= 0))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.IncreaseScaling(c, Affinity.Orange, secondaryValue);
                JUtils.IncrementMapElement(buffs, c.uuid, secondaryValue);
                c.flash();
            }
        });
    }
}