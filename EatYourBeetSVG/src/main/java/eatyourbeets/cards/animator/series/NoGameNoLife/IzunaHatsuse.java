package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorCard
{
    public static final String ID = Register(IzunaHatsuse.class);

    private boolean transformed;

    public IzunaHatsuse()
    {
        super(ID, 0, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF_AND_ENEMY);

        Initialize(4, 2, 4);
        SetUpgrade(2, 2, 2);

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

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetTransformed(GameUtilities.GetHealthPercentage(player) < 0.25f);
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
            GameActions.Bottom.Heal(this.magicNumber);
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        else
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
            GameActions.Bottom.GainBlock(this.block);
        }
    }

    private void SetTransformed(boolean value)
    {
        if (transformed != value)
        {
            transformed = value;

            if (transformed)
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "Alt"));
                this.type = CardType.ATTACK;

                cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);
            }
            else
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID));
                this.type = CardType.SKILL;

                cardText.OverrideDescription(null, true);
            }
        }
    }
}