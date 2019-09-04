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
    private boolean secondForm = false;

    public static final String ID = CreateFullID(Aqua.class.getSimpleName());

    public Aqua() 
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2, 3);

        this.tags.add(CardTags.HEALING);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (upgraded && secondForm)
        {
            GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, secondaryValue);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded && secondForm)
        {
            GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (!secondForm)
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
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);

            if (secondForm)
            {
                SetSecondForm();
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Aqua other = (Aqua) super.makeStatEquivalentCopy();

        if (secondForm)
        {
            other.SetSecondForm();
        }

        return other;
    }

    private void SetSecondForm()
    {
        this.loadCardImage(Resources_Animator.GetCardImage(ID + "2"));
        rawDescription = cardStrings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0];
        secondForm = true;
        initializeDescription();
    }

    private class BecomeUselessAction extends AnimatorAction
    {
        @Override
        public void update()
        {
            SetSecondForm();

            this.isDone = true;
        }
    }

    private static Aqua preview;

    @Override
    protected AbstractCard GetCardPreview()
    {
        if (preview == null || (preview.upgraded != this.upgraded))
        {
            preview = new Aqua();
            preview.SetSecondForm();

            if (upgraded)
            {
                preview.upgrade();
            }
        }

        return preview;
    }
}