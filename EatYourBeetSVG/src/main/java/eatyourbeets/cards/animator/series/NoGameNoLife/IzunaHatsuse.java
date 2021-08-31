package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IzunaHatsuse.class)
            .SetSkill(0, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IzunaHatsuse(true), true));

    private boolean transformed;

    public IzunaHatsuse()
    {
        this(false);
    }

    private IzunaHatsuse(boolean transformed)
    {
        super(DATA);

        Initialize(4, 2, 4);
        SetUpgrade(2, 2, 2);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetTransformed(transformed);
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
    public AbstractAttribute GetSpecialInfo()
    {
        return transformed ? HPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return transformed ? null : super.GetBlockInfo();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return transformed ? super.GetDamageInfo().AddMultiplier(2) : null;
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
        final IzunaHatsuse other = (IzunaHatsuse) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
        return transformed ? null : super.GetCardPreview();
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

                //TODO: handle cases like Serara
                affinities.List.clear();
                SetAffinity_Red(1);
                SetAffinity_Green(2, 0, 1);
                SetAffinity_Dark(1);
                this.type = CardType.ATTACK;

                cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            }
            else
            {
                LoadImage(null);
                affinities.List.clear();
                SetAffinity_Green(1);

                this.type = CardType.SKILL;

                cardText.OverrideDescription(null, true);
            }
        }
    }
}