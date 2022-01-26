package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_ChannelOrb;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashSet;

public class RundelhausCode extends PCLCard
{
    public static final PCLCardData DATA = Register(RundelhausCode.class)
            .SetAttack(2, CardRarity.COMMON, PCLAttackType.Electric, PCLCardTarget.Normal)
            .SetSeries(CardSeries.LogHorizon);

    private static final HashSet<AbstractCard> buffs = new HashSet<>();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public RundelhausCode()
    {
        super(DATA);

        Initialize(9, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.General, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.LIGHTNING);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!CombatStats.GetCombatData(cardID + "_buffs", false))
        {
            CombatStats.SetCombatData(cardID + "_buffs", true);
            buffs.clear();
        }

        PCLActions.Bottom.SelectFromHand(name, magicNumber, true)
        .SetOptions(true, false, true)
        .SetMessage(GR.PCL.Strings.HandSelection.GenericBuff)
        .SetFilter(c -> c instanceof PCLCard && !PCLGameUtilities.IsHindrance(c) && !buffs.contains(c) && (c.baseDamage >= 0 || c.baseBlock >= 0))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                ((PCLCard)c).AddScaling(PCLAffinity.Blue, 2);
                buffs.add(c);
                c.flash();
            }
        });

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            if (choices.TryInitialize(this))
            {
                for (PCLOrbHelper orb : PCLOrbHelper.ALL.values()) {
                    if (PCLGameUtilities.IsCommonOrb(orb.ID)) {
                        choices.AddEffect(new GenericEffect_ChannelOrb(orb, 1));
                    }
                }
            }

            choices.Select(PCLActions.Bottom, 1, null)
            .CancellableFromPlayer(true);
        });
    }
}