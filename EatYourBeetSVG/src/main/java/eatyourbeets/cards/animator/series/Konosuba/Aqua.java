package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Aqua extends AnimatorCard
{
    public static final String ID = Register(Aqua.class);
    static
    {
        GetStaticData(ID).InitializePreview(new Aqua(true), true);
    }

    private boolean transformed = false;
    private Aqua(boolean transformed)
    {
        this();

        SetTransformed(transformed);
    }

    public Aqua()
    {
        super(ID, 0, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

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
            LoadImage("2");
            cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[upgraded ? 1 : 0], true);
        }
        else
        {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
        }
    }
}