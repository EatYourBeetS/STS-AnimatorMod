package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IrohaTamaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IrohaTamaki.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public IrohaTamaki()
    {
        super(DATA);

        Initialize(3, 5);
        SetUpgrade(0, 2);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.GainBlock(block);

        if (HasSynergy())
        {
            if (upgraded)
            {
                AbstractCard topCard = player.drawPile.getTopCard();
                if (GameUtilities.IsCurseOrStatus(topCard))
                {
                    GameActions.Top.Exhaust(topCard);
                }
            }

            GameActions.Bottom.Cycle(name, 1);
        }
    }
}