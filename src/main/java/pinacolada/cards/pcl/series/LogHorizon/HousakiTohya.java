package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class HousakiTohya extends PCLCard
{
    public static final PCLCardData DATA = Register(HousakiTohya.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new HousakiMinori(), false));

    public HousakiTohya()
    {
        super(DATA);

        Initialize(4, 0, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        if (IsStarter()) {
            PCLActions.Bottom.ApplyWeak(player, m, magicNumber);
            PCLActions.Bottom.ApplyVulnerable(player, m, magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(1)
                .SetFilter(c -> HousakiMinori.DATA.ID.equals(c.cardID), false)
                .AddCallback((cards) ->
                {
                    if (cards.size() > 0) {
                        PCLActions.Bottom.GainEnergyNextTurn(1);
                    }
                    else {
                        PCLActions.Bottom.GainEnergy(1);
                    }
                });
    }
}