package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class FeliciaMitsuki extends PCLCard
{
    public static final PCLCardData DATA = Register(FeliciaMitsuki.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public FeliciaMitsuki()
    {
        super(DATA);

        Initialize(8, 0, 3, 1);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0 ,0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(0,0,1);

        SetAffinityRequirement(PCLAffinity.Orange, 3);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + (PCLJUtils.Count(player.hand.group, PCLGameUtilities::HasLightAffinity)) * magicNumber);
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (IsStarter()) {
            PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile)
                    .SetOptions(true, false)
                    .SetFilter(c -> Curse_GriefSeed.DATA.ID.equals(c.cardID))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            PCLActions.Top.Motivate(c, 1);
                        }
                    });
        }
    }
}