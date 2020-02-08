package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Shimakaze extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shimakaze.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Shimakaze()
    {
        super(DATA);

        Initialize(3, 3, 3);
        SetUpgrade(1, 1, 1);

        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.Draw(this.magicNumber);
        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}