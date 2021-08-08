package eatyourbeets.cards.animator.colorless.uncommon;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Shimakaze extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shimakaze.class)
            .SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Kancolle);

    public Shimakaze()
    {
        super(DATA);

        Initialize(2, 2, 3);
        SetUpgrade(1, 1, 1);
        
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        GameActions.Bottom.RetainPower(Affinity.Green);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}