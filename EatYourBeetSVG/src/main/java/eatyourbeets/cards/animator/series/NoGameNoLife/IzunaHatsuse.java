package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorCard
{
    public static final String ID = Register_Old(IzunaHatsuse.class);
    static
    {
        GetStaticData(ID).InitializePreview(new IzunaHatsuse(true), true);
    }

    private boolean transformed;
    private IzunaHatsuse(boolean transformed)
    {
        this();

        SetTransformed(transformed);
    }

    public IzunaHatsuse()
    {
        super(ID, 0, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF_AND_ENEMY);

        Initialize(4, 2, 4);
        SetUpgrade(2, 2, 2);

        SetTransformed(false);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (transformed)
        {
            return null;
        }

        return super.GetBlockInfo();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (!transformed)
        {
            return null;
        }

        return super.GetDamageInfo().AddMultiplier(2);
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
                LoadImage("Alt");

                this.type = CardType.ATTACK;

                cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            }
            else
            {
                LoadImage(null);

                this.type = CardType.SKILL;

                cardText.OverrideDescription(null, true);
            }
        }
    }
}