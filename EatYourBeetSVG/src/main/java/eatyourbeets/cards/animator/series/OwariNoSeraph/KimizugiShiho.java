package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KimizugiShiho extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KimizugiShiho.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeries(CardSeries.OwariNoSeraph);

    public KimizugiShiho()
    {
        super(DATA);

        Initialize(2,2, 1,1);
        SetUpgrade(1,0, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Cycle(name, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.FetchFromPile(name,magicNumber,player.drawPile).SetFilter(GameUtilities::IsHindrance);
        if (info.IsSynergizing) {
            GameActions.Bottom.Cycle(name, secondaryValue);
        }
    }
}