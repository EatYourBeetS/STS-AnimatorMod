package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Kuribayashi extends AnimatorCard
{
    public static final String ID = Register(Kuribayashi.class.getSimpleName(), EYBCardBadge.Synergy);

    private static final int STRENGTH_DOWN = 4;

    public Kuribayashi()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0,1, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
        GameActionsHelper.DamageTarget(p, m, damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);

        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, magicNumber, false), magicNumber);
        GameActionsHelper.ApplyPower(p, m, new ChokePower(m, this.secondaryValue), this.secondaryValue);

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameUtilities.LoseTemporaryStrength(p, m, STRENGTH_DOWN);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeSecondaryValue(2);
        }
    }
}