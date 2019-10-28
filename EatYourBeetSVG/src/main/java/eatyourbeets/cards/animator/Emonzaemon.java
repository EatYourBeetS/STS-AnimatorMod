package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Emonzaemon extends AnimatorCard
{
    public static final String ID = Register(Emonzaemon.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Emonzaemon()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(4,0);

        SetSynergy(Synergies.Katanagatari);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new EntouJyuu(), true);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        EntouJyuu toAdd = new EntouJyuu();
        if (upgraded)
        {
            toAdd.upgrade();
        }

        GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(toAdd, 1, true, true));
        GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}