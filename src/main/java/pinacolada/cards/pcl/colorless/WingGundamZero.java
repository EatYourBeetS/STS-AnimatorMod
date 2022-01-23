package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.IonizingStorm;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class WingGundamZero extends PCLCard
{
    public static final PCLCardData DATA = Register(WingGundamZero.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.Self).SetColor(CardColor.COLORLESS)
            .SetMaxCopies(1)
            .SetSeries(CardSeries.Gundam)
            .PostInitialize(data -> {data.AddPreview(new IonizingStorm(), false);
            });


    public WingGundamZero()
    {
        super(DATA);

        Initialize(0, 5, 8, 8);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Silver(1, 0, 2);
        SetAffinity_Light(1, 0, 2);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Silver, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CardGroup[] groups = upgraded ? (new CardGroup[] {p.discardPile, p.drawPile, p.hand}) : (new CardGroup[] {p.hand});
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SelectFromPile(name, magicNumber, groups)
                .SetOptions(false, true)
                .AddCallback((cards) -> {
                   for (AbstractCard c : cards) {
                       PCLCard pC = PCLJUtils.SafeCast(c, PCLCard.class);
                       if (pC != null && pC.maxUpgradeLevel > 0) {
                           pC.maxUpgradeLevel += 1;
                       }
                       c.upgrade();
                   }
                });

        if (CheckSpecialCondition(true) && info.TryActivateLimited()) {
            AbstractCard c = new IonizingStorm();
            c.applyPowers();
            PCLActions.Bottom.PlayCopy(c, null);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        int upgraded = PCLJUtils.Count(player.discardPile.group, c -> c.upgraded)
                + PCLJUtils.Count(player.drawPile.group, c -> c.upgraded)
                + PCLJUtils.Count(player.hand.group, c -> c.upgraded);
        return upgraded >= secondaryValue;
    }
}