package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.animator.ScarAction;
import eatyourbeets.actions._legacy.common.RefreshHandLayoutAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class Scar extends AnimatorCard
{
    public static final String ID = Register(Scar.class.getSimpleName(), EYBCardBadge.Special);

    public Scar()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(12,0, 0, 30);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToTop(new RefreshHandLayoutAction());
        GameActionsHelper.AddToTop(new ExhaustAction(p, p, 1, true));

        CardCrawlGame.sound.playA("ORB_DARK_EVOKE", -0.3F);
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.NONE);

        if (p.masterDeck.size() >= secondaryValue && EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.AddToBottom(new ScarAction(p));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}