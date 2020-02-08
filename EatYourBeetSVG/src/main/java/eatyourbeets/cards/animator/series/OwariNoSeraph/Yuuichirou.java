package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Asuramaru;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou.class).SetAttack(1, CardRarity.UNCOMMON);
    static
    {
        DATA.InitializePreview(new Asuramaru(), false);
    }

    public Yuuichirou()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(4, 0);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.OwariNoSeraph);
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