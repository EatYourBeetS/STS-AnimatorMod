package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
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
        
        SetAffinity_Green(1);
        SetAffinity_Star(0,0,1);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}