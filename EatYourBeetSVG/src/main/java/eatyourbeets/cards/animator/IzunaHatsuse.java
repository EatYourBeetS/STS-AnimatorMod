package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorCard
{
    public static final String ID = Register(IzunaHatsuse.class.getSimpleName(), EYBCardBadge.Special);

    private boolean transformed;

    public IzunaHatsuse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(4, 2, 4);

        SetTransformed(false);
        SetSynergy(Synergies.NoGameNoLife);

        if (InitializingPreview())
        {
            // InitializingPreview will only be true once
            IzunaHatsuse copy = new IzunaHatsuse();
            copy.SetTransformed(true);
            cardData.InitializePreview(copy, true);
        }
    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        SetTransformed(GameUtilities.GetHealthPercentage(AbstractDungeon.player) < 0.25f);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        IzunaHatsuse other = (IzunaHatsuse) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (transformed)
        {
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            GameActionsHelper.AddToBottom(new HealAction(p, p, this.magicNumber));
        }
        else
        {
            GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
            GameActionsHelper.GainBlock(p, this.block);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
            upgradeDamage(2);
            upgradeBlock(2);
        }
    }

    private void SetTransformed(boolean value)
    {
        if (transformed != value)
        {
            transformed = value;

            if (transformed)
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID + "Alt"));
                this.type = CardType.ATTACK;

                cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
            }
            else
            {
                this.loadCardImage(Resources_Animator.GetCardImage(ID));
                this.type = CardType.SKILL;

                cardText.OverrideDescription(null, true);
            }
        }
    }
}