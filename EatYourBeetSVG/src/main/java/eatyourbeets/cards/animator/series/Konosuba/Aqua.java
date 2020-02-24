package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Aqua extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Aqua.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Aqua(true), true);
    }

    private boolean transformed = false;
    private Aqua(boolean transformed)
    {
        this();

        SetTransformed(transformed);
    }

    public Aqua()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetHealing(true);
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    protected void OnUpgrade()
    {
        SetTransformed(transformed);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (upgraded && transformed)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (upgraded && transformed)
        {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!transformed)
        {
            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.Callback(__ -> SetTransformed(true));
        }
        else
        {
            GameActions.Bottom.VFX(new RainbowCardEffect());
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return !upgraded;
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
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], true);
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