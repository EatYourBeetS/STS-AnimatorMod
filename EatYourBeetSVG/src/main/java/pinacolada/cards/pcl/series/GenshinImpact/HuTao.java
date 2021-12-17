package pinacolada.cards.pcl.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_SearingBurn;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class HuTao extends PCLCard
{
    public static final PCLCardData DATA = Register(HuTao.class).SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .SetMultiformData(2)
            .PostInitialize(data -> data.AddPreview(new Curse_SearingBurn(), false));

    public HuTao()
    {
        super(DATA);

        Initialize(2, 0, 4, 2);
        SetUpgrade(2, 0, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Dark(1, 0, 8);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form != 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (int i = 0; i < secondaryValue; i++)
        {
            PCLActions.Bottom.MakeCardInHand(new Curse_SearingBurn())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d.SetVFXColor(Color.PURPLE));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, magicNumber, p.drawPile, p.hand)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        PCLActions.Bottom.Exhaust(card)
                                .ShowEffect(true, true)
                                .AddCallback(() -> PCLActions.Bottom.MakeCardInHand(new Curse_SearingBurn()));
                    }

                    if (cards.size() > 0) {
                        PCLActions.Bottom.GainEnergy(cards.size());
                    }
                });

    }
}