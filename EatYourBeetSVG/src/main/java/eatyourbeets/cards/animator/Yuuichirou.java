package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Yuuichirou extends AnimatorCard
{
    public static final String ID = Register(Yuuichirou.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Yuuichirou()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(8,0);

        SetSynergy(Synergies.OwariNoSeraph);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Asuramaru(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.MakeCardInDiscardPile(new Asuramaru(), 1, false);

        if (upgraded)
        {
            AbstractDungeon.player.discardPile.addToTop(new Dazed());
        }
        else
        {
            AbstractDungeon.player.discardPile.addToTop(new Wound());
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
        }
    }
}