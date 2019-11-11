package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class TanyaDegurechaff extends AnimatorCard implements StartupCard
{
    public static final String ID = Register(TanyaDegurechaff.class.getSimpleName(), EYBCardBadge.Special);

    public TanyaDegurechaff()
    {
        super(ID, 2, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(4, 7);

        SetSynergy(Synergies.YoujoSenki);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new TanyaDegurechaff_Type95(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        int discarded = 0;
        for (AbstractCard card : p.hand.getSkills().group)
        {
            GameActionsHelper.AddToBottom(new DiscardSpecificCardAction(card, p.hand));
            discarded += 1;
        }

        for (int i = 0; i < discarded; i++)
        {
            GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
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

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.MakeCardInDrawPile(new TanyaDegurechaff_Type95(), 1, false);

            return true;
        }

        return false;
    }
}