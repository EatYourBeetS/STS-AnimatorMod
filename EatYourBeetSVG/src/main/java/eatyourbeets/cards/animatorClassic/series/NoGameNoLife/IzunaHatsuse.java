package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(IzunaHatsuse.class).SetSeriesFromClassPackage().SetSkill(0, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new IzunaHatsuse(true), true);
    }

    private boolean transformed;
    private IzunaHatsuse(boolean transformed)
    {
        this();

        SetTransformed(transformed);
    }

    public IzunaHatsuse()
    {
        super(DATA);

        Initialize(4, 2, 4);
        SetUpgrade(2, 2, 2);

        SetTransformed(false);
        
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null && !transformed)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (transformed)
        {
            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        }
        else
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
            GameActions.Bottom.GainBlock(block);
        }
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        if (!transformed)
        {
            super.renderUpgradePreview(sb);
        }
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        if (transformed)
        {
            return null;
        }

        return super.GetCardPreview();
    }

    private void SetTransformed(boolean value)
    {
        if (transformed != value)
        {
            transformed = value;

            if (transformed)
            {
                LoadImage("Alt");
                SetAttackType(EYBAttackType.Normal);

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