package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_ChannelOrb;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class RundelhausCode extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RundelhausCode.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Normal)
            .SetSeries(CardSeries.LogHorizon);

    private static final HashSet<AbstractCard> buffs = new HashSet<>();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public RundelhausCode()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.LIGHTNING);
        GameActions.Bottom.GainIntellect(1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!CombatStats.GetCombatData(cardID + "_buffs", false))
        {
            CombatStats.SetCombatData(cardID + "_buffs", true);
            buffs.clear();
        }

        GameActions.Bottom.SelectFromHand(name, magicNumber, true)
        .SetOptions(true, false, true)
        .SetMessage(GR.Common.Strings.HandSelection.GenericBuff)
        .SetFilter(c -> c instanceof EYBCard && !GameUtilities.IsHindrance(c) && !buffs.contains(c) && (c.baseDamage >= 0 || c.baseBlock >= 0))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                ((EYBCard)c).AddScaling(Affinity.Blue, 2);
                buffs.add(c);
                c.flash();
            }
        });

        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_ChannelOrb(new Fire()));
                choices.AddEffect(new GenericEffect_ChannelOrb(new Lightning()));
                choices.AddEffect(new GenericEffect_ChannelOrb(new Frost()));
                choices.AddEffect(new GenericEffect_ChannelOrb(new Dark()));
            }

            choices.Select(GameActions.Bottom, 1, null)
            .CancellableFromPlayer(true);
        });
    }
}