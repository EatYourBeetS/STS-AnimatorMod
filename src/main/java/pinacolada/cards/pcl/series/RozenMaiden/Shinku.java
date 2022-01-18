package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Shinku extends PCLCard
{
    public static final PCLCardData DATA = Register(Shinku.class)
    		.SetAttack(1, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Shinku()
    {
        super(DATA);

        Initialize(3, 3, 2, 5);
        SetUpgrade(3, 1);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 7);
        SetAffinityRequirement(PCLAffinity.Dark, 7);
    }

    @Override
    public int GetXValue() {
        return magicNumber * player.hand.size();
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this,m, AttackEffects.SLASH_VERTICAL);

        PCLActions.Bottom.Cycle(name, magicNumber).AddCallback(() -> PCLActions.Bottom.ExhaustFromPile(name, 1, p.discardPile)
                .SetFilter(PCLGameUtilities::HasDarkAffinity)
                .SetOptions(false, true)
                .AddCallback(() ->
                {
                    PCLActions.Bottom.GainTemporaryHP(secondaryValue);
                }));

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Light, PCLAffinity.Dark).AddConditionalCallback(() -> {
            PCLActions.Bottom.GainTemporaryThorns(GetXValue());
        });
    }
}


