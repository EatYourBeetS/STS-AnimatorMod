package eatyourbeets.cards.animatorClassic.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IrohaTamaki extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(IrohaTamaki.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public IrohaTamaki()
    {
        super(DATA);

        Initialize(3, 5, 2);
        SetUpgrade(0, 2, 1);

        
        SetSpellcaster();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);

        if (p.drawPile.size() > 0)
        {
            AbstractCard topCard = p.drawPile.getTopCard();
            if (GameUtilities.IsHindrance(topCard))
            {
                GameActions.Bottom.Exhaust(topCard);
            }
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.Scry(magicNumber);
        }
    }
}