package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.cards.base.attributes.MixedHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Aqua extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Aqua.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Aqua(true), true));

    private boolean transformed = false;

    public Aqua()
    {
        this(false);
    }

    private Aqua(boolean transformed)
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Light(1, 1, 0);
        SetAffinity_Blue(1);

        SetHealing(true);
        SetTransformed(transformed);
    }

    @Override
    protected void OnUpgrade()
    {
        SetTransformed(transformed);
    }
    
    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return transformed ? null : upgraded
             ? MixedHPAttribute.Instance.SetCard(this, true)
             : HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!transformed)
        {
            GameActions.Bottom.GainBlessing(1, upgraded);
            GameActions.Bottom.Heal(magicNumber).Overheal(upgraded);
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.Callback(() -> SetTransformed(true));
        }
        else
        {
            GameActions.Bottom.VFX(new RainbowCardEffect());
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Aqua other = (Aqua) super.makeStatEquivalentCopy();

        other.SetTransformed(transformed);

        return other;
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
        transformed = value;

        if (transformed)
        {
            LoadImage("2");
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            type = CardType.STATUS;
        }
        else
        {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
            type = CardType.SKILL;
        }
    }
}