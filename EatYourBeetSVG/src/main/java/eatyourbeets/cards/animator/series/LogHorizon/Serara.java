package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashSet;

public class Serara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Serara.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    private static final HashSet<AbstractCard> buffs = new HashSet<>();

    public Serara()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (JUtils.Count(GameUtilities.GetIntents(), i -> i.isAttacking) == 0)
        {
            return;
        }

        if (!CombatStats.GetCombatData(cardID + "_buffs", false))
        {
            CombatStats.SetCombatData(cardID + "_buffs", true);
            buffs.clear();
        }

        GameActions.Bottom.GainForce(1);
        GameActions.Bottom.SelectFromHand(name, 1, !upgraded)
        .SetOptions(false, false, false)
        .SetMessage(GR.Common.Strings.HandSelection.GenericBuff)
        .SetFilter(c -> c instanceof EYBCard && !GameUtilities.IsCurseOrStatus(c) && !buffs.contains(c) && (c.baseDamage > 0 || c.baseBlock > 0))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                ((EYBCard)c).forceScaling += 2;
                buffs.add(c);
                c.flash();
            }
        });
    }
}