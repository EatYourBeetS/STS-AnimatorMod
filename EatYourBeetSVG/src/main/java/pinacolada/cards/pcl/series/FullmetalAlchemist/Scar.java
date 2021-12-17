package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;

public class Scar extends PCLCard
{
    public static final PCLCardData DATA = Register(Scar.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Scar()
    {
        super(DATA);

        Initialize(14, 0, 0, 4);
        SetUpgrade(4, 0);

        SetAffinity_Red(1);
        SetAffinity_Orange(2, 0, 2);

        SetAffinityRequirement(PCLAffinity.Orange, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect(__ ->
        {
            SFX.Play(SFX.ORB_DARK_EVOKE, 0.5f, 0.7f);
            return 0f;
        }));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(true, true, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Top.ChannelOrb(new Earth());
            }
        });

        if (info.IsSynergizing || TrySpendAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
            .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue)
            .SetOptions(false, true)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.SMASH);
                    PCLActions.Top.MoveCard(c, player.exhaustPile, player.discardPile)
                    .ShowEffect(true, true);
                }
            });
        }
    }
}