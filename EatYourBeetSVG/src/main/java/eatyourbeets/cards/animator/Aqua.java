package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;

public class Aqua extends AnimatorCard
{
    private boolean transformed = false;

    public static final String ID = Register(Aqua.class.getSimpleName());

    public Aqua() 
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2, 3);

        SetHealing(true);
        SetSynergy(Synergies.Konosuba);

        if (InitializingPreview())
        {
            Aqua copy = new Aqua(); // InitializingPreview will be true only once
            copy.SetTransformed(true);
            cardData.InitializePreview(copy, true);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (upgraded && transformed)
        {
            GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, secondaryValue);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded && transformed)
        {
            GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (!transformed)
        {
            GameActionsHelper.AddToBottom(new HealAction(p, p, magicNumber));
            GameActionsHelper.DrawCard(p, 1);
            GameActionsHelper.AddToBottom(new BecomeUselessAction());
        }
        else
        {
            GameActionsHelper.VFX(new RainbowCardEffect());
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade(false))
        {
            upgradeMagicNumber(1);
            SetTransformed(transformed);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Aqua other = (Aqua) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
    }

    private void SetTransformed(boolean value)
    {
        transformed = value;

        if (transformed)
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID + "2"));
            cardText.overrideDescription = cardData.strings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0];
            transformed = true;
        }
        else
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID));
            cardText.overrideDescription = null;
            transformed = false;
        }

        cardText.ForceRefresh();
    }

    private class BecomeUselessAction extends AnimatorAction
    {
        @Override
        public void update()
        {
            SetTransformed(true);

            this.isDone = true;
        }
    }
}