package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class TanyaDegurechaff extends AnimatorCard
{
    public static final String ID = CreateFullID(TanyaDegurechaff.class.getSimpleName());

    public TanyaDegurechaff()
    {
        super(ID, 2, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(12, 0, 4);

        SetSynergy(Synergies.YoujoSenki);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(new TanyaDegurechaff_Type95(), 1, true, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int discarded = 0;
        for (AbstractCard card : p.hand.getSkills().group)
        {
            GameActionsHelper.AddToBottom(new DiscardSpecificCardAction(card, p.hand));
            discarded += 1;
        }

        if (discarded > 0)
        {
            GameActionsHelper.GainBlock(p, this.magicNumber * discarded);
        }

        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }
}