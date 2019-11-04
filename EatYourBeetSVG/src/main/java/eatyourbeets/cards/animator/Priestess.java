package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ModifyMagicNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;

public class Priestess extends AnimatorCard_Cooldown
{
    public static final String ID = Register(Priestess.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Drawn);

    public Priestess()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 0, 5);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.GainTemporaryHP(p, p, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (HasActiveSynergy())
        {
            GameActionsHelper.GainTemporaryHP(p, p, 1);
        }

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-1);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return upgraded ? 1 : 2;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainTemporaryHP(p, magicNumber);
    }
}