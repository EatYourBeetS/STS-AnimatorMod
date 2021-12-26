package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class GirlDeMo extends PCLCard
{
    public static final PCLCardData DATA = Register(GirlDeMo.class).SetSkill(3, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None).SetSeries(CardSeries.AngelBeats);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetAffinity_Star(1);
        SetHarmonic(true);
        SetExhaust(true);
        SetEthereal(true);
        SetAutoplay(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(1).AddCallback(() -> {
            PCLActions.Bottom.SelectFromHand(name, player.hand.size() - 1, true).AddCallback(cards -> {
                for (AbstractCard c : cards)
                {
                    PCLActions.Top.Motivate(c, 1);
                }
            });
        });

        for (int i = 0; i < magicNumber; i++) {
            PCLAffinity lowest = PCLJUtils.FindMin(PCLAffinity.Basic(), PCLCombatStats.MatchingSystem::GetPowerAmount);
            PCLActions.Bottom.StackAffinityPower(lowest, 1, false);
        }
    }
}