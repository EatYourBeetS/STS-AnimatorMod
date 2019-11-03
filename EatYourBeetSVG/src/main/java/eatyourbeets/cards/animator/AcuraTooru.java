package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class AcuraTooru extends AnimatorCard
{
    public static final String ID = Register(AcuraTooru.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Discard);

    public AcuraTooru()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4, 0, 4, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.GainBlock(AbstractDungeon.player, magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        for (int i = 0; i < this.secondaryValue; i++)
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(ThrowingKnife.GetRandomCard()));
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainBlock(p, block);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}