package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Asuramaru;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Yuuichirou extends AnimatorCard
{
    public static final String ID = Register(Yuuichirou.class, EYBCardBadge.Exhaust);

    public Yuuichirou()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(8, 0);
        SetUpgrade(4, 0);

        SetSynergy(Synergies.OwariNoSeraph);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Asuramaru(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Asuramaru());

        AbstractDungeon.player.discardPile.addToTop(new Wound());
        AbstractDungeon.player.discardPile.addToTop(new Wound());
    }
}